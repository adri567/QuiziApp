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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class QuiziMain extends Application { 

	Button wordFileButton = new Button("Choose Word File");
	Label showWordText = new Label();

	@Override
	public void start(final Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		//BorderPane root  = new BorderPane();
		VBox root = new VBox();
		
		
		wordFileButton.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				// FileChooser wird erstellt, damit wir ein Word file aussuchen können
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
		});
		
		root.getChildren().addAll(wordFileButton, showWordText);
		
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void setupFilter(FileChooser fileChooser) {
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word document", "*.docx"));
	}
	
	public void saveQuestion(ArrayList<QAModel> pack) throws IOException {
		File file = new File("test2.json");
		
		// Neuen Ordner erstellen
//		File dir = new File("Folder");
//		dir.mkdir();
		
		var writer = new ObjectMapper();
		writer.enable(SerializationFeature.INDENT_OUTPUT);
		writer.writeValue(file, pack);
		
			
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}
