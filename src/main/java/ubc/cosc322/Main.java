package ubc.cosc322;

import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

public class Main {
	public static void main(String[] args) {
		AmazonsPlayer player = new AmazonsPlayer("team04", "WeW1llR0ckY0u");
		
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
