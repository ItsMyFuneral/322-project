package ubc.cosc322.search;

import java.util.*;

import ubc.cosc322.board.GameState;
import ubc.cosc322.board.tiletypes.*;

class ShortestMove {
	//class to hold the friendly and enemy shortest moves within the 2d array
	int f = Integer.MAX_VALUE;
	int e = Integer.MAX_VALUE;
}

public class TerritoryHeuristic {
	GameState state;
	ShortestMove dists[][] = new ShortestMove[10][10];
	
	public double calc(GameState state) {
		this.state = state;
		
		//initialize dists
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				dists[i][j] = new ShortestMove();
			}
		}
		
		setDistances(true);
		setDistances(false);
		
		//set up variables to do an average of the evaluations
		double sum = 0;
		int count = 0;
		
		//average across entire board
		for(int k = 0; k < 10; k++)
			for(int l = 0; l < 10; l++)
				if(dists[k][l].f < Integer.MAX_VALUE || dists[k][l].e < Integer.MAX_VALUE)
				{
					sum += localEval(dists[k][l]);
					count++;
				}
		
		return sum / count;
		
	}
	
	public double localEval(ShortestMove dist) {
		return 0.5 + Math.pow(0.5, dist.f) - Math.pow(0.5, dist.e);
	}
	
	public void setDistances(boolean fr) {
		Queen[] queens;
		if(fr) queens = state.friends;
		else queens = state.enemies;
		
		ArrayList<Queen> queue = new ArrayList<>();
		int numMoves = 1;
		
		queue.addAll(new ArrayList<>(Arrays.asList(queens)));
		
		while(queue.size() > 0) {
			ArrayList<Queen> newQ = new ArrayList<>();
			for(Queen q : queue) {
				ArrayList<Queen> moves = state.getMoves(q);
				Iterator<Queen> iter = moves.iterator();
				
				while(iter.hasNext()) {
					Queen mv = iter.next();
					
					//if:
					if(dists[mv.row][mv.col].f < numMoves ||			   // player can reach square in under numMoves, or
							(dists[mv.row][mv.col].f == numMoves && fr) || // player can reach square in exactly
																		   // numMoves, and it's their turn, or
							
					   dists[mv.row][mv.col].e < numMoves ||			   // pone can reach square in under numMoves, or
					   		(dists[mv.row][mv.col].e == numMoves && !fr))  // pone can reach square in exactly numMoves,
																		   // it's their turn
					{
						// with all of dists[] set up as max integer value, this sets all dists.f and dists.e values
						// to 1 if the given player can reach them within numMoves turns
						iter.remove();	//removes most recent item returned by next()
					}
					else
					{
						if(fr) dists[mv.row][mv.col].f = numMoves;
						else dists[mv.row][mv.col].e = numMoves;
					}
				}
			}
			numMoves++;
			queue = newQ;
		}
	}
}
