package ubc.cosc322.board;

import java.util.*;
import ubc.cosc322.search.*;
import ubc.cosc322.board.tiletypes.*;

public class GameState {
	// bunch of variables will end up going here
	public Tile[][] board = new Tile[10][10];
	
	public boolean ourTurn = true;
	
	public Queen[] friends;
	public Queen[] enemies;
	public ArrayList<Arrow> arrows = new ArrayList<>();
	public ArrayList<Queen> moves = new ArrayList<>();
	
	ChildFinder cf = new ChildFinder();
	
	//numbers for Monte-Carlo randomness
	// figure out multithreading stuff later
	public int numTrials;
	public int numWins;
	
	public GameState(boolean isWhite) {
		// only called when game starts
		
		// black starts. remove ! if white starts but pretty sure black starts.
		ourTurn = !isWhite;
		
		if(!isWhite) {
			friends = new Queen[] { new Queen(6,0,true), new Queen(6,9,true), new Queen(9,3,true), new Queen(9,6,true) };
			enemies = new Queen[] { new Queen(0,3,false), new Queen(0,6,false), new Queen(3,0,false), new Queen(3,9,false) };
		}
		else
		{
			friends = new Queen[] { new Queen(0,3,true), new Queen(0,6,true), new Queen(3,0,true), new Queen(3,9,true) };
			enemies = new Queen[] { new Queen(6,0,false), new Queen(6,9,false), new Queen(9,3,false), new Queen(9,6,false) };
		}
		
		updateBoard();
	}
	
	public GameState(Queen[] friends, Queen[] enemies, ArrayList<Arrow> arrows, boolean ourTurn)
	{
		this.friends = friends;
		this.enemies = enemies;
		this.arrows = arrows;
		this.ourTurn = ourTurn;
		updateBoard();
	}
	
	public void moveQueen(Queen q, Arrow a)
	{
		addArrow(a);
		
		if(q.friendly)
		{
			for(Queen qn : friends)
			{
				if(qn.row == q.prevRow && qn.col == q.prevCol)	// move the queen that 'q' indicates
					qn.move(q.row, q.col);
			}
		}
		else
		{
			for(Queen qn : enemies)
			{
				if(qn.row == q.prevRow && qn.col == q.prevCol)	// move the queen that 'q' indicates
					qn.move(q.row, q.col);
			}
		}
		
		updateBoard();
		ourTurn = !ourTurn;
	}
	
	public void randomPlay() {
		// I dunno what this is gonna do tbh
		// I guess something like:
		
		// Queen mv = moves.get((int) (Math.random() * moves.size()));
		// ArrayList<Arrow> arrows = getArrowMoves(mv.row, mv.col, move.prevRow, mv.prevCol);
		// Arrow a = arrow.get((int) (Math.random() * arrows.size()));
		// mv.friendly = ourTurn;
		
		// moveQueen(mv, a)
	}
	
	public void updateBoard() {
		
		// reset the board
		for(int i = 0; i < board.length; i++)
		{
			Arrays.fill(board[i], null);
		}
		
		for(Queen enemy : enemies) {
			if(enemy != null) board[enemy.row][enemy.col] = enemy;
		}
		
		for(Queen friend : friends) {
			if(friend != null) board[friend.row][friend.col] = friend;
		}
		
		for(Arrow a : arrows) {
			if(a != null) board[a.row][a.col] = a;
		}
	}
	
	public void updateMoves() {
		// empty list of moves
		moves.clear();
		
		//init list of queens who can currently move
		Queen[] queens;
		if(ourTurn) queens = friends;
		else queens = enemies;
		
		//add all of each queen's moves to moves list
		for(Queen q : queens) moves.addAll(getMoves(q));
	}
	
	//check if we win
	public boolean isGoal() { return isFriendGoal(); }
	
	public boolean isFriendGoal() {
		ArrayList<Queen> m = new ArrayList<>();
		for(Queen q : enemies) {
			//add queen's moves
			m.addAll(getMoves(q));
		}
		return m.isEmpty();
	}
	
	public boolean isEnemyGoal() {
		ArrayList<Queen> m = new ArrayList<>();
		for(Queen q : friends) {
			//add queen's moves
			m.addAll(getMoves(q));
		}
		return m.isEmpty();
	}
	
	public double getScore() {
		if(numTrials == 0) return 0;
		else return numWins / numTrials;
	}
	
	// get all of q's mossible moves
	public ArrayList<Queen> getMoves(Queen q)
	{
		ArrayList<Queen> mvs = new ArrayList<Queen>();
		
		/* add all moves:
		 * up
		 * down
		 * left
		 * right
		 * diag NW
		 * diag NE
		 * diag SW
		 * diag SE
		 * 
		 */
		
		return mvs;
	}
	
	public ArrayList<Arrow> getArrowMoves(int row, int col, int oldRow, int oldCol)
	{
		ArrayList<Arrow> mvs = new ArrayList<Arrow>();
		
		/* add all moves:
		 * up
		 * down
		 * left
		 * right
		 * diag NW
		 * diag NE
		 * diag SW
		 * diag SE
		 * 
		 */
		
		return mvs;
	}
	
	public double status() {
		if(isFriendGoal()) return 1;
		if(isEnemyGoal()) return 0;
		return 0.5;
	}
	
	public void addArrow(Arrow a)
	{
		arrows.add(a);
		updateBoard();
	}
	
}
