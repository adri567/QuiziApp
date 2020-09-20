package com.QuiziApp;

import java.io.File;

import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.QuiziApp.HelperClasses.ConvertWordDocToText;
import com.QuiziApp.HelperClasses.Utility;
import com.QuiziApp.HelperClasses.WordsearchAlgorithm;
import com.QuiziApp.Models.QAModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * 
 * Diese Klasse ist für das PopUp Fenster zuständig. 
 * 
 * 
 * @author Jannik Walter und Adrian Suthold
 * @version 1.3
 * @since 25.08.2020
 * 
 *        Copyright © 2020 Jannik Walter und Adrian Suthold. All rights reserved.
 */

public class PopupController {

	// MARK: - Properties

	Stage primaryStage = new Stage();
	ArrayList<QAModel> questions = new ArrayList<QAModel>();
	ArrayList<String> folders = new ArrayList<String>();
	ObservableList<String> gruppen;
	String folderName = new String();
	String fileName = new String();
	String valueGruppe = new String();
	boolean closeWindow = true;
	HomeController homeController = new HomeController();

	
	public void initialize() {
		folders = Utility.sharedInstance.findFoldersInDirectory("Quizis/");
		gruppen = FXCollections.observableArrayList(folders);
		gruppen.add(0, "Neue Gruppe");
		chooseSection.setItems(gruppen);
		chooseSection.setValue("Neue Gruppe");
	}
	
	// MARK: - Methods

	/// Diese Methode schaut nur nach Word Document Files
	///
	/// Parameter fileChooser: dieser Parameter beinhaltet die Datei die ausgewählt wurde
	private void setupFilter(FileChooser fileChooser) {
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word document", "*.docx"));
	}

	
	/// Diese Methode wandelt die Word Datei in eine JSON Datei um und speichert sie zu dem zugehörigen Ordner
	///
	/// Parameter pack: hier drin befindet sich die Fragen mit den Antworten
	private void saveQuestion(ArrayList<QAModel> pack) throws IOException {

		var writer = new ObjectMapper();
		writer.enable(SerializationFeature.INDENT_OUTPUT);

		folderName = chooseNewSection.getText();
		fileName = nameQuiz.getText();
		closeWindow = true;
		
		
		// Überprüft, ob der Ordnername doppelt vorhanden ist.
		if (chooseNewSection.isDisable() == false) {
			for (String folder : folders) {
				if (folderName.equals(folder)) {
					errorLabel.setVisible(true);
					errorLabel.setText("Gruppennamen bereits vorhanden");
					closeWindow = false;
					return;
				}
			}
		} 
		
		// Überprüft, ob ein Gruppenname eingegeben wurde
		if (chooseNewSection.isDisable() == false && folderName.isEmpty()) {
			errorLabel.setVisible(true);
			errorLabel.setText("Bitte ein Gruppennamen wählen");
			closeWindow = false;
			return;
		}

		// Überprüft, ob ein Quizname eingegeben wurde, wenn chooseNewSection disabled ist
		if (chooseNewSection.isDisable() && fileName.isEmpty()) {
			errorLabel.setVisible(true);
			errorLabel.setText("Bitte einen Quiznamen eingeben.");
			closeWindow = false;
			return;
		}
		
		// Überprüft, ob ein Quizname eingegeben wurde, wenn chooseNewSection enabledist, der folderName und der fileName empty ist
		if (chooseNewSection.isDisable() == false && !folderName.isEmpty() && fileName.isEmpty()) {
			errorLabel.setVisible(true);
			errorLabel.setText("Bitte einen Quiznamen eingeben.");
			closeWindow = false;
			return;
		}
		
		
		if (closeWindow) {
			// Wenn es ungleich null ist, wird ein Ordner mit der Datei gespeichert
			if (valueGruppe != null) {
				File dir = new File("Quizis/" + folderName);
				dir.mkdir();
				File file = new File("Quizis/" + valueGruppe + "/" + fileName + ".json");
				writer.writeValue(file, pack);
			} else {
				File dir = new File("Quizis/" + folderName);
				dir.mkdir();
				File file = new File(dir + "/" + fileName + ".json");
				writer.writeValue(file, pack);
			}
		}
	}

	@FXML
	private ChoiceBox<String> chooseSection;

	@FXML
	void chooseSectionTapped(ActionEvent event) {

		// Hier drin steht der aktuell ausgewählte Wert von der ChoiceBox
		valueGruppe = chooseSection.getSelectionModel().getSelectedItem();

		// Hier wird überprüft, welcher Wert drin steht und es wird die passende abfrage
		// ausgewählt und ausgeführt
		if (valueGruppe == null) {

			chooseNewSection.setDisable(false);
			chooseNewSection.setPromptText("Neue Gruppe wählen oder erstellen");

		} else if (valueGruppe.equals("Neue Gruppe")) {

			chooseNewSection.setDisable(false);
			chooseNewSection.setPromptText("Neue Gruppe wählen oder erstellen");

		}

		if (valueGruppe != null && !valueGruppe.equals("Neue Gruppe")) {
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
		File wordFile = fileChooser.showOpenDialog(primaryStage);
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

	@FXML
	private Button createQuizi;

	@FXML
	void createQuiziTapped(ActionEvent event) throws InvocationTargetException, IOException, InterruptedException {

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
