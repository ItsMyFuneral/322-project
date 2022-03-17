package ubc.cosc322.search;

import java.util.ArrayList;

import ubc.cosc322.board.*;
import ubc.cosc322.board.tiletypes.*;

// The idea for this heuristic came from a student in the previous year's class, Jordan Ribbink.
// He basically just suggested the idea of a heuristic based on the number of queens that are alive,
// compared to the opponent's. (It also hails from my original idea of a heuristic based on the number
// of moves available to each queen, which would also be fair, but feels unnecessary.)
// I implemented this from there.

// Yes, I made the name of the class a Queen heuristic.

public class KeepYourselfAliveHeuristic {
    GameState state;

    public double calc(GameState state) {
        this.state = state;

        return (double) getLiveQueens(true) - (double)getLiveQueens(false);
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

        //if queen has one or fewer moves
        boolean oneMove = frontier.size() <= 1;
        ArrayList<Queen> newFrontier = new ArrayList<>();

        for(Queen move: frontier) {
            ArrayList<Queen> moves = state.getMoves(move);
            moves.removeAll(visited);

            newFrontier.addAll(moves);
        }
        
        frontier = newFrontier;
        visited.addAll(frontier);
        

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