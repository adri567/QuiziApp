package com.QuiziApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.QuiziApp.HelperClasses.ConvertWordDocToText;
import com.QuiziApp.HelperClasses.WordsearchAlgorithm;
import com.QuiziApp.Models.QAModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PopupController {
	
	// Properties
	Stage primaryStage = new Stage();


    @FXML
    private ChoiceBox<String> chooseSection;

    @FXML
    private TextField chooseNewSection;
    
    @FXML
    private TextField nameQuiz;

    @FXML
    private Button chooseDocument;

    @FXML
    void chooceDocumentTapped(ActionEvent event) {

    	System.out.println("test");
    	
    	// FileChooser wird erstellt, damit wir ein Word file aussuchen können
		FileChooser fileChooser = new FileChooser();
		setupFilter(fileChooser);
		File wordFile= fileChooser.showOpenDialog(primaryStage);
		System.out.print(wordFile.getAbsolutePath());
		ConvertWordDocToText text = new ConvertWordDocToText();
		WordsearchAlgorithm search = new WordsearchAlgorithm();

		
		try {
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
		String test = nameQuiz.getText();
		
		File file = new File(test + ".json");
		
		// Neuen Ordner erstellen
//		File dir = new File("Folder");
//		dir.mkdir();
		
		var writer = new ObjectMapper();
		writer.enable(SerializationFeature.INDENT_OUTPUT);
		writer.writeValue(file, pack);
		System.out.println("Success");
		createQuizi.setDisable(false);
	}
    
    @FXML
    private Button createQuizi;
    
    @FXML
    void createQuiziTapped(ActionEvent event) {
    	
    	Stage stage = (Stage) createQuizi.getScene().getWindow();
    	stage.close();
    }

    @FXML
    private Label errorLabel;

   

    

}
