package ubc.cosc322.search;

import java.util.ArrayList;

import ubc.cosc322.board.*;
import ubc.cosc322.board.tiletypes.*;
import ubc.cosc322.search.*;

// The idea of a "Dead monarch heuristic" was suggested by a previous student in the class, Jordan Ribbink.
// He gave me a general description of the idea and its uses, and I implemented it from there.
public class DeadMonarchHeuristic {
    GameState state;

    public double calc(GameState state) {
        this.state = state;

        return getLiveQueens(true) - (double)getLiveQueens(false);
    }

    private int getLiveQueens(boolean friendly) {
        Queen[] queens;
        if(friendly) queens = state.friends;
        else queens = state.enemies;

        int count = 4;
        for(Queen q: queens) {
            if(queenWillDie(q)) {
                count--;
            }
        }

        return count;
    }

    private boolean queenWillDie(Queen q) {
        ArrayList<Queen> visited = new ArrayList<>();
        ArrayList<Queen> frontier = new ArrayList<>();

        frontier.addAll(state.getMoves(q));

        boolean oneMove = frontier.size() <= 1;

        // Could be looped starting here
        ArrayList<Queen> newFrontier = new ArrayList<>();

        for(Queen move: frontier) {
            ArrayList<Queen> moves = state.getMoves(move);
            moves.removeAll(visited);

            newFrontier.addAll(moves);
        }
        
        frontier = newFrontier;
        visited.addAll(frontier);
        //loop woudl end here
        

        return frontier.isEmpty() && oneMove;
    }

    public boolean isBlunder(GameState state) {
        MCTSNode n = new MCTSNode(state);
        n.setChildren();

        double sourceHeuristicValue = calc(state);

        for(MCTSNode child: n.getChildren()) {
        	// if there exists an opponent's move that would bring us to fewer live queens than opponent's number
            if(calc(child.board) < sourceHeuristicValue) return true;
        }
        // else we're fine
        return false;
    }
}