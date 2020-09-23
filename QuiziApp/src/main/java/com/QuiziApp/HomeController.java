package com.QuiziApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.collections4.map.HashedMap;

import com.QuiziApp.HelperClasses.Utility;
import com.QuiziApp.Models.QAModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * 
 * Diese Klasse ist für das Haupftfenster verantwortlich. Sie umfasst alle grundlegenden Informationen, die das Programm brauch um laufen zu können.
 * 
 * 
 * @author Jannik Walter und Adrian Suthold
 * @version 1.5
 * @since 24.08.2020
 * 
 *        Copyright © 2020 Jannik Walter und Adrian Suthold. All rights reserved.
 */


public class HomeController {

	// MARK: - Properties
	
	Stage primaryStage = new Stage();
	ArrayList<String> folders = new ArrayList<String>();				// Hier stehen die Ordner Namen drin, die in der ListView angezeigt werden
	ArrayList<String> filesOfFolder = new ArrayList<String>();			// Hier stehen die Datei Namen drin, die in der Listview angezeigt werden
	ObservableList<String> groups;										// In der ObservableList stehen im späteren Verlauf die Ordner drin	
	ObservableList<String> files;										// In der ObservableList stehen im späteren Verlauf die Ordner drin
	ArrayList<QAModel> questionPackages = new ArrayList<QAModel>();
	HashedMap<String, ArrayList<String>> answersOfQuestionsHashedMap = new HashedMap<String, ArrayList<String>>();
	private int countQuestion = 0;										// Diese Variable zählt die Frage bei welcher wir gerade sind
	private String folderName;
	private Boolean submissionOfQuestions = false;


	// Diese Methode wird bei jedem Programmstart als erstes aufgerufen
	public void initialize() {

		// Hier werden die beiden Buttens auf Disable gesetzt, damit man Anfangs nicht drauf klicken kann
		backButton.setDisable(true);
		nextButton.setDisable(true);
		
		// Methoden aufrufen
		setupSectionListView();
		setupQuizListView();
		getQuizFromListView();

	}
	
	//  MARK: - Methods
	
	
	/// Diese Methode löscht alle Antwortmöglichkeiten aus der VBox. 
	///
	///
	private void removeAnswersFromVBox() {
		int size = answerVBox.getChildren().size();
		questionTextArea.setText("");
		for (int i = 0; i < size; i++) {
			answerVBox.getChildren().remove(0);
		}
	}
	
	/// Diese Methode fügt der listView die ObservableList "items" hinzu.
	///
	///
	public void setupSectionListView() {

		folders = Utility.sharedInstance.findFoldersInDirectory("Quizis");

		groups = FXCollections.observableArrayList(folders);

		selectSectionView.setItems(groups);

	}

	/// Diese Methode Konfiguriert die QuizListView. Wenn man auf eine Gruppe klickt, wird der Gruppennamen in eine Variable gespeichert.
	///
	///
	public void setupQuizListView() {

		// Es wurde ein Listener hinzugefügt, der schaut, auf welche Zeile in der ListView gedrückt wurde.
		selectSectionView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					backButton.setDisable(true);
					
					// Diese Variable muss zurückgesetzt werden, da sie sonst den Wert des Vorherigen Quizes hat
					countQuestion = 0;
					
					// In dieser Variable steht der Ordername drin
					String selectedItem = selectSectionView.getSelectionModel().getSelectedItem();
					folderName = selectedItem;
					
					// In der ArrayList stehen alle Files drin, die in der Gruppe stehen wo drauf geklickt wurde
					filesOfFolder = Utility.sharedInstance.getFilesFromFolder("Quizis/" + selectedItem);

					// Alle files werden in die observableArrayList geladen
					files = FXCollections.observableArrayList(filesOfFolder);

					selectQuizView.setItems(files);

				});

	}

	/// Diese Methode gibt uns ein bestimmtes Quiz was wir bearbeiten möchten
	///
	///
	public void getQuizFromListView() {

		selectQuizView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					
					String selectedItem = selectQuizView.getSelectionModel().getSelectedItem();
					
					backButton.setDisable(true);
					nextButton.setDisable(false);
					
					countQuestion = 0;
					
					// Hier steht die Anzahl der Fragen drin.
					// Es muss -1 gerechnet werden, da wir mit der tatsächlichen Anzahl von Quizes arbeiten.
					int sizeOfQuestionPackage = questionPackages.size() - 1;
					
					if (countQuestion < sizeOfQuestionPackage) {
						nextButton.setText("weiter");
					}
					

					// Instanz von dem object "ObjectMapper" erstellen.
					var reader = new ObjectMapper();

					// Wenn es ein Quiz gibt soll folgende Anweisung ausgeführt werden.
					if (!folders.isEmpty()) {

						// Wenn Antwortmöglichkeiten in der answerVBox stehen, soll er die Answeisung ausführen.
						if (answerVBox.getChildren().size() > 0) {
							removeAnswersFromVBox();
						}

						ArrayList<QAModel> model;
						
						// Dieser try catch Block ist notwendig, da es ein Fehler geben kann, das Programm aber trotzdem noch weiter laufen soll
						try {
							// Hier wird die .json Datei in eine ArrayList gespeichert.
							model = reader.readValue(new File("Quizis/" + folderName + "/" + selectedItem + ".json"),
									new TypeReference<ArrayList<QAModel>>() {
									});

							// Hier wird jede Frage in eine HashMap gespeicehrt, die wir später brauchen, um die richtigen Antworten einer Frag zu bekommen
							for (QAModel qaModel : model) {
								answersOfQuestionsHashedMap.put(qaModel.getQuestion(), null);
							}

							questionPackages = model;
							
							// Methoden aufrufen
							setQuestionText();
							addRightAnswer();
							addWrongAnswer();

						} catch (JsonParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonMappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

	}
	
	/// Diese Methode setzt die Frage in der questionTextArea.
	///
	///
	private void setQuestionText() {

		questionTextArea.setText(questionPackages.get(countQuestion).getQuestion());

	}
	
	/// Diese Methode ist dafür zuständig die richtige Frage zurückzugeben, die man gerade beantworten möchte
	///
	/// return question: gibt ein String zurück, der die Frage beinhaltet
	private String getQuestionText() {
		
		String question = questionPackages.get(countQuestion).getQuestion();
		
		return question;
	}
	
	/// Diese Methode guckt anhand des QuestionPacke was vom typ QAModel ist, welche Fragen richtig sind und fügt eine neue Node in der answerVBoc hinzu
	///
	///
	private void addRightAnswer() {
		// Hier drin stehen NUR die richtigen Antworten
		ArrayList<String> rightAnswers = questionPackages.get(countQuestion).getRightAnswers();
		
		for (String answer : rightAnswers) {
			
			FXMLLoader loader = new FXMLLoader();
			
			// Es wird versucht die FXML Datei der rightAnswers zu laden. Dies kann schiefgehen, deswegen der try catch Block.
			try {
				Node node = loader.load(getClass().getResource("rightAnswerFXML.fxml").openStream());
				
				// Wenn man ein Node zurückbekommen hat, wird dieser der answerVBox hinzugefügt.
				answerVBox.getChildren().add(node);
				
				// Dies ist eine Referenz zur Klasse RightAnswerController
				RightAnswerController controller = (RightAnswerController) loader.getController();
				
				// Anhand der Referenz, können wir auf die Methoden zugreifen und z.B. den text füllen.
				controller.setContent(answer); 
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/// Diese Methode guckt anhand des QuestionPacke was vom typ QAModel ist, welche Fragen falsch sind und fügt eine neue Node in der answerVBoc hinzu
	///
	///
	private void addWrongAnswer() {
		// Hier drin stehen NUR die falschen Antworten
		ArrayList<String> wrongAnswers = questionPackages.get(countQuestion).getWrongAnswers();
		for (String answer : wrongAnswers) {
			FXMLLoader loader = new FXMLLoader();

			try {
				Node node = loader.load(getClass().getResource("wrongAnswerFXML.fxml").openStream());
				answerVBox.getChildren().add(node);
				WrongAnswerController controller = (WrongAnswerController) loader.getController();
				controller.setContent(answer); 
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	
	/// In dieser Methode bekommen wir alle Checkboxen mit ihren Werten zurück.
	///
	///
	private void getCheckBoxValues() {

		ArrayList<String> answers = new ArrayList<String>();
		
		// Da in der answerVBoc mehrere Node drin stecken und nur die Checkboxen haben wollen, müssen wir durch jede Schicht einzeln durchgehen.
		for (int i = 0; i < answerVBox.getChildren().size(); i++) {
			Node node = answerVBox.getChildren().get(i);
			// Um die erste Node zu bekommen müssen wir eine Typumwandlung zur HBox machen
			if (node instanceof HBox) {
				for (Node anchorNode : ((HBox) node).getChildren()) {
					// Da die HBox wiederrum ein Node enthält, das vom Typ anchorPane ist, müssen wir wieder ein Typumwandlung durchführen.
					if (anchorNode instanceof AnchorPane) {
						for (Node checkNode : ((AnchorPane) anchorNode).getChildren()) {
							// Im anchorPane gibt es wiederum verschiedene Nodes. Da wir die Checkbox benötigen, benutzen wir wieder instanceOf für eine Typumwandlung
							if (checkNode instanceof CheckBox) {
								Boolean checkBoolean = ((CheckBox) checkNode).isSelected();
								// Wenn die CheckBox, die wir angeklickt haben auf true ist wird folgende Answeisung ausgeführt.
								if (checkBoolean) {
									for (Node textNode : ((AnchorPane) anchorNode).getChildren()) {
										if (textNode instanceof TextArea) {
											// Hier bekommen wir den Text von der textArea das wir ausgewählt haben
											answers.add(((TextArea) textNode).getText());
											// Die Antwortmöglichkeit wird ein eine Hashmap gespeichert.
											answersOfQuestionsHashedMap.put(questionPackages.get(countQuestion).getQuestion(), answers );
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	/// Diese Methode setzt die CheckBox Values auf den Wert den wir vorher ausgewählt haben, wenn wir im Quiz auf dem back Button klicken.
	///
	///
	private void setCheckBoxValues() {
		
		// Hier drin steht die Frage die gerade zu beantworten ist.
		String question = getQuestionText();
		ArrayList<String> values = new ArrayList<String>();
		
		// Wir benötigen jeden Key aus der HashMap, deswegen durchlaufen wir die HashMap mit einer forEach Schleife
		for (String key : answersOfQuestionsHashedMap.keySet()) {
			// Wenn der key gleichd er Question ist, die oben in der questionTextAre steht, bekommen wir alle Antworten, die wir vorher ausgewählt haben
			if (key.equals(question)) {
				values.addAll(answersOfQuestionsHashedMap.get(key));
			}
		}
		
		for (int i = 0; i < answerVBox.getChildren().size(); i++) {
			Node node = answerVBox.getChildren().get(i);
			if (node instanceof HBox) {
				for (Node anchorNode : ((HBox) node).getChildren()) {
					if (anchorNode instanceof AnchorPane) {
						for (Node checkNode : ((AnchorPane) anchorNode).getChildren()) {
							if (checkNode instanceof CheckBox) {
								for (Node textNode : ((AnchorPane) anchorNode).getChildren()) {
									if (textNode instanceof TextArea) {
										for (String value : values) {
											// Wenn der text von value den Text von der HBox entspricht, wird die CheckBox auf true gesetzt
											if (value.equals(((TextArea) textNode).getText())) {
												((CheckBox) checkNode).setSelected(true);
											}
										}
									}
								}						
							}
						}
					}
				}
			}
		}
	}
	
	
	/// Diese Methode wertet alle Antwortmöglichkeiten aus und gibt uns am Ende ein Feedback zurpck was wir falsch oder richtig beanwortet haben.
	///
	/// Parameter answers: Hier bekommen wir eine HashMap übergeben mit den fertigen Fragen
	public void evaluateAnswers(HashedMap<String, ArrayList<String>> answers) {

		int doneRight = 0;
		int wrongAnswers = 0;
		int checkMultipleAnswers = 0;
		
		
		// ForEach Schleife, die ein questionpackage enthält
		for (QAModel question : questionPackages) {
			
			// Hier drin steht von dem questionpacke die Frage
			String tempQuestion = question.getQuestion();
			
			// Hier drin stehen die richtigen Anwortmöglichkeiten
			ArrayList<String> rightAnswers = question.getRightAnswers();
			
			for (String key : answersOfQuestionsHashedMap.keySet()) {
				
				// Wenn die tempQuestion gleich der Frage aus der HashMap ist, wird die Anweisung ausgeführt
				if (tempQuestion.equals(key)) {
					
					// Wenn im die größe der beiden ArrayListen gleich ist, wird die Anweisung ausgeführt. Andernfalls wissen wir schon, das die Frage falsch beantwortet wurde
					if (rightAnswers.size() == answersOfQuestionsHashedMap.get(key).size()) {
						
							for (String answersOfQuestionsHashedMapRightAnswers : answersOfQuestionsHashedMap.get(key)) {
								
								// Wenn rightAnswers eine richtige Antwort von answersOfQuestionsHashedMap.get(key) beinhält, wir die Variable in der Anweisung um 1 inkrementiert.
								// Andernfalls wird das Programm unterbrochen, da die ganze Frage falsch ist und wronAnswers wir dum 1 hochgezählt
								if (rightAnswers.contains(answersOfQuestionsHashedMapRightAnswers)) {
									checkMultipleAnswers++;
								} else {
									wrongAnswers++;
									checkMultipleAnswers = 0;
									break;
								}
								
								// Wenn der Wert gleich ist, wird doneRight um 1 hochgezählt und die Frage wurde richtig beantwortet
								if (checkMultipleAnswers == rightAnswers.size()) {
									doneRight++;
									checkMultipleAnswers = 0;
								}
								
							}
					} else {
						wrongAnswers++;
						checkMultipleAnswers = 0;
					}
				}
			}
		}
		
		FXMLLoader loader = new FXMLLoader();
		
		// Hier wird die resultFXML Datei geladen um das Ergebnis in einer TextFieldArea anzuzeigen.
		try {
			Node node = loader.load(getClass().getResource("resultFXML.fxml").openStream());
			answerVBox.getChildren().add(node);
			ResultController controller = (ResultController) loader.getController();
			controller.setContent("Du hast " + doneRight + " Frage(n) richtig beantwortet." + "\n" + "Du hast " + wrongAnswers + " Frage(n) falsch beantwortet.");
			questionTextArea.setText("Ergebnis: ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		// Anschließend wird das Ergebnis auch nochmal in der Konosle ausgegeben
		System.out.println("Du hast " + doneRight + " Frage(n) richtig beantwortet.");
		System.out.println("Du hast " + wrongAnswers + " Frage(n) falsch beantwortet.");
		
		
	}
	
	
	// MARK: - FXML Content
	
	@FXML
	private Button createButton;

	@FXML
	void createButtonTapped(ActionEvent event) {

		try {
			Parent root = FXMLLoader.load(getClass().getResource("uploadWDPopupFXML.fxml"));
			Scene createQuizScene = new Scene(root, 300, 170);
			primaryStage.setScene(createQuizScene);
			primaryStage.show();
			primaryStage.setResizable(false);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@FXML
	private ListView<String> selectSectionView;

	@FXML
	private ListView<String> selectQuizView;

	@FXML
	private Button refreshButton;

	// Diese Methode sorgt daf�r das die PrimaryStage neu geladen wird damit
	// ListView refreshed wird.
	@FXML
	void refreshButtonTapped(ActionEvent event) throws IOException {

		

		//getCheckBoxValues();
//		for (String key : answersOfQuestionsHashedMap.keySet()) {
//			System.out.print("Key:" + key);
//			System.out.println("Value: " + answersOfQuestionsHashedMap.get(key));
//		}
		// groups.clear();
		// etupSectionListView();

	}

	@FXML
	private TextArea questionTextArea;

	@FXML
	private ScrollPane answerScrollPane;

	@FXML
	private VBox answerVBox;

	

	@FXML
	private Button nextButton;

	@FXML
	void nextButtonTapped(ActionEvent event) {
		
		
		// bekommt immer die CheckBox Values von der vorherigen Frage
		getCheckBoxValues();
		
		
		// Wenn der Text des Buttons auf Fertig ist, dann wird der folgende Anweisungsblock durchgeführt
		if (nextButton.getText().equals("Fertig")) {
			
			int sizeOfAnswerdQuestions = answersOfQuestionsHashedMap.keySet().size();
			int countQuestions = 0;
			
			// Hier wird überprüft ob auch jede Frage beantwortet wurde
			for (String key : answersOfQuestionsHashedMap.keySet()) {
				System.out.print(key);
				// Wenn die Antworten einer Frage != null sind, wird count Question um ein hochgezählt. Ansonsten wird das Programm abgebrochen
				if (answersOfQuestionsHashedMap.get(key) != null) {
					countQuestions++;
				} else { 
					return;
				}
			}
			
				// Hier wird überprüft, ob die Variablen gleich sind. Erst wenn beide gleich sind bekommen wir ein Ergbnis.
				if (sizeOfAnswerdQuestions == countQuestions) {
					nextButton.setDisable(false);
					removeAnswersFromVBox();
					evaluateAnswers(answersOfQuestionsHashedMap);
					return;
				} else {
					nextButton.setDisable(true);
					return;
				}
		}

		
		removeAnswersFromVBox();

		int sizeOfQuestionPackage = questionPackages.size() - 1;
		countQuestion++;

		
		if (countQuestion > 0) {
			backButton.setDisable(false);
			setQuestionText();
			addWrongAnswer();
			addRightAnswer();
		}
		
		String tempQuestion = questionPackages.get(countQuestion).getQuestion();
		
		
		for (String key : answersOfQuestionsHashedMap.keySet()) {
			
			if (tempQuestion.equals(key)) {
				// Wenn die Frage schon in der HashMap steht, werden die Values der CheckBox gesetzt
				if (answersOfQuestionsHashedMap.get(key) != null) {
					setCheckBoxValues();
				}
			}
		}
		
		// Wenn die Größe erreicht wurde, wird der Text des Button geändert
		if (countQuestion == sizeOfQuestionPackage) {
			nextButton.setText("Fertig");
		}
		

		
	}

	@FXML
	private Button backButton;

	@FXML
	void backButtonTapped(ActionEvent event) {
		
		getCheckBoxValues();

		removeAnswersFromVBox();

		if (countQuestion > 0) {
			countQuestion--;
			setQuestionText();
			addRightAnswer();
			addWrongAnswer();

			nextButton.setDisable(false);
			
			if (countQuestion == 0) {
				backButton.setDisable(true);
			}
		}
		
		String tempQuestion = questionPackages.get(countQuestion).getQuestion();
		for (String key : answersOfQuestionsHashedMap.keySet()) {
			if (tempQuestion.equals(key)) {
				if (answersOfQuestionsHashedMap.get(key) != null) {
					setCheckBoxValues();
				}
			}
		}
		
		int sizeOfQuestionPackage = questionPackages.size() - 1;
		
		// solange countQuestion kleiner ist, steht "weiter" im Button
		if (countQuestion < sizeOfQuestionPackage) {
			nextButton.setText("weiter");
		}

	}

}