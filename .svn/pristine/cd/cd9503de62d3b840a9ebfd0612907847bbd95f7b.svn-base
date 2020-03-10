package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Controller for the setup page
 * 
 * @author becca
 * @version 12/8/16
 */
public class SetupController {
	String redColor = "-fx-font-size: 13; -fx-base: #C0392B;";
	String blackColor = "-fx-font-size: 13; -fx-base: #000000;";
	String whiteColor = "-fx-font-size: 13; -fx-base: #ffffff;";
	String blueColor = "-fx-font-size: 13; -fx-base: #000066;";

	@FXML
	Button startGame;

	@FXML
	RadioButton onePlayerBtn;

	@FXML
	RadioButton twoPlayerBtn;

	@FXML
	RadioButton threePlayerBtn;

	@FXML
	RadioButton fourPlayerBtn;

	@FXML
	RadioButton zeroAIBtn;

	@FXML
	RadioButton oneAIBtn;

	@FXML
	RadioButton twoAIBtn;

	@FXML
	RadioButton threeAIBtn;

	final ToggleGroup playerGroup = new ToggleGroup();
	final ToggleGroup AIGroup = new ToggleGroup();

	/**
	 * Method that is called every time this page is loaded
	 */
	@FXML
	protected void initialize() {
		// if it's the first turn and there is a piece in the selection pane,
		// get the shape of the piece from the selection pane and update the
		// pieces is allowed
		// if not the first turn check that the surrounding pieces are not of
		// the same color except diagonals
		onePlayerBtn.setSelected(true);
		onePlayerBtn.setToggleGroup(playerGroup);
		twoPlayerBtn.setToggleGroup(playerGroup);
		threePlayerBtn.setToggleGroup(playerGroup);
		fourPlayerBtn.setToggleGroup(playerGroup);

		oneAIBtn.setSelected(true);
		zeroAIBtn.setToggleGroup(AIGroup);
		oneAIBtn.setToggleGroup(AIGroup);
		twoAIBtn.setToggleGroup(AIGroup);
		twoAIBtn.setDisable(true);
		threeAIBtn.setToggleGroup(AIGroup);

	}

	/**
	 * Method for pressing the lets play button
	 * 
	 * @param event
	 *            The event of the button being pressed
	 * @throws Exception
	 *             An exception if the main page can't be found
	 */
	public void openMainPage(ActionEvent event) throws Exception {
		if (onePlayerBtn.isSelected()) {
			MainBoardController.setPlayers(1);
		} else if (twoPlayerBtn.isSelected()) {
			MainBoardController.setPlayers(2);
		} else if (threePlayerBtn.isSelected()) {
			MainBoardController.setPlayers(3);
		} else if (fourPlayerBtn.isSelected()) {
			MainBoardController.setPlayers(4);
		}

		if (oneAIBtn.isSelected()) {
			MainBoardController.setComputerPlayers(1);
		}
		if (twoAIBtn.isSelected()) {
			MainBoardController.setComputerPlayers(2);
		}
		if (threeAIBtn.isSelected()) {
			MainBoardController.setComputerPlayers(3);
		}
		Parent root = FXMLLoader.load(getClass().getResource("MainBoardGui.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setTitle("Electro-Blokus");
		stage.setScene(new Scene(root, 1000, 900));
		stage.show();

	}

	/**
	 * Disables buttons depending on which button has been selected
	 */
	public void disableBtns() {
		if (onePlayerBtn.isSelected()) {
			oneAIBtn.setDisable(false);
			twoAIBtn.setDisable(true);
			threeAIBtn.setDisable(false);
			zeroAIBtn.setDisable(false);
		} else if (twoPlayerBtn.isSelected()) {
			zeroAIBtn.setSelected(true);
			threeAIBtn.setDisable(true);
			oneAIBtn.setDisable(true);
			zeroAIBtn.setDisable(false);
			twoAIBtn.setDisable(false);
		} else if (threePlayerBtn.isSelected()) {
			twoAIBtn.setDisable(true);
			oneAIBtn.setDisable(false);
			oneAIBtn.setSelected(true);
			threeAIBtn.setDisable(true);
			zeroAIBtn.setDisable(true);
		} else if (fourPlayerBtn.isSelected()) {
			twoAIBtn.setDisable(true);
			oneAIBtn.setDisable(true);
			zeroAIBtn.setSelected(true);
			threeAIBtn.setDisable(true);
			zeroAIBtn.setDisable(false);
		}

	}
}
