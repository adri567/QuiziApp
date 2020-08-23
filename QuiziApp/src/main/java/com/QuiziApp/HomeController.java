package com.QuiziApp;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.QuiziApp.HelperClasses.Utility;
import com.QuiziApp.Models.QAModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class HomeController {

	
	// Properties
	Stage primaryStage = new Stage();
	ArrayList<String> folders = new ArrayList<String>();
	ObservableList<String> gruppen; 
	// Methods

	// Diese Methode wird bei jedem Programmstart als erstes aufgerufen
	public void initialize() throws JsonParseException, JsonMappingException, IOException {
		System.out.print("init");
		setupListView();
		
		//Instanz von dem object "ObjectMapper" erstellen.
		var reader = new ObjectMapper();
		
		// Konvertiert die json datei in eine ArrayList.			
		ArrayList<QAModel> model = reader.readValue(new File("Quizis/Chemie/asadasd.json"), new TypeReference<ArrayList<QAModel>>(){});
		
		for (QAModel qaModel : model) {
			System.out.println(qaModel.getQuestion());
			System.out.println(qaModel.getRightAnswers());
			System.out.println(qaModel.getWrongAnswers());
		}
		
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
	
	
	
	//Diese Methode f�gt der listView die ObservableList "items" hinzu.
	public void setupListView() {
		
		folders = Utility.sharedInstance.findFoldersInDirectory("Quizis");
		gruppen = FXCollections.observableArrayList(folders);
		
		selectQuizView.getItems().addAll(gruppen);
	}
	
	}

