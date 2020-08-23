package com.QuiziApp;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;




public class PopupController {
	
	// Properties
	Stage primaryStage = new Stage();
	ArrayList<QAModel> questions = new ArrayList<QAModel>();
	ArrayList<String> folders = new ArrayList<String>();
	ObservableList<String> gruppen;
	String folderName = new String();
	String fileName = new String();
	String valueGruppe = new String();
	boolean closeWindow = true;
	
	// Methods
	
	public void initialize() {
		folders = findFoldersInDirectory("Quizis/");
		gruppen = FXCollections.observableArrayList(folders);
		gruppen.add(0, "Neue Gruppe");
		chooseSection.setItems(gruppen);
		chooseSection.setValue("Neue Gruppe");
	}


    @FXML
    private ChoiceBox<String> chooseSection;
        
    @FXML
    void chooseSectionTapped(ActionEvent event) {
    	
    	// Hier drin steht der aktuell ausgewählte Wert von der ChoiceBox
    	valueGruppe = chooseSection.getSelectionModel().getSelectedItem();
    	
    
    	// Hier wird überprüft, welcher Wert drin steht und es wird die passende abfrage ausgewählt und ausgeführt
    	if(valueGruppe == null) {
  
    		chooseNewSection.setDisable(false);
    		chooseNewSection.setPromptText("Neue Gruppe wählen oder erstellen");
    		
    	} else if (valueGruppe.equals("Neue Gruppe")) {
    		
    		chooseNewSection.setDisable(false);
    		chooseNewSection.setPromptText("Neue Gruppe wählen oder erstellen");
    		
    	} else {
    		
    		chooseNewSection.setDisable(true);	
    		chooseNewSection.setPromptText(valueGruppe);
    		chooseNewSection.setText("");
    		
    	}
    	
    }

    @FXML
    private TextField chooseNewSection;
    
    @FXML
    private TextField nameQuiz;

    @FXML
    private Button chooseDocument;

    @FXML
    void chooceDocumentTapped(ActionEvent event) {
    	
    	// FileChooser wird erstellt, damit wir ein Word file aussuchen können
		FileChooser fileChooser = new FileChooser();
		setupFilter(fileChooser);
		File wordFile= fileChooser.showOpenDialog(primaryStage);
		System.out.print(wordFile.getAbsolutePath());
		ConvertWordDocToText text = new ConvertWordDocToText();
		WordsearchAlgorithm search = new WordsearchAlgorithm();

	
		try {
			questions = search.filterQuestionsFromPackage(text.getText(wordFile));
			createQuizi.setDisable(false);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
    }
    
    
    
    
    private void setupFilter(FileChooser fileChooser) {
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word document", "*.docx"));
	}
	
	public void saveQuestion(ArrayList<QAModel> pack) throws IOException {
		
		var writer = new ObjectMapper();
		writer.enable(SerializationFeature.INDENT_OUTPUT);
		
		folderName = chooseNewSection.getText();
		fileName = nameQuiz.getText();
		closeWindow = true;
		
		if (!folderName.isEmpty()) {
			
			for (String folder : folders) {
				if (folderName.equals(folder)) {
					errorLabel.setVisible(true);
					errorLabel.setText("Gruppennamen bereits vorhanden");
					closeWindow = false;
					return;
				}
			}
			File dir = new File("Quizis/" + folderName);
			dir.mkdir();
			File file = new File(dir + "/" + fileName + ".json");
			writer.writeValue(file, pack);
		} else {
			File file = new File("Quizis/" + valueGruppe + "/" + fileName + ".json");
			writer.writeValue(file, pack);
		}

	}
	
	
	public ArrayList<String> findFoldersInDirectory(String directoryPath) {
	    File directory = new File(directoryPath);
		
	    FileFilter directoryFileFilter = new FileFilter() {
	        public boolean accept(File file) {
	            return file.isDirectory();
	        }
	    };
			
	    File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
	    ArrayList<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
	    for (File directoryAsFile : directoryListAsFile) {
	        foldersInDirectory.add(directoryAsFile.getName());
	    }

	    return foldersInDirectory;
	}
	
	
	@FXML
	private ListView<String> selectQuizView;
	
	//Observablelist wird erstellt und mit Werten gef�llt (TEST)
	ObservableList<String> items = FXCollections.observableArrayList("Adrian", "Jannik", "Daniel", "Oliver", "Nils");
	
	//Diese Methode f�gt der listView die ObservableList "items" hinzu.
	public void setupListView() {
		selectQuizView.getItems().addAll(items);
	}


    
    @FXML
    private Button createQuizi;
    
    @FXML
    void createQuiziTapped(ActionEvent event) {
    	
    	
    	try {
			saveQuestion(questions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (closeWindow) {
	    	Stage stage = (Stage) createQuizi.getScene().getWindow();
	    	stage.close();
    	}
    }

    @FXML
    private Label errorLabel;
    
}
