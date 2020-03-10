package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Starting file for the Blokus application
 * @author brent
 * @version 12/8/16
 */
public class Blokus extends Application {
	
	
	/**
	 * Main for the program
	 * @param args The magical arguments that makes a main a main
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Loads the primary stage and goes to the setup page
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("SetupPage.fxml"));
		primaryStage.setTitle("Blokus");
		primaryStage.setScene(new Scene(root, 750, 500));
	    primaryStage.show();

	}
}
