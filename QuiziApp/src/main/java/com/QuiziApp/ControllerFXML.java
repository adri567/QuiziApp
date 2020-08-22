package com.QuiziApp;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import com.QuiziApp.HelperClasses.ConvertWordDocToText;
import com.QuiziApp.HelperClasses.WordsearchAlgorithm;
import com.QuiziApp.Models.QAModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControllerFXML {

	
	// Properties
	Label showWordText = new Label();
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

    	System.out.println("test");
    	
    	// FileChooser wird erstellt, damit wir ein Word file aussuchen kÃ¶nnen
		FileChooser fileChooser = new FileChooser();
		setupFilter(fileChooser);
		File wordFile= fileChooser.showOpenDialog(primaryStage);
		System.out.print(wordFile.getAbsolutePath());
		ConvertWordDocToText text = new ConvertWordDocToText();
		WordsearchAlgorithm search = new WordsearchAlgorithm();

		
		try {
			showWordText.setText(text.getText(wordFile));
			//search.filter(text.getText(wordFile));
			
			ArrayList<QAModel>  questions = search.filterQuestionsFromPackage(text.getText(wordFile));
			
			saveQuestion(questions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    private void setupFilter(FileChooser fileChooser) {
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word document", "*.docx"));
	}
	
	public void saveQuestion(ArrayList<QAModel> pack) throws IOException {
		File file = new File("DerPenisIst5cmGroß.json");
		
		// Neuen Ordner erstellen
//		File dir = new File("Folder");
//		dir.mkdir();
		
		var writer = new ObjectMapper();
		writer.enable(SerializationFeature.INDENT_OUTPUT);
		writer.writeValue(file, pack);
		System.out.print("Success");
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

