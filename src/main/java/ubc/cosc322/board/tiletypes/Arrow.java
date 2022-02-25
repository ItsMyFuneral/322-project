package ubc.cosc322.board.tiletypes;

import java.util.*;

public class Arrow extends Tile {
	public Arrow(int row, int col) {
		super(row, col);
	}
	
	public ArrayList<Integer> position() {
		ArrayList<Integer> position = new ArrayList<>();
		position.add(this.row + 1);
		position.add(this.col + 1);
		
		return position;
	}
}
