package com.QuiziApp;


import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class HomeController {

	
	// Properties
	Stage primaryStage = new Stage();

	// Methods

	// Diese Methode wird bei jedem Programmstart als erstes aufgerufen
	public void initialize() {
		System.out.print("init");
		setupListView();
	}
	
    @FXML
    private Button createButton;
    
    @FXML
    void createButtonTapped(ActionEvent event) {
    	
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("uploadWDPopupFXML.fxml"));
			Scene createQuizScene = new Scene(root,300, 170);
			primaryStage.setScene(createQuizScene);
			primaryStage.show();
			primaryStage.setResizable(false);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	@FXML
	private ListView<String> selectQuizView;
	
	//Observablelist wird erstellt und mit Werten gefüllt (TEST)
	ObservableList<String> items = FXCollections.observableArrayList("Adrian", "Jannik", "Daniel", "Oliver", "Nils");
	
	//Diese Methode fügt der listView die ObservableList "items" hinzu.
	public void setupListView() {
		selectQuizView.getItems().addAll(items);
	}
	
	}

