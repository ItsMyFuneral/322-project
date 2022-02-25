package ubc.cosc322.board.tiletypes;

import java.util.*;

public class Queen extends Tile {
	
	//prev might be useful
	public int prevRow;
	public int prevCol;
	public boolean friendly;		// ours or not
	
	public Queen(int row, int col)
	{
		super(row, col);
		this.row = row;
		this.col = col;
	}
	
	public Queen(int row, int col, boolean friend)
	{
		super(row, col);
		
		this.row = row;
		this.col = col;
		this.friendly = friend;
	}
	
	//basically just update row and col with new values. prev might be useful
	public void move(int row, int col) {
		this.prevRow = this.row;
		this.prevCol = this.col;
		this.row = row;
		this.col = col;
	}
	
	public ArrayList<Integer> oldPosition() {
		ArrayList<Integer> position = new ArrayList<>();
		position.add(this.prevRow + 1);
		position.add(this.prevCol + 1);
		
		return position;
	}
	
	public ArrayList<Integer> position() {
		ArrayList<Integer> position = new ArrayList<>();
		position.add(this.row + 1);
		position.add(this.col + 1);
		
		return position;
	}
}
