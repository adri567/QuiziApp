package com.QuiziApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * Diese Klasse ruft die HomeController Klasse auf.
 * 
 * 
 * @author Jannik Walter
 * @version 2.5
 * @since 20.08.2020
 * 
 *        Copyright © 2020 Jannik Walter. All rights reserved.
 */

public class QuiziMain extends Application {

	// Instanz der Klasse ControllerFXML
	PopupController popupController = new PopupController();

	@Override
	public void start(final Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Parent root = FXMLLoader.load(getClass().getResource("homeFXML.fxml"));

		// hier �bergeben wir die primaryStage an der Variable primaryStage aus der
		// Instanz controller
		popupController.primaryStage = primaryStage;

		Scene scene = new Scene(root, 950, 600);
		//scene.getStylesheets().add(getClass().getResource("answerView.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	// ..
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}
