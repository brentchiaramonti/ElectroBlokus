package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for the rules page
 * 
 * @author brent
 * @version 12/8/16
 */
public class RulesGuiController {

	/**
	 * method for the back button, opens the main board page
	 * 
	 * @param event
	 *            The button that was pressed
	 * @throws Exception
	 *             An exception if the main board page doesn't exist
	 */
	public void openMainPage(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("MainBoardGui.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setTitle("My Secure Passwords");
		stage.setScene(new Scene(root, 1000, 900));
		stage.show();
	}
}
