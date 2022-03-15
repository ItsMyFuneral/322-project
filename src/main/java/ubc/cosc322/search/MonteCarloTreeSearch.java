package ubc.cosc322.search;

import java.util.*;
import java.util.concurrent.*;

import ubc.cosc322.board.tiletypes.*;
import ubc.cosc322.messages.*;
import ubc.cosc322.board.*;

// UCT: Upper Confidence bound applied to Trees. Relevant for MCTS, improves search.
// Doesn't ignore less-played nodes but focuses on more promising ones.
// sqrt(2) is just the default, apparently? I remember it coming up in the slides.
class UCT {
	public static double uctValue(double totalTrials, double nodeWinScore, double nodeTrials) {
		if(nodeTrials == 0) { return Double.MAX_VALUE; }
		return ((double) nodeWinScore / (double) nodeTrials) + 
				Math.sqrt(2) * Math.sqrt(Math.log(totalTrials) / (double) nodeTrials);
	}
	
	public static MCTSNode findBestNodeUCT(MCTSNode node) {
		double parentTrials = node.board.numTrials.get();
		ArrayList<MCTSNode> nodes = new ArrayList<>();
		
		//add grandchildren, i.e. states after one of our moves and one of pone's, to list of nodes
		for(MCTSNode ch : node.getChildren())
		{
			nodes.addAll(ch.getChildren());
		}
		
		return Collections.max(nodes,
				Comparator.comparing(c -> uctValue(parentTrials, c.board.numWins.get(), c.board.numTrials.get())));
	}
}

// for multithreading later
class Worker extends Thread {
	MCTSNode rootNode;
	long endTime;
	MonteCarloTreeSearch mcts;
	
	TerritoryHeuristic heur = new TerritoryHeuristic();
	DeadMonarchHeuristic dmh = new DeadMonarchHeuristic();
	
	public Worker(MCTSNode root, long endTime, MonteCarloTreeSearch mcts)
	{
		this.rootNode = root;
		this.endTime = endTime;
		this.mcts = mcts;
	}
	public void run() {
		while(System.nanoTime() < endTime) {
			try {
				MCTSNode n = mcts.selectPromising(rootNode);
				double result = mcts.randomPlayout(n, heur, dmh);
				mcts.backPropagate(n, result);
			}
			catch(Exception e) {
				System.out.println("error");
			}
		}
	}
}

public class MonteCarloTreeSearch {
	public MCTSNode rootNode;
	public boolean isWhite;
	public int playouts = 0;
	//something about heuristic in here? idk
	private double heuristic = 0;
	
	public ArrayList<MCTSState> stateUpdates = new ArrayList<>();
	
	//multithreading config stuff
	final double MAX_TIME_DEFAULT = 29.5;
	final double NUM_THREADS = Runtime.getRuntime().availableProcessors();
	
	double MAX_TIME = 29.5;
	
	public MonteCarloTreeSearch(MCTSNode root) {
		rootNode = root;
	}
	
	public void performSearch(boolean isBot)
	{
		if(isBot) MAX_TIME = MAX_TIME_DEFAULT - 1;
		performSearch();
	}
	
	public void performSearch() {
		// do something in here, idk
		this.heuristic = (new TerritoryHeuristic()).calc(rootNode.board);
		
		rootNode.setChildren();
		
		long start = System.nanoTime(); //now
		long end = start + ((long) (MAX_TIME + TimeUnit.SECONDS.toNanos(1)));
		playouts = 0;
		
		ArrayList<Worker> workers = new ArrayList<>();
		
		for(int i = 0; i < NUM_THREADS; i++)
		{
			Worker w = new Worker(rootNode, end, this);
			w.start();
			workers.add(w);
		}
		
		boolean running = true;
		while(running)
		{
			running = false;
			for(Thread t : workers) if(t.isAlive()) running = true;
		}
	}
	
	public MCTSNode findNextMove() {
		if(rootNode.board.status() != 0.5) return rootNode;
		
		rootNode.board.numTrials.set(0);
		rootNode.board.numWins.set(0);
		performSearch();
		
		MCTSNode winnerNode = null;
		
		//iterate through children
		for(MCTSNode ch : rootNode.getChildren())
		{
			//get 
			Queen q = ch.getQueen();
			Arrow a = ch.getArrow();
			
			for(MCTSState u : stateUpdates)
			{
				if(u.q.friendly == q.friendly && u.q.col == q.col && u.q.row == q.row &&
						u.q.prevRow == q.prevRow && u.q.prevCol == q.prevCol &&
						u.a.row == a.row && u.a.col == a.row)
				{
					ch.board.updateMCTS(u.w, u.t);
					rootNode.board.updateMCTS(u.w, u.t);
				}
			}
			
			playouts += ch.board.numTrials.get();
			
			if(winnerNode == null || ch.board.getScore() > winnerNode.board.getScore())
				winnerNode = ch;
		}
		
		stateUpdates.clear();
		
		return winnerNode;
	}
	
	public void moveQueen(Queen q, Arrow a)
	{
		rootNode.moveQueen(q, a);
	}
	
	public MCTSNode selectPromising(MCTSNode parent)
	{
		MCTSNode n = parent;
		while(n.getChildren().size() != 0)
			n = UCT.findBestNodeUCT(n);
		
		return n;
	}
	
	public void backPropagate(MCTSNode nodeExp, double result)
	{
		MCTSNode n = nodeExp;
		while(n != null)
		{
			n.board.numTrials.addAndGet(1);
			n.board.numWins.addAndGet(result);
			
			n = n.getParent();
		}
	}
	
	public double randomPlayout(MCTSNode n, TerritoryHeuristic h, DeadMonarchHeuristic dmh)
	{
		ChildFinder cf = new ChildFinder();
		MCTSNode temp = new MCTSNode(cf.cloneState(n.board));	//wish this could be n.state instead, but too late now
		GameState st = temp.board;
		
		double status = st.status();
		double dmhVal = dmh.calc(st);
		
		boolean isBlunder = dmh.isBlunder(st);
		
		// make sure we improve AND that the move isn't a blunder
		boolean approved = dmhVal > heuristic && !isBlunder;
		
		int turns = 0;
		while(status == 0.5 && turns < 11)
		{
			st.makeRandomPlay();
			status = st.checkStatus();
			turns++;
		}
		if(status == 0.5)
		{
			// evaluate the current gamestate
			double value = h.calc(st);
			if(approved) return 0.85 * value + 0.15; //Don't want 0 as a base value, so scale to [0.15, 1]
			if(isBlunder) return 0.6 * value;		// here we can have 0 as a base value in case of awful situations
			return value;							// otherwise it's nothing special and we let the value do its thing
		}
		// otherwise it's a win or loss, so just return that
		return status;
		
	}
	
	
}
