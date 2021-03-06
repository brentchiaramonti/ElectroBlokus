package gui;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * The Board class, has a 20 by 20 array of strings to hold information about
 * the board
 * 
 * @author rgullett
 * @version 12/8/16
 */
public class Board {

	private static final int ROWS = 20;
	private static final int COLUMNS = 20;

	private String[][] board = new String[ROWS][COLUMNS];

	/**
	 * Initialize all board pieces to p
	 */
	Board() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				board[i][j] = "p";
			}
		}
	}

	/**
	 * Returns the string array for the board
	 * 
	 * @return The string array for the board
	 */
	public String[][] getBoard() {
		return board;
	}

	/**
	 * Takes in a piece, a player, and the x, y locations and attempts to add
	 * the piece to the board
	 * 
	 * @param selectedPiece
	 *            The piece that will be added
	 * @param player
	 *            The player that will have the piece placed for
	 * @param buttonX
	 *            The X location where the piece will be placed
	 * @param buttonY
	 *            The Y location where the piece will be placed
	 * @return A string, it will be empty if placed correctly, it will have an
	 *         error message if it couldn't be placed
	 */
	public String placePiece(Piece selectedPiece, Player player, int buttonX, int buttonY) {
		String errorText;
		if (selectedPiece != null) {
			errorText = " ";
			int i = buttonY;
			int j = buttonX;

			int xL = selectedPiece.findLeftMost();
			int xR = selectedPiece.findRightMost();
			int yT = selectedPiece.findTopMost();
			int yB = selectedPiece.findBottomMost();

			int refY = selectedPiece.getPointOfReference()[0];
			int refX = selectedPiece.getPointOfReference()[1];

			if (player.isFirstTurn()) {
				if (i == 0 && j == 0) {
					if (checkInBounds(i, j, yT, xL, refY, refX, selectedPiece) && checkCorner1(yT, xL, selectedPiece)
							&& checkOpen(i, j, yT, xL, refY, refX, selectedPiece)) {
						placeCorner1(yT, xL, selectedPiece, player);
						player.setFirstTurn(false);
					} else {
						errorText = "First piece must be on corner & in bounds";
					}
				} else if (i == 0 && j == 19) {
					if (checkInBounds(i, j, yT, xR, refY, refX, selectedPiece) && checkCorner2(yT, xR, selectedPiece)
							&& checkOpen(i, j, yT, xR, refY, refX, selectedPiece)) {
						placeCorner2(yT, xR, selectedPiece, player);
						player.setFirstTurn(false);
					} else {
						errorText = "First piece must be on corner & in bounds";
					}
				} else if (i == 19 && j == 0) {
					if (checkInBounds(i, j, yB, xL, refY, refX, selectedPiece) && checkCorner3(yB, xL, selectedPiece)
							&& checkOpen(i, j, yB, xL, refY, refX, selectedPiece)) {
						placeCorner3(yB, xL, selectedPiece, player);
						player.setFirstTurn(false);
					} else {
						errorText = "First piece must be on corner & in bounds";
					}
				} else if (i == 19 && j == 19) {
					if (checkInBounds(i, j, yB, xR, refY, refX, selectedPiece) && checkCorner4(yB, xR, selectedPiece)
							&& checkOpen(i, j, yB, xR, refY, refX, selectedPiece)) {
						placeCorner4(yB, xR, selectedPiece, player);
						player.setFirstTurn(false);
					} else {
						errorText = "First piece must be on corner & in bounds";
					}
				}
				// first piece not on corners
				else {
					errorText = "First piece must be placed on corner";
				}
			}
			// not the first turn
			else {
				errorText = " ";
				// check that spot chosen is corner to corner with their piece
				// and also not adjacent to any of their pieces
				if (checkIfValidLocation(i, j, selectedPiece, player)) {
					board[i][j] = player.getPlayerID();
					for (int k = yT; k < selectedPiece.getPieceType().length; k++) {
						for (int m = xL; m < selectedPiece.getPieceType()[k].length; m++) {
							if (selectedPiece.getPieceType()[k][m] == 1) {
								board[i + (k - refY)][j + (m - refX)] = player.getPlayerID();

							}
						}
					}
					MainBoardController.setPlacedPiece(true);
				} // check corner to corner and adjacency
				else {
					errorText = "Oops, not a valid placement";
				}
			}
		}
		// no selected piece
		else {
			errorText = "Choose a piece!";
		}

		return errorText;
	}

	/**
	 * Checks if the place is open
	 * 
	 * @param i
	 * @param j
	 * @param yT
	 * @param xL
	 * @param refY
	 * @param refX
	 * @param selectedPiece
	 * @return True if it is, false if it isn't
	 */
	private boolean checkOpen(int i, int j, int yT, int xL, int refY, int refX, Piece selectedPiece) {
		boolean open = true;
		// for the entire piece
		for (int k = yT; k < selectedPiece.getPieceType().length; k++) {
			for (int m = xL; m < selectedPiece.getPieceType()[k].length; m++) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					// inBounds
					if (!board[i + (k - refY)][j + (m - refX)].equals("p")) {
						open = false;
					}
				}
			}
		}
		return open;
	}

	/**
	 * Checks if the current location is valid
	 * 
	 * @param i
	 * @param j
	 * @param selectedPiece
	 * @param player
	 * @return True if it is, false if it isn't
	 */
	public boolean checkIfValidLocation(int i, int j, Piece selectedPiece, Player player) {
		// gets information for checking
		int xL = selectedPiece.findLeftMost();
		int xR = selectedPiece.findRightMost();
		int yT = selectedPiece.findTopMost();
		int yB = selectedPiece.findBottomMost();
		int refY = selectedPiece.getPointOfReference()[0];
		int refX = selectedPiece.getPointOfReference()[1];

		return checkCornerToCorner(i, j, player) && !checkAdjacent(i, j, yT, xL, refY, refX, selectedPiece, player)
				&& checkInBounds(i, j, yT, xL, refY, refX, selectedPiece)
				&& checkOpen(i, j, yT, xL, refY, refX, selectedPiece);
	}

	/**
	 * Check that the placement on the piece would be in bounds
	 * 
	 * @param i
	 * @param j
	 * @param yT
	 * @param xL
	 * @param refY
	 * @param refX
	 * @return True if it is, false if it isn't
	 */
	private boolean checkInBounds(int i, int j, int yT, int xL, int refY, int refX, Piece selectedPiece) {
		boolean inBounds = true;
		// for the entire piece
		for (int k = yT; k < selectedPiece.getPieceType().length; k++) {
			for (int m = xL; m < selectedPiece.getPieceType()[k].length; m++) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					// inBounds
					if ((i + (k - refY)) > 19 || (i + (k - refY)) < 0 || (j + (m - refX)) > 19
							|| (j + (m - refX)) < 0) {
						inBounds = false;
					}
				}
			}
		}
		return inBounds;
	}

	/**
	 * Check that there are no pieces of the same color adjacent to the current
	 * piece
	 * 
	 * @param i
	 *            row on button array
	 * @param j
	 *            column on button array
	 * @param yT
	 *            top most value in selected piece array
	 * @param xL
	 *            left most value in selected piece array
	 * @param refY
	 *            reference pt y value in selected piece array
	 * @param refX
	 *            reference pt x value in selected piece array
	 * @return false if there are no adjacent pieces of the same color, true
	 *         otherwise
	 */
	private boolean checkAdjacent(int i, int j, int yT, int xL, int refY, int refX, Piece selectedPiece,
			Player player) {
		boolean adjacent = false;
		// for the entire piece
		for (int k = yT; k < selectedPiece.getPieceType().length; k++) {
			for (int m = xL; m < selectedPiece.getPieceType()[k].length; m++) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					// checkAdjacent single
					if (checkAdjacentSingle(i + (k - refY), j + (m - refX), player)) {
						adjacent = true;
					}
				}
			}
		}
		return adjacent;
	}

	/**
	 * Check that the single part of piece would not be adjacent to any other
	 * pieces
	 * 
	 * @param i
	 *            row number on button array
	 * @param j
	 *            column number on button array
	 * @return false if there are no adjacent red pieces, true otherwise
	 */
	private boolean checkAdjacentSingle(int i, int j, Player player) {
		boolean adjacent = false;
		if (i > -1 && i < 20 && j > -1 && j < 20) {
			if (((i + 1) < 20) && board[i + 1][j].equals(player.getPlayerID())) {
				adjacent = true;
			}
			if (((i - 1) > -1) && board[i - 1][j].equals(player.getPlayerID())) {
				adjacent = true;
			}
			if (((j - 1) > -1) && board[i][j - 1].equals(player.getPlayerID())) {
				adjacent = true;
			}

			if (((j + 1) < 20) && board[i][j + 1].equals(player.getPlayerID())) {
				adjacent = true;
			}
		}

		return adjacent;
	}

	/**
	 * Check that piece is placed corner to corner with another piece. Will need
	 * to be changed when more players are included.
	 * 
	 * @param i
	 *            row number in the button array
	 * @param j
	 *            column number in the button array
	 * @return true if it is corner to corner with at least one piece of the
	 *         same color
	 */
	private boolean checkCornerToCorner(int i, int j, Player player) {
		if (((i + 1) < 20 && (j + 1) < 20) && board[i + 1][j + 1].equals(player.getPlayerID())) {
			return true;
		} else if (((i + 1) < 20 && (j - 1) > -1) && board[i + 1][j - 1].equals(player.getPlayerID())) {
			return true;
		} else if (((i - 1) > -1 && (j + 1) < 20) && board[i - 1][j + 1].equals(player.getPlayerID())) {
			return true;
		} else if (((i - 1) > -1 && (j - 1) > -1) && board[i - 1][j - 1].equals(player.getPlayerID())) {
			return true;
		}
		return false;
	}

	/**
	 * Attempts to place the piece in the first corner
	 * 
	 * @param yT
	 * @param xL
	 * @param selectedPiece
	 * @param player
	 */
	private void placeCorner1(int yT, int xL, Piece selectedPiece, Player player) {
		for (int k = yT; k < selectedPiece.getPieceType().length; k++) {
			for (int m = xL; m < selectedPiece.getPieceType()[k].length; m++) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					board[k - yT][m - xL] = player.getPlayerID();
					MainBoardController.setPlacedPiece(true);
				}
			}
		}
	}

	/**
	 * Attempts to place a piece in the second corner
	 * 
	 * @param yT
	 * @param xR
	 * @param selectedPiece
	 * @param player
	 */
	private void placeCorner2(int yT, int xR, Piece selectedPiece, Player player) {
		for (int k = yT; k < selectedPiece.getPieceType().length; k++) {
			for (int m = xR; m > -1; m--) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					board[k - yT][19 - (xR - m)] = player.getPlayerID();
					MainBoardController.setPlacedPiece(true);
				}
			}
		}
	}

	/**
	 * Attempts to place a piece in the third corner
	 * 
	 * @param yB
	 * @param xL
	 * @param selectedPiece
	 * @param player
	 */
	private void placeCorner3(int yB, int xL, Piece selectedPiece, Player player) {
		for (int k = yB; k > -1; k--) {
			for (int m = xL; m < selectedPiece.getPieceType()[k].length; m++) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					board[19 - (yB - k)][m - xL] = player.getPlayerID();
					MainBoardController.setPlacedPiece(true);
				}
			}
		}
	}

	/**
	 * Attempts to place a piece in the fourth corner
	 * 
	 * @param yB
	 * @param xR
	 * @param selectedPiece
	 * @param player
	 */
	private void placeCorner4(int yB, int xR, Piece selectedPiece, Player player) {
		for (int k = yB; k > -1; k--) {
			for (int m = xR; m > -1; m--) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					board[19 - (yB - k)][19 - (xR - m)] = player.getPlayerID();
					MainBoardController.setPlacedPiece(true);
				}
			}
		}
	}

	/**
	 * Checks if the first corner is available
	 * 
	 * @param yT
	 * @param xL
	 * @param selectedPiece
	 * @return
	 */
	private boolean checkCorner1(int yT, int xL, Piece selectedPiece) {
		boolean onCorner = false;
		for (int k = yT; k < selectedPiece.getPieceType().length; k++) {
			for (int m = xL; m < selectedPiece.getPieceType()[k].length; m++) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					if ((k - yT) == 0 && (m - xL) == 0) {
						onCorner = true;
					}
				}
			}
		}
		return onCorner;
	}

	/**
	 * Checks if the second corner is available
	 * 
	 * @param yT
	 * @param xR
	 * @param selectedPiece
	 * @return
	 */
	private boolean checkCorner2(int yT, int xR, Piece selectedPiece) {
		boolean onCorner = false;
		for (int k = yT; k < selectedPiece.getPieceType().length; k++) {
			for (int m = xR; m > -1; m--) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					if ((k - yT) == 0 && (19 - (xR - m)) == 19) {
						onCorner = true;
					}
				}
			}
		}
		return onCorner;
	}

	/**
	 * Checks if the third corner is available
	 * 
	 * @param yB
	 * @param xL
	 * @param selectedPiece
	 * @return
	 */
	private boolean checkCorner3(int yB, int xL, Piece selectedPiece) {
		boolean onCorner = false;
		for (int k = yB; k > -1; k--) {
			for (int m = xL; m < selectedPiece.getPieceType()[k].length; m++) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					if ((19 - (yB - k)) == 19 && (m - xL) == 0) {
						onCorner = true;
					}
				}
			}
		}
		return onCorner;
	}

	/**
	 * Checks if the fourth corner is available
	 * 
	 * @param yB
	 * @param xR
	 * @param selectedPiece
	 * @return
	 */
	private boolean checkCorner4(int yB, int xR, Piece selectedPiece) {
		boolean onCorner = false;
		for (int k = yB; k > -1; k--) {
			for (int m = xR; m > -1; m--) {
				if (selectedPiece.getPieceType()[k][m] == 1) {
					if ((19 - (yB - k)) == 19 && (19 - (xR - m)) == 19) {
						onCorner = true;
					}
				}
			}
		}
		return onCorner;
	}

	/**
	 * Calculates the score for all 4 players
	 * 
	 * @return An int array of length for, score for player 1 is in 0, score for
	 *         player 2 is in 1, score for player 3 is in 2, and score for
	 *         player 4 is in 3.
	 */
	public int[] calculateScore() {
		int[] scores = new int[4];
		int score1 = 0;
		int score2 = 0;
		int score3 = 0;
		int score4 = 0;
		for (int u = 0; u < ROWS; u++) {
			for (int z = 0; z < COLUMNS; z++) {
				if (board[u][z].equals("p1") || board[u][z].equals("pa1")) {
					score1++;
				} else if (board[u][z].equals("p2") || board[u][z].equals("pa2")) {
					score2++;
				} else if (board[u][z].equals("p3") || board[u][z].equals("pa3")) {
					score3++;
				} else if (board[u][z].equals("p4") || board[u][z].equals("pa4")) {
					score4++;
				}
			}
		}

		scores[0] = score1;
		scores[1] = score2;
		scores[2] = score3;
		scores[3] = score4;
		return scores;
	}
}