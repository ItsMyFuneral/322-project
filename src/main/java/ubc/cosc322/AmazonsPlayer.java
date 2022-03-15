package ubc.cosc322;

import java.util.*;

import ubc.cosc322.board.*;
import ubc.cosc322.search.*;

import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.Amazon.GameBoard;
import ygraph.ai.smartfox.games.amazons.AmazonsBoard;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

import ubc.cosc322.board.tiletypes.*;
import ubc.cosc322.board.*;
import ubc.cosc322.messages.*;
import ubc.cosc322.search.*;

/**
 * For testing and demo purposes only. An GUI Amazon client for human players 
 * @author yong.gao@ubc.ca
 */
public class AmazonsPlayer extends GamePlayer {
	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;
	
	private String userName = null;
	private String password = null;
	
	//basic info: player colour and turn counter
	private boolean isWhite = true;
	private int turn = 0;
	
	public boolean running = true;
	
	public MonteCarloTreeSearch mcts;
	
	AmazonsBoard gameBoard;
	
	public boolean isBot = false;
	
	public AmazonsPlayer(String un, String pw)
	{
		this.userName = un;
		this.password = pw;
		this.gamegui = new BaseGameGUI(this);
	}
	
	public void handleOwnMove()
	{
		MCTSNode best = mcts.findNextMove();
		Queen bestQ = best.getQueen();
		Arrow bestA = best.getArrow();
		
		if(bestQ == null) {	//i.e. if game is over
			if(best.board.checkStatus() == 1) System.out.println("GET THOSE FREAKING W'S BOYS");
			else System.out.println("Mission failed. We'll get 'em next time.");
			return;
		}
		
		turn++;
		
		
		ArrayList<Integer> qpC = bestQ.oldPosition();
		ArrayList<Integer> qpN = bestQ.position();
		ArrayList<Integer> arP = bestA.position();
		
		gameClient.sendMoveMessage(qpC, qpN, arP);
		this.gamegui.updateGameState(qpC,qpN,arP);
		
		bestQ.friendly = true;
		mcts.moveQueen(bestQ, bestA);
	}
	
	public void handleOpponentMove(Map<String, Object> msgDetails)
	{
		//read info. All these unchecked casts are safe because we know what the objects are
		ArrayList<Integer> queenCur = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
		ArrayList<Integer> queenNxt = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT);
		ArrayList<Integer> arrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
		
		// create enemy move as a Queen object
		Queen enemy = new Queen(queenNxt.get(0) - 1, queenNxt.get(1) - 1, false);
		enemy.prevRow = queenCur.get(0) - 1;
		enemy.prevCol = queenCur.get(1) - 1;
		
		turn++;
		
		// and make the arrow
		Arrow a = new Arrow(arrowPos.get(0) - 1, arrowPos.get(1) - 1);
		
		//then update the gamestate
		this.gamegui.updateGameState(queenCur, queenNxt, arrowPos);
		
		if(mcts != null) {
			mcts.moveQueen(enemy, a);
			
			handleOwnMove();
		}
	}
	
	public void reset() {
		this.mcts = null;
	}
	
	public void initBot() {
		isBot = true;
	}
	
	public void initPlayer() {
		this.Go();
	}
	
	@SuppressWarnings("unchecked")
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails)
	{
		System.out.println("got to handle a message");
		System.out.println(messageType);
		switch(messageType)
		{
			case GameMessage.GAME_ACTION_START:
				isWhite = msgDetails.get("player-white").equals(this.userName);
				GameState board = new GameState(isWhite);
				mcts = new MonteCarloTreeSearch(new MCTSNode(board));
				mcts.isWhite = isWhite;
				
				// handle first move here somewhere
				if(!isWhite) handleOwnMove();
				
				break;
			case GameMessage.GAME_ACTION_MOVE:
				// handle opponent move
				handleOpponentMove(msgDetails);
				
				break;
			case GameMessage.GAME_STATE_BOARD:
				ArrayList<Integer> arr = (ArrayList<Integer>) msgDetails.get("game-state");
				this.gamegui.setGameState(arr);
				break;
			default:
				break;
		}
		return true;
	}
	
	// copied from COSC322Test.java, might want to be updated
	public void onLogin() {
        this.userName = gameClient.getUserName(); 
        if(this.gamegui != null) { 
        	this.gamegui.setRoomInformation(this.gameClient.getRoomList()); 
        	this.gameClient.joinRoom(this.gameClient.getRoomList().get(0).getName());
        }
    }
	
	@Override
    public String userName() {
    	return userName;
    }

    @Override
    public GameClient getGameClient() {
        return this.gameClient;
    }

    @Override
    public BaseGameGUI getGameGUI() {
        return this.gamegui;
    }

    @Override
    public void connect() {
        gameClient = new GameClient(userName, password, this);
    }
}
