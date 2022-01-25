package ubc.cosc322;

import java.util.*;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
 
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {
    	COSC322Test player = new COSC322Test(args[0], args[1]);
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
    /**
     * Any name and passwd 
     * @param userName
      * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    }
 


    @Override
    public void onLogin() {
        this.userName = gameClient.getUserName(); 
        if(this.gamegui != null) { 
        	this.gamegui.setRoomInformation(this.gameClient.getRoomList()); 
        }
        
        this.getGameClient().joinRoom(this.gameClient.getRoomList().get(1).getName());
    }

    @SuppressWarnings("unchecked")
	@Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
	
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
    	
    	System.out.println("got here");
    	
    	System.out.println(messageType);
    	
    	if(messageType.equals("cosc322.game-state.board"))
    	{
    		System.out.println("here?");
    		ArrayList<Integer> board = (ArrayList<Integer>) msgDetails.get("game-state");
    		System.out.println("what about here?");
    		this.gamegui.setGameState(board);
    		System.out.println("or here?");
    	}
    	else if(messageType.equals(""))		//whatever it is for GameMessage.GAME_ACTION_MOVE 
    	{
    		ArrayList<Integer> cur = (ArrayList<Integer>) msgDetails.get("queen-pos-cur");
    		ArrayList<Integer> mve = (ArrayList<Integer>) msgDetails.get("queen-pos-next");
    		ArrayList<Integer> arr = (ArrayList<Integer>) msgDetails.get("arrow-pos");
    		this.gamegui.updateGameState(cur,mve,arr);
    	}
    	
    	return true;   	
    }
    
    
    @Override
    public String userName() {
    	return userName;
    }

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub
		return this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);			
	}

 
}//end of class
