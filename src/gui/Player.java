package gui;

/**
 * The player class
 * 
 * @author person
 * @version 12/8/16
 */
public class Player {
	// turn
	private boolean turn;
	private boolean firstTurn = true;
	private int score;
	private String color;
	private int[] pieces = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	private String playerID;
	private boolean isAI;
	private boolean passed = false;

	/**
	 * Returns the score
	 * 
	 * @return An int of the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Sets the score
	 * 
	 * @param score
	 *            The int that will be the score
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Gets the string for the color
	 * 
	 * @return The string for the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the string for the color
	 * 
	 * @param color
	 *            The string for the color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Constructor for the player
	 * 
	 * @param playerID
	 *            The number of the player
	 * @param isAI
	 *            Boolean for if this player is an ai or not
	 */
	Player(int playerID, boolean isAI) {
		this.isAI = isAI;
		if (!isAI) {
			this.playerID = "p" + playerID;
		} else {
			this.playerID = "pa" + playerID;
		}

	}

	/**
	 * Returns whether this player is ai or not
	 * 
	 * @return True if ai, false if is not
	 */
	public boolean isAI() {
		return isAI;
	}

	/**
	 * Gets if it is this players turn
	 * 
	 * @return True if is, false if isn't
	 */
	public boolean isTurn() {
		return turn;
	}

	/**
	 * Sets if it is this players turn
	 * 
	 * @param turn
	 *            Boolean
	 */
	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	/**
	 * Gets the ID for this player
	 * 
	 * @return The string id
	 */
	public String getPlayerID() {
		return playerID;
	}

	/**
	 * Gets if it is the first turn for this player
	 * 
	 * @return True if it is, false if it isn't
	 */
	public boolean isFirstTurn() {
		return firstTurn;
	}

	/**
	 * Sets if it is the first turn for this player
	 * 
	 * @param firstTurn
	 *            True if it is, false if it isn't
	 */
	public void setFirstTurn(boolean firstTurn) {
		this.firstTurn = firstTurn;
	}

	/**
	 * Gets the array of pieces this player has
	 * 
	 * @return The piece int array
	 */
	public int[] getPieces() {
		return pieces;
	}

	/**
	 * Resets the player to have only 1 of each piece
	 */
	public void resetPieces() {
		for (int i = 0; i < pieces.length; i++) {
			pieces[i] = 1;
		}
	}

	/**
	 * Gets whether this player has passed or not
	 * 
	 * @return True if they have, false if they haven't
	 */
	public boolean getPassed() {
		return passed;
	}

	/**
	 * Sets whether this player has passed or not
	 * 
	 * @param passed
	 *            True if they have, false if they haven't
	 */
	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	/**
	 * Checks to see if every piece has been used
	 * 
	 * @return True if they ahve, false if they haven't
	 */
	public boolean checkIfOutOfPieces() {
		boolean output = true;
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] > 0) {
				output = false;
			}
		}

		return output;
	}

	/**
	 * Doubles the amount of pieces the player currently has
	 */
	public void doubleAmountOfPieces() {
		for (int i = 0; i < pieces.length; i++) {
			pieces[i] *= 2;
		}
	}
}
