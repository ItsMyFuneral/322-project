package ubc.cosc322.search;

import java.util.*;
import ubc.cosc322.board.tiletypes.*;

import com.google.common.util.concurrent.AtomicDouble;

public class GameState {
	// bunch of variables will end up going here
	public Tile[][] board = new Tile[10][10];
	
	public boolean ourTurn = true;
	
	public Queen[] friends;
	public Queen[] enemies;
	public ArrayList<Arrow> arrows = new ArrayList<>();
	public ArrayList<Queen> moves = new ArrayList<>();
	
	ChildFinder cf = new ChildFinder();
	
	// numbers for Monte-Carlo randomness
	// AtomicDoubles for multithreading
	public AtomicDouble numTrials = new AtomicDouble(0);
	public AtomicDouble numWins = new AtomicDouble(0);
	
	public GameState(boolean isWhite) {
		// only called when game starts
		
		// black starts. remove ! if white starts but pretty sure black starts.
		ourTurn = isWhite;
		
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
		updateMoves();
	}
	
	public GameState(Queen[] friends, Queen[] enemies, ArrayList<Arrow> arrows, boolean ourTurn)
	{
		this.friends = friends;
		this.enemies = enemies;
		this.arrows = arrows;
		this.ourTurn = ourTurn;
		
		this.updateBoard();
	}
	
	public void moveQueen(Queen q, Arrow a)
	{
		//adding the arrow is easy, we don't care what team it's for
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
	
	public void updateMCTS(double wins, double trials) {
		numWins.addAndGet(wins);
		numTrials.addAndGet(trials);
	}
	
	public void makeRandomPlay() {
		updateMoves();
		
		//pick random queen move
		int randM = (int) (Math.random() * this.moves.size());
		Queen m = this.moves.get((int)(Math.random() * this.moves.size()));
		//then pick random arrow from list of all of that queen's arrows
		ArrayList<Arrow> possArrows = getArrowMoves(m.row, m.col, m.prevRow, m.prevCol);
		Arrow a = possArrows.get((int)(Math.random() * possArrows.size()));
		m.friendly = ourTurn;
		
		//execute the move
		moveQueen(m, a);
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
	
	//check if we lose
	public boolean isEnemyGoal() {
		ArrayList<Queen> m = new ArrayList<>();
		for(Queen q : friends) {
			//add queen's moves
			m.addAll(getMoves(q));
		}
		return m.isEmpty();
	}
	
	public double getScore() {
		double trials = numTrials.get();
		if(trials != 0)
			return (double)numWins.get() / trials;
		else
			return trials;
	}
	
	public double checkStatus() {
		if(isFriendGoal()) return 1;
		if(isEnemyGoal()) return 0;
		return 0.5;
	}
	
	// get all of q's mossible moves
	public ArrayList<Queen> getMoves(Queen q)
	{
		ArrayList<Queen> mvs = new ArrayList<Queen>();
		
		for(int i = 1; q.col - i >= 0; i++)
        {
            if(board[q.row][q.col - i] == null) {
                Queen move = new Queen(q.row, q.col - i);
                move.prevCol = q.col;
                move.prevRow = q.row;
                move.friendly = q.friendly;
                mvs.add(move);
            }
            else
                break;
        }

        // Get all moves right
        for(int i = 1; q.col + i <= 9; i++)
        {
            if(board[q.row][q.col + i] == null)
            {
                Queen move = new Queen(q.row, q.col + i);
                move.prevCol = q.col;
                move.prevRow = q.row;
                move.friendly = q.friendly;
                mvs.add(move);
            }
            else
                break;
        }

        // Get all moves up
        for(int i = 1; q.row - i >= 0; i++)
        {
            if(board[q.row - i][q.col] == null)
            {
                Queen move = new Queen(q.row - i, q.col);
                move.prevCol = q.col;
                move.prevRow = q.row;
                move.friendly = q.friendly;
                mvs.add(move);
            }
            else
                break;
        }

        // Get all moves down
        for(int i = 1; q.row + i <= 9; i++)
        {
            if(board[q.row + i][q.col] == null)
            {
                Queen move = new Queen(q.row + i, q.col);
                move.prevCol = q.col;
                move.prevRow = q.row;
                move.friendly = q.friendly;
                mvs.add(move);
            }
            else
                break;
        }

        // Get all moves diag left/up
        for(int i = 1; q.col - i >= 0 && q.row - i >= 0; i++)
        {
            if(board[q.row - i][q.col - i] == null)
            {
                Queen move = new Queen(q.row - i, q.col - i);
                move.prevCol = q.col;
                move.prevRow = q.row;
                move.friendly = q.friendly;
                mvs.add(move);
            }
            else
                break;
        }

        // Get all moves diag left/down
        for(int i = 1; q.col - i >= 0 && q.row + i <= 9; i++)
        {
            if(board[q.row + i][q.col - i] == null)
            {
                Queen move = new Queen(q.row + i, q.col - i);
                move.prevCol = q.col;
                move.prevRow = q.row;
                move.friendly = q.friendly;
                mvs.add(move);
            }
            else
                break;
        }

        // Get all moves diag right/down
        for(int i = 1; q.col + i <= 9 && q.row + i <= 9; i++)
        {
            if(board[q.row + i][q.col + i] == null)
            {
                Queen move = new Queen(q.row + i, q.col + i);
                move.prevCol = q.col;
                move.prevRow = q.row;
                move.friendly = q.friendly;
                mvs.add(move);
            }
            else
                break;
        }

        // Get all moves diag right/up
        for(int i = 1; q.col + i <= 9 && q.row - i >= 0; i++)
        {
            if(board[q.row - i][q.col + i] == null)
            {
                Queen move = new Queen(q.row - i, q.col + i);
                move.prevCol = q.col;
                move.prevRow = q.row;
                move.friendly = q.friendly;
                mvs.add(move);
            }
            else
                break;
        }
		
		return mvs;
	}
	
	public ArrayList<Arrow> getArrowMoves(int row, int col, int oldRow, int oldCol)
	{
		ArrayList<Arrow> mvs = new ArrayList<Arrow>();
		
		// Get all moves left
        for(int i = 1; col - i >= 0; i++)
        {
            if(board[row][col - i] == null || (row == oldRow && col - i == oldCol))
                mvs.add(new Arrow(row, col - i));
            else
                break;
        }

        // Get all moves right
        for(int i = 1; col + i <= 9; i++)
        {
            if(board[row][col + i] == null || (row == oldRow && col + i == oldCol))
                mvs.add(new Arrow(row, col + i));
            else
                break;
        }

        // Get all moves down
        for(int i = 1; row - i >= 0; i++)
        {
            if(board[row - i][col] == null || (row - i == oldRow && col == oldCol))
                mvs.add(new Arrow(row - i, col));
            else
                break;
        }

        // Get all moves up
        for(int i = 1; row + i <= 9; i++)
        {
            if(board[row + i][col] == null || (row + i == oldRow && col == oldCol))
                mvs.add(new Arrow(row + i, col));
            else
                break;
        }

        // Get all moves diag NW
        for(int i = 1; col - i >= 0 && row - i >= 0; i++)
        {
            if(board[row - i][col - i] == null || (row - i == oldRow && col - i == oldCol))
                mvs.add(new Arrow(row - i, col - i));
            else
                break;
        }

        // Get all moves diag SW
        for(int i = 1; col - i >= 0 && row + i <= 9; i++)
        {
            if(board[row + i][col - i] == null || (row + i == oldRow && col - i == oldCol))
                mvs.add(new Arrow(row + i, col - i));
            else
                break;
        }

        // Get all moves diag SE
        for(int i = 1; col + i <= 9 && row + i <= 9; i++)
        {
            if(board[row + i][col + i] == null || (row + i == oldRow && col + i == oldCol))
                mvs.add(new Arrow(row + i, col + i));
            else
                break;
        }

        // Get all moves diag NE
        for(int i = 1; col + i <= 9 && row - i >= 0; i++)
        {
            if(board[row - i][col + i] == null || (row - i == oldRow && col + i == oldCol))
                mvs.add(new Arrow(row - i, col + i));
            else
                break;
        }
		
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
