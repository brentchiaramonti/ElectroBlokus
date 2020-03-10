package gui;

/**
 * Subclass for player, has a differnt constructor which sets the player to be
 * considered an ai
 * 
 * @author Jakub
 * @version 12/8/16
 */
public class ComputerPlayer extends Player {

	ComputerPlayer(int playerID) {
		super(playerID, true);
	}

}
