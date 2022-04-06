package ubc.cosc322.search;

import java.util.ArrayList;

import ubc.cosc322.board.tiletypes.*;

// This heuristic is essentially a simplified idea I had earlier this semester.
// The original idea consisted of a heuristic whose value would be based on the number of moves
// available to each friendly queen, minus the same number for enemy queens.
// I decided that was too complicated (and might also over-favor the center of the board),
// and chose to implement an algorithm based on the number of queens each player has "alive,"
// i.e. with at least one legal move.
// I implemented this from there.

// Yes, I made the name of the class a Queen reference. Oddly fitting, too.

public class KeepYourselfAliveHeuristic {
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

        //if queen has one or fewer moves
        boolean oneMove = frontier.size() <= 1;
        ArrayList<Queen> newQ = new ArrayList<>();

        for(Queen move: frontier) {
            ArrayList<Queen> moves = state.getMoves(move);
            moves.removeAll(visited);

            newQ.addAll(moves);
        }
        
        frontier = newQ;
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