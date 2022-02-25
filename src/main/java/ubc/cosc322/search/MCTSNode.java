package ubc.cosc322.search;

import java.util.*;
import ubc.cosc322.board.*;
import ubc.cosc322.board.tiletypes.*;

public class MCTSNode {
	private Queen queen;
	private Arrow arrow;
	private ArrayList<MCTSNode> children = new ArrayList<MCTSNode>();
	public GameState board;
	
	private ChildFinder childF = new ChildFinder();
	
	public MCTSNode parent;
	
	public MCTSNode(GameState board, Queen q, Arrow a)
	{
		this.board = board;
		this.queen = q;
		this.arrow = a;
	}
	
	public MCTSNode(GameState board) {
		this.board = board;
	}
	
	public void moveQueen(Queen q, Arrow a)
	{
		board.moveQueen(q, a);
		children.clear();
	}
	
	public void setChildren()
	{
		children = childF.getChildren(board, this);
	}
	
	public ArrayList<MCTSNode> getChildren() {
		return children;
	}
	
	public void setParent(MCTSNode p) {
		this.parent = p;
	}
	
	public MCTSNode getParent() {
		return parent;
	}
	
	public Queen getQueen() {
		return queen;
	}
	
	public Arrow getArrow() {
		return arrow;
	}
}
