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
	
	public boolean isBot = false;
	
	public AmazonsPlayer(String un, String pw)
	{
		this.userName = un;
		this.password = pw;
	}
	
	@SuppressWarnings("unchecked")
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails)
	{
		switch(messageType)
		{
			case GameMessage.GAME_ACTION_START:
				isWhite = msgDetails.get("player-white").equals(this.userName);
				GameState board = new GameState(isWhite);
				mcts = new MonteCarloTreeSearch(new MCTSNode(board));
				mcts.isWhite = isWhite;
				
				// handle first move here somewhere
				break;
			case GameMessage.GAME_ACTION_MOVE:
				// handle opponent move
				break;
			case GameMessage.GAME_STATE_BOARD:
				ArrayList<Integer> arr = (ArrayList<Integer>) msgDetails.get("game-state");
				this.gamegui.setGameState(arr);
				break;
		}
		return true;
	}
	
	// copied from COSC322Test.java, might want to be updated
	public void onLogin() {
        this.userName = gameClient.getUserName(); 
        if(this.gamegui != null) { 
        	this.gamegui.setRoomInformation(this.gameClient.getRoomList()); 
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
        //return this.gamegui;
        return null;
    }

    @Override
    public void connect() {
        gameClient = new GameClient(userName, password, this);
    }
}
