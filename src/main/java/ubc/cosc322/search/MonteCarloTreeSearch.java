package ubc.cosc322.search;

import java.util.*;
import java.util.concurrent.*;

import ubc.cosc322.board.tiletypes.*;

// UCT: Upper Confidence bound applied to Trees. Relevant for MCTS somehow idk, I just read that
//      it improves the search somehow. Blank class as placeholder
class UCT {
	
}

// for multithreading later
class SimulationWorker extends Thread {
	MCTSNode rootNode;
	long endTime;
	MonteCarloTreeSearch mcts;
	
	public SimulationWorker(MCTSNode root, long endTime, MonteCarloTreeSearch mcts)
	{
		this.rootNode = root;
		this.endTime = endTime;
		this.mcts = mcts;
	}
	public void run() {
		while(System.nanoTime() < endTime) {
			// do something here later
		}
	}
}

public class MonteCarloTreeSearch {
	public MCTSNode rootNode;
	public boolean isWhite;
	public int playouts = 0;
	//something about heuristic in here? idk
	
	public MonteCarloTreeSearch(MCTSNode root) {
		this.rootNode = root;
	}
	
	public void performSearch() {
		// do something in here, idk
		
		rootNode.setChildren();
	}
	
	public double randomPlayout(MCTSNode exp) {
		return 0;	//temp
	}
	
	public MCTSNode findNextMove() {
		if(rootNode.board.status() != 0.5) return rootNode;
		
		rootNode.board.numTrials = 0;
		rootNode.board.numWins = 0;
		performSearch();
		
		MCTSNode winnerNode = null;
		for(MCTSNode ch : rootNode.getChildren())
		{
			Queen q = ch.getQueen();
			Arrow a = ch.getArrow();
			
			playouts += ch.board.numTrials;
			
			if(winnerNode == null || ch.board.getScore() > winnerNode.board.getScore())
				winnerNode = ch;
		}
		
		return winnerNode;
	}
}
