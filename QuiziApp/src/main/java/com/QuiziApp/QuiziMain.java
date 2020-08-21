package com.QuiziApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.QuiziApp.HelperClasses.ConvertWordDocToText;
import com.QuiziApp.HelperClasses.WordsearchAlgorithm;
import com.QuiziApp.Models.QAModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class QuiziMain extends Application { 

	// Instanz der Klasse ControllerFXML
	ControllerFXML controller = new ControllerFXML();

	@Override
	public void start(final Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		Parent root = FXMLLoader.load(getClass().getResource("homeFXML.fxml"));
		
		// hier übergeben wir die primaryStage an der Variable primaryStage aus der Instanz controller
		controller.primaryStage = primaryStage;
		
		Scene scene = new Scene(root, 800, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}
