package ubc.cosc322.messages;

import ubc.cosc322.board.tiletypes.*;

public class MCTSState {
	public Queen q;			//queen
	public Arrow a;			//arrow
	public double t;		//trials
	public double w;		//wins
	
	public MCTSState(Queen q, Arrow a, double t, double w)
	{
		this.q = q;
		this.a = a;
		this.t = t;
		this.w = w;
	}
}
