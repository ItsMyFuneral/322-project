package ubc.cosc322.search;

import java.util.*;

import ubc.cosc322.board.*;
import ubc.cosc322.board.tiletypes.*;

public class ChildFinder {
	public ChildFinder() {
		// nothing.
	}
	
	public ArrayList<MCTSNode> getChildren(GameState state, MCTSNode parent) {
		Queen[] queens;
		ArrayList<MCTSNode> children = new ArrayList<MCTSNode>();
		boolean ourTurn = state.ourTurn;
		
		if(!ourTurn) queens = state.enemies;
		else queens = state.friends;
		
		for(int i = 0; i < queens.length; i++)
		{
			for(Queen move : state.getMoves(queens[i])) //for each possible move, test the board with it
			{
				GameState tempState = cloneState(state);
				tempState.ourTurn = !ourTurn;
				
				if(ourTurn) tempState.friends[i] = move;
				else		tempState.enemies[i] = move;
				
				tempState.updateBoard();
				
				//get list of all possible arrow shots for the move being tested
				ArrayList<Arrow> possArrows = tempState.getArrowMoves(move.row, move.col, move.prevRow, move.prevCol);
				for(Arrow a : possArrows)
				{
					//that's right, baby, we're going three layers deep!
					GameState newState = cloneState(tempState);
					newState.addArrow(a);
					
					// make a new search node that contains the new board state after
					// playing 'move' and shooting arrow 'a'
					MCTSNode node = new MCTSNode(newState, move, a);
					node.parent = parent;	// set its parent properly
					children.add(node);	//then add to list of children
				}
			}
		}
		
		return children;
	}
	
	public GameState cloneState(GameState state)
	{
		//return a new GameState identical to the given GameState, but with all references to queens/arrows changed.
		//Lets us experiment with the cloned state without messing up the real one
		Queen[] newFriends = new Queen[4];
		for(int i = 0; i < 4; i++)
		{
			Queen q = state.friends[i];
			newFriends[i] = new Queen(q.row, q.col, q.friendly);
			newFriends[i].prevCol = q.prevCol;
			newFriends[i].prevRow = q.prevRow;
		}
		
		Queen[] newEnemies = new Queen[4];
		for(int i = 0; i < 4; i++)
		{
			Queen q = state.enemies[i];
			newEnemies[i] = new Queen(q.row, q.col, q.friendly);
			newEnemies[i].prevCol = q.prevCol;
			newEnemies[i].prevRow = q.prevRow;
		}
		
		ArrayList<Arrow> arrowsCopy = new ArrayList<Arrow>();
		for(Arrow a : state.arrows)
			arrowsCopy.add(a);
		
		
		return new GameState(newFriends, newEnemies, arrowsCopy, state.ourTurn);
	}
}
