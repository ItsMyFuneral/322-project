package ubc.cosc322;

import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

public class Main {
	public static void main(String[] args) {
		AmazonsPlayer player = new AmazonsPlayer("test1", "WeW1llR0ckY0u");
		
		System.out.println("got here");
		
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
		
		while(player.running) {}
	}
}
