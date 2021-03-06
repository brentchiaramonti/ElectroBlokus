package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Controller for the main board gui
 * 
 * @author brent
 * @version 12/8/16
 */
public class MainBoardController {
	@FXML
	Pane buttonPane;

	@FXML
	Pane seletedPiecePane;

	@FXML
	MenuButton File;

	@FXML
	Label p1Score;

	@FXML
	Label p2Score;

	@FXML
	Label p3Score;

	@FXML
	Label p4Score;

	@FXML
	HBox blocksChoiceDisplay;

	@FXML
	Label errorBox;

	@FXML
	Button aiButton;

	@FXML
	Label winnerLabel;

	Button[][] buttonArray;

	Button[][] selectedPieceButtonArray;

	String redColor = "-fx-font-size: 13; -fx-base: #C0392B;";
	String selectedRedColor = "-fx-font-size: 13; -fx-base: #999999;";
	String lightGreenColor = "-fx-font-size: 13; -fx-base: #40ff00;";
	String highlightColor = "-fx-font-size: 13; -fx-base: #66ffff;";
	String blackColor = "-fx-font-size: 13; -fx-base: #000000;";
	String blueColor = "-fx-font-size: 13; -fx-base: #000066;";
	String purpleColor = "-fx-font-size: 13; -fx-base: #cc0099;";
	String boardColor = " -fx-font-size: 13; -fx-base: #F0F3F4;";
	boolean firstPiecePlaced;

	private static final int NUMBER_OF_LINES = 20;
	private static final int BUTTONS_PER_LINE = 20;

	private static final int SELECTED_PIECE_LENGTH = 5;
	private static final int SELECTED_PIECE_HEIGHT = 5;

	private static int numPlayers;
	private static int numCompPlayers = 0;
	private static Player player1;
	private static Player player2;
	private static Player player3;
	private static Player player4;
	private static Player currentPlayer;
	// ComputerPlayer p = new ComputerPlayer(1);

	private static Piece selectedPiece;
	private static Board board = new Board();
	private static int selectedPieceNumber;

	private static boolean placedPiece = false;

	/**
	 * Method that is called every time the main board is loaded. Sets up the 2
	 * button panes
	 */
	@FXML
	protected void initialize() {
		if (player1 == null) {
			createPlayers();
			createComputerPlayers();
		}

		updateScore();
		displayPlayerPieces();
		buttonArray = new Button[NUMBER_OF_LINES][BUTTONS_PER_LINE];

		for (int i = 0; i < NUMBER_OF_LINES; i++) {
			for (int j = 0; j < BUTTONS_PER_LINE; j++) {
				buttonArray[i][j] = new Button("   ");

				buttonArray[i][j].minWidth(90.0);
				buttonArray[i][j].maxWidth(90.0);
				buttonArray[i][j].prefWidth(90.0);

				buttonArray[i][j].minHeight(90.0);
				buttonArray[i][j].maxHeight(90.0);
				buttonArray[i][j].prefHeight(90.0);

				buttonArray[i][j].setMnemonicParsing(false);

				buttonArray[i][j].setLayoutX(j * 29);
				buttonArray[i][j].setLayoutY(i * 29);

				buttonArray[i][j].setStyle(" -fx-font-size: 13; -fx-base: #F0F3F4;");

				buttonArray[i][j].setTextAlignment(TextAlignment.CENTER);
				buttonArray[i][j].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						// Player p = new Player(1);
						placePieceOnBoard(e, currentPlayer);
						resetBoard();
						if (placedPiece) {
							changePlayer();
							setPlacedPiece(false);
							board.calculateScore();
							if (currentPlayer.isAI()) {
								resetBoard();
								if (placedPiece) {
									changePlayer();
									setPlacedPiece(false);
									board.calculateScore();
								}
							}
						}

					}
				});

				buttonArray[i][j].setOnMouseEntered(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent t) {
						hoverOn(t);
					}
				});

				buttonArray[i][j].setOnMouseExited(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent t) {
						hoverOff(t);
					}
				});
				buttonPane.getChildren().add(buttonArray[i][j]);

			}
		}

		selectedPieceButtonArray = new Button[SELECTED_PIECE_HEIGHT][SELECTED_PIECE_LENGTH];

		for (int i = 0; i < SELECTED_PIECE_HEIGHT; i++) {
			for (int j = 0; j < SELECTED_PIECE_LENGTH; j++) {
				selectedPieceButtonArray[i][j] = new Button("   ");

				selectedPieceButtonArray[i][j].minWidth(90.0);
				selectedPieceButtonArray[i][j].maxWidth(90.0);
				selectedPieceButtonArray[i][j].prefWidth(90.0);

				selectedPieceButtonArray[i][j].minHeight(90.0);
				selectedPieceButtonArray[i][j].maxHeight(90.0);
				selectedPieceButtonArray[i][j].prefHeight(90.0);

				selectedPieceButtonArray[i][j].setMnemonicParsing(false);

				selectedPieceButtonArray[i][j].setLayoutX(j * 32);
				selectedPieceButtonArray[i][j].setLayoutY(i * 30);

				selectedPieceButtonArray[i][j].setStyle("-fx-base: #F0F3F4;");

				selectedPieceButtonArray[i][j].setTextAlignment(TextAlignment.CENTER);

				selectedPieceButtonArray[i][j].setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent e) {
						setReferencePoint(e);
					}

				});

				seletedPiecePane.getChildren().add(selectedPieceButtonArray[i][j]);
			}
		}

		resetBoard();
	}

	/**
	 * Resets the board and players and goes back to the set up pane
	 * 
	 * @param event
	 *            The button that is pressed
	 * @throws IOException
	 *             Throws an exception if it can't find the setup page
	 */
	public void resetGame(ActionEvent event) throws IOException {
		player1 = null;
		player2 = null;
		player3 = null;
		player4 = null;
		selectedPiece = null;
		board = new Board();
		currentPlayer = null;
		placedPiece = false;
		numCompPlayers = 0;

		Parent root = FXMLLoader.load(getClass().getResource("SetupPage.fxml"));
		Stage stage = (Stage) File.getScene().getWindow();
		stage.setTitle("Blokus");
		stage.setScene(new Scene(root, 762.0, 472.0));
		stage.show();
	}

	/**
	 * method to have the current player stop playing
	 * 
	 * @param event
	 *            The button that is pressed
	 */
	public void passCurrentPlayer(ActionEvent event) {
		currentPlayer.setPassed(true);
		changePlayer();
	}

	/**
	 * Create the human players
	 */
	private void createPlayers() {
		Player[] players = new Player[numPlayers];
		for (int k = 0; k < numPlayers; k++) {
			players[k] = new Player(k + 1, false);
		}

		if (numPlayers + numCompPlayers == 1) {
			players[0].doubleAmountOfPieces();
			players[0].doubleAmountOfPieces();
		}
		if (numPlayers + numCompPlayers == 2) {
			if (numPlayers == 2) {
				players[0].doubleAmountOfPieces();
				players[1].doubleAmountOfPieces();
			} else {
				players[0].doubleAmountOfPieces();
			}
		}
		assignPlayers(players);
	}

	/**
	 * Create the computer players
	 */
	private void createComputerPlayers() {
		Player[] players = new Player[5];
		// for the number of reg players +1 until 4 players

		for (int k = numPlayers + 1; k <= numPlayers + numCompPlayers; k++) {
			// create array of computer players
			players[k] = new ComputerPlayer(k);
		}

		if (numPlayers + numCompPlayers == 2) {
			if (numCompPlayers == 1) {
				players[numPlayers + 1].doubleAmountOfPieces();
			}
		}
		assignCompPlayers(players);
	}

	/**
	 * Assign player variables to actual player objects
	 * 
	 * @param players
	 *            array of player objects
	 */
	private void assignPlayers(Player[] players) {
		for (int k = 0; k < numPlayers; k++) {
			if (k == 0) {
				player1 = players[0];
				player1.setColor(redColor);
			} else if (k == 1) {
				player2 = players[1];
				player2.setColor(blackColor);
			} else if (k == 2) {
				player3 = players[2];
				player3.setColor(blueColor);
			} else if (k == 3) {
				player4 = players[3];
				player4.setColor(purpleColor);
			}
		}
		currentPlayer = player1;
	}

	/**
	 * Assign the player variables to computer player objects
	 * 
	 * @param players
	 *            array of computer player objects
	 */
	private void assignCompPlayers(Player[] players) {
		// for the number of total players
		for (int k = 2; k <= numPlayers + numCompPlayers; k++) {
			if (players[k] != null) {
				if (k == 2) {
					player2 = players[k];
					player2.setColor(blackColor);
				} else if (k == 3) {
					player3 = players[k];
					player3.setColor(blueColor);
				} else if (k == 4) {
					player4 = players[k];
					player4.setColor(purpleColor);
				}
			}
		}
	}

	/**
	 * Rotate between human and ai players
	 */
	private void changePlayer() {
		resetButtons();
		updateScore();
		clearPlayerPieces();
		selectedPiece = null;
		if (currentPlayer == player1) {
			if (numPlayers + numCompPlayers >= 2) {
				currentPlayer = player2;
			}
		} else if (currentPlayer == player2) {
			if (numPlayers + numCompPlayers >= 3) {
				currentPlayer = player3;
			} else if (numPlayers + numCompPlayers == 2) {
				currentPlayer = player1;
			}
		} else if (currentPlayer == player3) {
			if (numPlayers + numCompPlayers == 4) {
				currentPlayer = player4;
			} else if (numPlayers + numCompPlayers == 3) {
				currentPlayer = player1;
			}
		} else if (currentPlayer == player4) {
			currentPlayer = player1;
		}

		if (currentPlayer.getPassed()) {
			boolean allPassed = true;
			if (player1 != null) {
				if (!player1.getPassed()) {
					allPassed = false;
				}
			}
			if (player2 != null) {
				if (!player2.getPassed()) {
					allPassed = false;
				}
			}
			if (player3 != null) {
				if (!player3.getPassed()) {
					allPassed = false;
				}
			}
			if (player4 != null) {
				if (!player4.getPassed()) {
					allPassed = false;
				}
			}

			if (allPassed) {
				declareWinner();
			} else {
				changePlayer();
			}
		} else {

			displayPlayerPieces();
			if (currentPlayer.isAI()) {
				if (currentPlayer.isFirstTurn()) {
					chooseCornerSpot();
				} else {
					chooseSpot((ComputerPlayer) currentPlayer);
				}
			} else {
				resetBoard();
			}
		}

	}

	/**
	 * Finds which player has the highest score and displays the winner to the
	 * user
	 */
	public void declareWinner() {
		int winningPlayerNumber = 1;
		if (player1 != null) {
			int player1Score = player1.getScore();
			if (player2 != null) {
				int player2Score = player2.getScore();
				if (player3 != null) {
					int player3Score = player3.getScore();
					if (player4 != null) {
						int player4Score = player4.getScore();
						winningPlayerNumber = findBiggest4(player1Score, player2Score, player3Score, player4Score);
					} else {
						winningPlayerNumber = findBiggest3(player1Score, player2Score, player3Score);
					}
				} else {
					winningPlayerNumber = findBiggest2(player1Score, player2Score);
				}
			}
		}

		if (winningPlayerNumber == 1) {
			winnerLabel.setText("Player 1 Wins");
		} else if (winningPlayerNumber == 2) {
			winnerLabel.setText("Player 2 Wins");
		} else if (winningPlayerNumber == 3) {
			winnerLabel.setText("Player 3 Wins");
		} else if (winningPlayerNumber == 4) {
			winnerLabel.setText("Player 4 Wins");
		}
	}

	/**
	 * Finds the biggest of 4 ints
	 * 
	 * @param p1
	 *            the first int
	 * @param p2
	 *            the second int
	 * @param p3
	 *            the third int
	 * @param p4
	 *            the fourth int
	 * @return 1, 2, 3, or 4, depending on which number was the highest
	 */
	private int findBiggest4(int p1, int p2, int p3, int p4) {
		if (p1 > p2) {
			if (p1 > p3) {
				if (p1 > p4) {
					return 1;
				} else {
					return 4;
				}
			} else {
				if (p3 > p4) {
					return 3;
				} else {
					return 4;
				}
			}
		} else {
			if (p2 > p3) {
				if (p2 > p4) {
					return 2;
				} else {
					return 4;
				}
			} else {
				if (p3 > p4) {
					return 3;
				} else {
					return 4;
				}
			}
		}
	}

	/**
	 * Finds the biggest of 3 ints
	 * 
	 * @param p1
	 *            the first int
	 * @param p2
	 *            the second int
	 * @param p3
	 *            the third int
	 * @return 1, 2, or 3, depending on which number was the highest
	 */
	private int findBiggest3(int p1, int p2, int p3) {
		if (p1 > p2) {
			if (p1 > p3) {
				return 1;
			} else {
				return 3;
			}
		} else {
			if (p2 > p3) {
				return 2;
			} else {
				return 3;
			}
		}
	}

	/**
	 * Finds the biggest of 2 ints
	 * 
	 * @param p1
	 *            the first int
	 * @param p2
	 *            the second int
	 * @return 1, or 2, depending on which number was the highest
	 */
	private int findBiggest2(int p1, int p2) {
		if (p1 > p2) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * Sets the number of human players
	 * 
	 * @param num
	 *            number of human players
	 */
	public static void setPlayers(int num) {
		numPlayers = num;
	}

	/**
	 * Sets the number of computer players
	 * 
	 * @param num
	 *            number of computer players
	 */
	public static void setComputerPlayers(int num) {
		numCompPlayers = num;
	}

	/**
	 * Sets the boolean placedPiece
	 * 
	 * @param placed
	 *            What placedPiece will be
	 */
	public static void setPlacedPiece(boolean placed) {
		placedPiece = placed;
	}

	/**
	 * Opens the Registration Page when the user selects Register
	 * 
	 * @param event
	 *            the user clicks the register button
	 * @throws Exception
	 *             the page does not open properly
	 */
	public void openRulesPage(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("RulesGui.fxml"));
		Stage stage = (Stage) File.getScene().getWindow();
		stage.setTitle("Blokus: Rules");
		stage.setScene(new Scene(root, 1000, 900));
		stage.show();
	}

	/**
	 * Method for displaying the current piece on the board when hovering over a
	 * button
	 * 
	 * @param t
	 *            The mouseEvent for hovering of a specific button
	 */
	public void hoverOn(MouseEvent t) {
		if (selectedPiece != null) {
			int[][] pieceArray = selectedPiece.getPieceType();
			int buttonX = (int) ((Node) t.getSource()).getLayoutX() / 29;
			int buttonY = (int) ((Node) t.getSource()).getLayoutY() / 29;
			int pointX = selectedPiece.getPointOfReference()[1];
			int pointY = selectedPiece.getPointOfReference()[0];

			for (int i = 0; i < pieceArray.length; i++) {
				for (int j = 0; j < pieceArray[i].length; j++) {
					try {
						if (pieceArray[i][j] == 1) {
							if (board.getBoard()[buttonY + i - pointY][buttonX + j - pointX].equals("p")) {
								buttonArray[buttonY + i - pointY][buttonX + j - pointX].setStyle(lightGreenColor);
							}
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
		}
	}

	/**
	 * Resets the board to get rid of the piece so it can be displayed elsewhere
	 * 
	 * @param t
	 */
	public void hoverOff(MouseEvent t) {
		highlightValid();
	}

	/**
	 * Rotates the selected piece to the left and re displays it to the user
	 * 
	 * @param event
	 *            The event for the button being pressed
	 */
	public void rotateLeft(ActionEvent event) {
		if (selectedPiece != null) {
			selectedPiece.rotateLeft();
			resetButtons();
			showPiece();
		} else {
			errorBox.setText("Choose a piece!");
		}
	}

	/**
	 * Rotates the selected piece to the right and re displays it to the user
	 * 
	 * @param event
	 *            The event for the button being pressed
	 */
	public void rotateRight(ActionEvent event) {
		if (selectedPiece != null) {
			selectedPiece.rotateRight();
			resetButtons();
			showPiece();
		} else {
			errorBox.setText("Choose a piece!");
		}
	}

	/**
	 * Flips the selected piece vertically and re displays it to the user
	 * 
	 * @param event
	 *            The event for the button being pressed
	 */
	public void flip(ActionEvent event) {
		if (selectedPiece != null) {
			selectedPiece.flip();
			resetButtons();
			showPiece();
		} else {
			errorBox.setText("Choose a piece!");
		}
	}

	/**
	 * Sets a new reference point for the selected piece
	 * 
	 * @param event
	 *            The event of one of the selectedPane buttons being pressed
	 */
	public void setReferencePoint(ActionEvent event) {
		if (selectedPiece != null) {
			int i = (int) ((Node) event.getSource()).getLayoutY() / 30;
			int j = (int) ((Node) event.getSource()).getLayoutX() / 32;

			if (selectedPiece.getPieceType()[i][j] == 1) {
				selectedPiece.setPointOfReference(new int[] { i, j });
				resetButtons();
				showPiece();
			}
		}
	}

	/**
	 * Method for placing a piece on the board
	 * 
	 * @param event
	 *            The button where the piece will be placed
	 * @param p
	 *            This does nothing, but to late to remove it now
	 */
	public void placePieceOnBoard(ActionEvent event, Player p) {
		int i = (int) ((Node) event.getSource()).getLayoutY() / 29;
		int j = (int) ((Node) event.getSource()).getLayoutX() / 29;
		errorBox.setText(board.placePiece(selectedPiece, p, j, i));
		if (errorBox.getText().equals(" ")) {
			currentPlayer.getPieces()[selectedPieceNumber]--;

		}
		resetBoard();
	}

	/**
	 * Updates the score labels
	 */
	public void updateScore() {
		int[] scores = board.calculateScore();

		if (player1 != null) {
			if (player1.checkIfOutOfPieces()) {
				player1.setScore(scores[0] + 15);
			} else {
				player1.setScore(scores[0]);
			}
			p1Score.setText("p1: " + player1.getScore());
		} else {
			p1Score.setText("");
		}
		if (player2 != null) {
			if (player2.checkIfOutOfPieces()) {
				player2.setScore(scores[1] + 15);
			} else {
				player2.setScore(scores[1]);
			}
			p2Score.setText("p2: " + player2.getScore());
		} else {
			p2Score.setText("");
		}
		if (player3 != null) {
			if (player3.checkIfOutOfPieces()) {
				player3.setScore(scores[2] + 15);
			} else {
				player3.setScore(scores[2]);
			}
			p3Score.setText("p3: " + player3.getScore());
		} else {
			p3Score.setText("");
		}
		if (player4 != null) {
			if (player4.checkIfOutOfPieces()) {
				player4.setScore(scores[3] + 15);
			} else {
				player4.setScore(scores[3]);
			}
			p4Score.setText("p4: " + player4.getScore());
		} else {
			p4Score.setText("");
		}
	}

	/**
	 * resets the selected piece panel
	 */
	public void resetButtons() {
		for (int i = 0; i < selectedPieceButtonArray.length; i++) {
			for (int j = 0; j < selectedPieceButtonArray[0].length; j++) {
				selectedPieceButtonArray[i][j].setStyle(null);
			}
		}
	}

	/**
	 * Makes the selected piece appear in the selected piece pane
	 */
	public void showPiece() {
		errorBox.setText("");
		int[][] piece = selectedPiece.getPieceType();
		int[] referencePoint = selectedPiece.getPointOfReference();
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if (piece[i][j] == 1) {
					if (referencePoint[0] == i && referencePoint[1] == j) {
						selectedPieceButtonArray[i][j].setStyle(selectedRedColor);
					} else {
						selectedPieceButtonArray[i][j].setStyle(currentPlayer.getColor());
					}
				}
			}
		}
		highlightValid();
	}

	/**
	 * Goes through the entire board and checks if that button is a valid place
	 * to place the current selected piece then sets that button to be light
	 * blue
	 */
	public void highlightValid() {

		// checks if the selected piece is not null
		if (selectedPiece != null) {

			// resets the board
			resetBoard();

			// loops through the entire board
			for (int i = 0; i < NUMBER_OF_LINES; i++) {
				for (int j = 0; j < BUTTONS_PER_LINE; j++) {

					// checks if the current button is a valid location
					// Player p = new Player(1);
					if (board.checkIfValidLocation(i, j, selectedPiece, currentPlayer)) {

						// checks if the current button is not occupied by a
						// piece
						buttonArray[i][j].setStyle(highlightColor);

					}
				}
			}
		}
	}

	/**
	 * Refreshes the board, making it look liike what the board actually is,
	 * without hover over or highlighted buttons
	 */
	public void resetBoard() {
		for (int i = 0; i < NUMBER_OF_LINES; i++) {
			for (int j = 0; j < BUTTONS_PER_LINE; j++) {
				if (board.getBoard()[i][j].equals("p")) {
					buttonArray[i][j].setStyle(boardColor);
				} else if (board.getBoard()[i][j].equals("p1")) {
					buttonArray[i][j].setStyle(redColor);
				} else if (board.getBoard()[i][j].equals("p2") || board.getBoard()[i][j].equals("pa2")) {
					buttonArray[i][j].setStyle(blackColor);
				} else if (board.getBoard()[i][j].equals("p3") || board.getBoard()[i][j].equals("pa3")) {
					buttonArray[i][j].setStyle(blueColor);
				} else if (board.getBoard()[i][j].equals("p4") || board.getBoard()[i][j].equals("pa4")) {
					buttonArray[i][j].setStyle(purpleColor);
				}
			}
		}
	}

	/**
	 * AI method that is called after the button AI Test is clicked
	 * 
	 * @param event
	 */
	public void letsPlaceAI(ActionEvent event) {
		int count = 0;
		while (count < 1) {
			placeInitialPiece((ComputerPlayer) currentPlayer);
			if (errorBox.getText().equals("Piece can be placed!")) {
				count++;
				resetBoard();
				currentPlayer.setFirstTurn(false);
				checkValidAI((ComputerPlayer) currentPlayer);
			}
		}

	}

	/**
	 * AI Testing code, places the first piece after button is clicked bug
	 * currently occurs occasionally
	 * 
	 * @param ComputerPlayer
	 *            player This isn't used anymore, too late to remove it
	 */
	public void placeInitialPiece(ComputerPlayer player) {
		player = (ComputerPlayer) MainBoardController.currentPlayer;
		resetButtons();
		selectRandomPiece(0);
		randomRotation();
		showPiece();
		int buttonY = 0;
		int buttonX = 0;
		errorBox.setText(board.placePiece(selectedPiece, player, buttonX, buttonY));
		if (errorBox.getText().length() < 2) {
			errorBox.setText("Piece can be placed!");
			currentPlayer.setFirstTurn(false);
			currentPlayer.getPieces()[selectedPieceNumber]--;
		}
	}

	/**
	 * checks to see if the location is valid and a piece can be placed for the
	 * ai
	 */
	public void checkValidAI(ComputerPlayer player) {

		if (selectedPiece != null) {
			// resets the board
			resetBoard();

			// loops through the entire board
			for (int i = 0; i < NUMBER_OF_LINES; i++) {
				for (int j = 0; j < BUTTONS_PER_LINE; j++) {

					// checks if the current button is a valid location
					// Player p = new Player(1);
					if (board.checkIfValidLocation(i, j, selectedPiece, player)) {

						// checks if the current button is not occupied by a
						// piece
						if (board.getBoard()[i][j].equals("p")) {
							// buttonArray[i][j].setStyle(highlightColor);
							placeAIPiece(i, j, player);
						}
					}
				}
			}
		}
	}

	/**
	 * places the piece if the location is valid
	 * 
	 * @param buttonX
	 *            The X location of the pressed button
	 * @param buttonY
	 *            The Y location of the pressed button
	 */
	public void placeAIPiece(int buttonX, int buttonY, ComputerPlayer player) {
		resetButtons();
		selectRandomPiece(0);
		randomRotation();
		showPiece();
		errorBox.setText(board.placePiece(selectedPiece, player, buttonX, buttonY));
		if (errorBox.getText().length() < 2) {
			errorBox.setText("Piece can be placed!");

		}
	}

	/**
	 * Choose a spot to place the ai not the first turn
	 * 
	 * @param currentPlayer
	 *            current ai player
	 */
	private void chooseSpot(ComputerPlayer currentPlayer) {
		currentPlayer = (ComputerPlayer) MainBoardController.currentPlayer;
		boolean foundSpot = false;
		int h = (int) (Math.random() * 21 + 1);
		int g = 1;
		while (g < 1000 && !foundSpot) {
			selectRandomPiece(h);
			randomRotation();
			highlightValid();
			for (int u = 0; u < 20; u++) {
				for (int v = 0; v < 20; v++) {
					if (!foundSpot) {
						if (buttonArray[u][v].getStyle().equals(highlightColor)) {
							if (board.placePiece(selectedPiece, currentPlayer, v, u).length() < 2) {
								foundSpot = true;
								currentPlayer.getPieces()[selectedPieceNumber]--;
							}
						}
					}
				}
			}
			g++;
		}
		if (foundSpot) {
			changePlayer();
		} else {
			currentPlayer.setPassed(true);
			changePlayer();
		}

	}

	/**
	 * Chooses spot for first piece of ai
	 */
	private void chooseCornerSpot() {
		boolean foundSpot = false;
		int g = (int) (Math.random() * 21 + 1);
		int h = 1;
		while (!foundSpot && h < 1000) {
			g = (int) (Math.random() * 21 + 1);
			selectRandomPiece(g);
			randomRotation();
			// check top left
			if (board.placePiece(selectedPiece, currentPlayer, 0, 0).length() < 2) {
				foundSpot = true;
				// check top right
			} else if (board.placePiece(selectedPiece, currentPlayer, 0, 19).length() < 2) {
				foundSpot = true;
				// check bottom left
			} else if (board.placePiece(selectedPiece, currentPlayer, 19, 0).length() < 2) {
				foundSpot = true;
				// check bottom right
			} else if (board.placePiece(selectedPiece, currentPlayer, 19, 19).length() < 2) {
				foundSpot = true;
			}

			h++;

		}
		currentPlayer.getPieces()[selectedPieceNumber]--;
		if (foundSpot) {
			changePlayer();
		}
	}

	/**
	 * Selects a random piece for the ai
	 * 
	 * @param randInt
	 */
	public void selectRandomPiece(int randInt) {

		int[] pieces = currentPlayer.getPieces();

		while (pieces[randInt - 1] < 1) {
			if (currentPlayer.checkIfOutOfPieces()) {
				currentPlayer.setPassed(true);
				return;
			} else {
				randInt = (int) (Math.random() * 21 + 1);
			}
		}

		selectedPieceNumber = randInt - 1;
		if (randInt == 1) {
			selectedPiece = CreatePieces.createSingle();
		} else if (randInt == 2) {
			selectedPiece = CreatePieces.createDouble();
		} else if (randInt == 3) {
			selectedPiece = CreatePieces.createTriple();
		} else if (randInt == 4) {
			selectedPiece = CreatePieces.createTriple2();
		} else if (randInt == 5) {
			selectedPiece = CreatePieces.createQuad1();
		} else if (randInt == 6) {
			selectedPiece = CreatePieces.createQuad2();
		} else if (randInt == 7) {
			selectedPiece = CreatePieces.createQuad3();
		} else if (randInt == 8) {
			selectedPiece = CreatePieces.createQuad4();
		} else if (randInt == 9) {
			selectedPiece = CreatePieces.createQuad5();
		} else if (randInt == 10) {
			selectedPiece = CreatePieces.createPent1();
		} else if (randInt == 11) {
			selectedPiece = CreatePieces.createPent2();
		} else if (randInt == 12) {
			selectedPiece = CreatePieces.createPent3();
		} else if (randInt == 13) {
			selectedPiece = CreatePieces.createPent4();
		} else if (randInt == 14) {
			selectedPiece = CreatePieces.createPent5();
		} else if (randInt == 15) {
			selectedPiece = CreatePieces.createPent6();
		} else if (randInt == 16) {
			selectedPiece = CreatePieces.createPent7();
		} else if (randInt == 17) {
			selectedPiece = CreatePieces.createPent8();
		} else if (randInt == 18) {
			selectedPiece = CreatePieces.createPent9();
		} else if (randInt == 19) {
			selectedPiece = CreatePieces.createPent10();
		} else if (randInt == 20) {
			selectedPiece = CreatePieces.createPent11();
		} else if (randInt == 21) {
			selectedPiece = CreatePieces.createPent12();
		}
	}

	/**
	 * Puts the current selectedPiece into a random rotation
	 */
	public void randomRotation() {
		int randInt = (int) (Math.random() * 4 + 1);
		if (selectedPiece != null) {
			if (randInt == 1) {
				selectedPiece.rotateLeft();
			} else if (randInt == 2) {
				selectedPiece.rotateRight();
			} else if (randInt == 3) {
				selectedPiece.flip();
			} else {
				// do nothing
			}
		}
	}

	/**
	 * Clears the blocks choice display HBox so it can be repopulated
	 */
	public void clearPlayerPieces() {
		blocksChoiceDisplay.getChildren().clear();

	}

	/**
	 * Makes buttons for each type of piece the player still has in the HBox
	 */
	public void displayPlayerPieces() {
		int[] pieces = currentPlayer.getPieces();
		if (pieces[0] > 0) {
			Button pieceButton = new Button("", new ImageView("images/singlePieceImage.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("single");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[1] > 0) {
			Button pieceButton = new Button("", new ImageView("images/doublePieceImage.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("double");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[2] > 0) {
			Button pieceButton = new Button("", new ImageView("images/triplePieceImage.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("triple");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[3] > 0) {
			Button pieceButton = new Button("", new ImageView("images/triplePieceImage2.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("triple2");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[4] > 0) {
			Button pieceButton = new Button("", new ImageView("images/4PieceImage.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("4piece");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[5] > 0) {
			Button pieceButton = new Button("", new ImageView("images/4PieceImage2.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("4piece2");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[6] > 0) {
			Button pieceButton = new Button("", new ImageView("images/4PieceImage3.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("4piece3");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[7] > 0) {
			Button pieceButton = new Button("", new ImageView("images/4PieceImage4.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("4piece4");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[8] > 0) {
			Button pieceButton = new Button("", new ImageView("images/4PieceImage5.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("4piece5");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[9] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[10] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage2.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece2");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[11] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage3.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece3");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[12] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage4.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece4");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[13] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage5.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece5");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[14] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage6.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece6");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[15] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage7.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece7");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[16] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage8.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece8");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[17] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage9.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece9");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[18] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage10.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece10");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[19] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage11.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece11");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
		if (pieces[20] > 0) {
			Button pieceButton = new Button("", new ImageView("images/5PieceImage12.png"));
			pieceButton.setPrefHeight(220.0);
			pieceButton.setPrefWidth(170.0);
			pieceButton.setId("5piece12");
			pieceButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					selectPiece(e);
				}
			});
			blocksChoiceDisplay.getChildren().add(pieceButton);
		}
	}

	/**
	 * Gets which button was pressed and makes a piece depending on the button
	 * 
	 * @param event
	 *            The button that was pressed
	 */
	public void selectPiece(ActionEvent event) {
		String id = ((Node) event.getSource()).getId();
		if (id.equals("single")) {
			selectSinglePiece(event);
		}
		if (id.equals("double")) {
			selectDoublePiece(event);
		}
		if (id.equals("triple")) {
			selectTriplePiece(event);
		}
		if (id.equals("triple2")) {
			selectTriplePiece2(event);
		}
		if (id.equals("4piece")) {
			selectQuadPiece(event);
		}
		if (id.equals("4piece2")) {
			selectQuadPiece2(event);
		}
		if (id.equals("4piece3")) {
			selectQuadPiece3(event);
		}
		if (id.equals("4piece4")) {
			selectQuadPiece4(event);
		}
		if (id.equals("4piece5")) {
			selectQuadPiece5(event);
		}
		if (id.equals("5piece")) {
			selectPentPiece(event);
		}
		if (id.equals("5piece2")) {
			selectPentPiece2(event);
		}
		if (id.equals("5piece3")) {
			selectPentPiece3(event);
		}
		if (id.equals("5piece4")) {
			selectPentPiece4(event);
		}
		if (id.equals("5piece5")) {
			selectPentPiece5(event);
		}
		if (id.equals("5piece6")) {
			selectPentPiece6(event);
		}
		if (id.equals("5piece7")) {
			selectPentPiece7(event);
		}
		if (id.equals("5piece8")) {
			selectPentPiece8(event);
		}
		if (id.equals("5piece9")) {
			selectPentPiece9(event);
		}
		if (id.equals("5piece10")) {
			selectPentPiece10(event);
		}
		if (id.equals("5piece11")) {
			selectPentPiece11(event);
		}
		if (id.equals("5piece12")) {
			selectPentPiece12(event);
		}
	}

	/**
	 * Makes the selectedPiece be a single piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectSinglePiece(ActionEvent event) {
		selectedPieceNumber = 0;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createSingle();
			showPiece();
		}

	}

	/**
	 * Makes the selectedPiece be a double piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectDoublePiece(ActionEvent event) {
		selectedPieceNumber = 1;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createDouble();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a triple piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectTriplePiece(ActionEvent event) {
		selectedPieceNumber = 2;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createTriple();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a trimple 2 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectTriplePiece2(ActionEvent event) {
		selectedPieceNumber = 3;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createTriple2();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a quad piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectQuadPiece(ActionEvent event) {
		selectedPieceNumber = 4;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createQuad1();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a quad 2 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectQuadPiece2(ActionEvent event) {
		selectedPieceNumber = 5;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createQuad2();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a quad 3 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectQuadPiece3(ActionEvent event) {
		selectedPieceNumber = 6;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createQuad3();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a quad 4 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectQuadPiece4(ActionEvent event) {
		selectedPieceNumber = 7;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createQuad4();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a quad 5 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectQuadPiece5(ActionEvent event) {
		selectedPieceNumber = 8;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createQuad5();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece(ActionEvent event) {
		selectedPieceNumber = 9;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent1();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 2 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece2(ActionEvent event) {
		selectedPieceNumber = 10;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent2();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 3 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece3(ActionEvent event) {
		selectedPieceNumber = 11;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent3();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 4 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece4(ActionEvent event) {
		selectedPieceNumber = 12;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent4();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 5 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece5(ActionEvent event) {
		selectedPieceNumber = 13;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent5();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 6 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece6(ActionEvent event) {
		selectedPieceNumber = 14;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent6();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 7 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece7(ActionEvent event) {
		selectedPieceNumber = 15;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent7();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 8 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece8(ActionEvent event) {
		selectedPieceNumber = 16;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent8();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 9 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece9(ActionEvent event) {
		selectedPieceNumber = 17;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent9();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 10 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece10(ActionEvent event) {
		selectedPieceNumber = 18;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent10();
			showPiece();
		}
	}

	/**
	 * Makes the selectedPiece be a pent 11 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece11(ActionEvent event) {
		selectedPieceNumber = 19;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent11();
			showPiece();

		}
	}

	/**
	 * Makes the selectedPiece be a pent 12 piece
	 * 
	 * @param event
	 *            Not used, to late to be removed
	 */
	public void selectPentPiece12(ActionEvent event) {
		selectedPieceNumber = 20;

		if (currentPlayer.getPieces()[selectedPieceNumber] > 0) {
			resetButtons();
			selectedPiece = CreatePieces.createPent12();
			showPiece();

		}
	}

}
