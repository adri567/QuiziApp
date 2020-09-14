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

public class HomeController {

	// Properties
	Stage primaryStage = new Stage();
	ArrayList<String> folders = new ArrayList<String>();
	ArrayList<String> filesOfFolder = new ArrayList<String>();
	ObservableList<String> groups;
	ObservableList<String> files;
	ArrayList<QAModel> questionPackages = new ArrayList<QAModel>();
	HashedMap<String, ArrayList<String>> answersOfQuestionsHashedMap = new HashedMap<String, ArrayList<String>>();
	int countQuestion = 0;

	// String question = new String();
	// ArrayList<String> rightAnswerArrayList = new ArrayList<String>();
	// ArrayList<String> wrongAnswerArrayList = new ArrayList<String>();

	// Methods

	// Diese Methode wird bei jedem Programmstart als erstes aufgerufen
	public void initialize() {
		System.out.print("init");

		backButton.setDisable(true);
		nextButton.setDisable(true);
		
		setupSectionListView();
		setupQuizListView();
		getQuizFromListView();

	}

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

	// Diese Methode f�gt der listView die ObservableList "items" hinzu.
	public void setupSectionListView() {

		folders = Utility.sharedInstance.findFoldersInDirectory("Quizis");

		// ArrayList<SectionModel> sections = new ArrayList<SectionModel>();

		// for (String folder : folders) {
		// sections.add(new SectionModel(folder));
		// }

		groups = FXCollections.observableArrayList(folders);

		selectSectionView.setItems(groups);

		// selectQuizView.setCellFactory(sectionCell -> new
		// SectionListViewCellController());

	}

	@FXML
	private ListView<String> selectQuizView;

	private String folderName;

	public void setupQuizListView() {
		System.out.print(selectQuizView.getItems().size());

		selectSectionView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					backButton.setDisable(true);
					//nextButton.setDisable(false);
					countQuestion = 0;
					String selectedItem = selectSectionView.getSelectionModel().getSelectedItem();
					folderName = selectedItem;
					filesOfFolder = Utility.sharedInstance.getFilesFromFolder("Quizis/" + selectedItem);

					files = FXCollections.observableArrayList(filesOfFolder);

					selectQuizView.setItems(files);

				});

	}

	public void getQuizFromListView() {

		selectQuizView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					backButton.setDisable(true);
					nextButton.setDisable(false);
					countQuestion = 0;
					int sizeOfQuestionPackage = questionPackages.size() - 1;
					if (countQuestion < sizeOfQuestionPackage) {
						nextButton.setText("weiter");
					}
					String selectedItem = selectQuizView.getSelectionModel().getSelectedItem();

					// Instanz von dem object "ObjectMapper" erstellen.
					var reader = new ObjectMapper();

					// Konvertiert die json datei in eine ArrayList.
					if (!folders.isEmpty()) {

						if (answerVBox.getChildren().size() > 0) {
							removeAnswersFromVBox();
						}

						ArrayList<QAModel> model;
						try {
							model = reader.readValue(new File("Quizis/" + folderName + "/" + selectedItem + ".json"),
									new TypeReference<ArrayList<QAModel>>() {
									});

							for (QAModel qaModel : model) {
								answersOfQuestionsHashedMap.put(qaModel.getQuestion(), null);
							}

							questionPackages = model;
							
							
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

	public void setQuestionText() {

		questionTextArea.setText(questionPackages.get(countQuestion).getQuestion());

	}
	
	public String getQuestionText() {
		
		String question = questionPackages.get(countQuestion).getQuestion();
		
		return question;
	}

	@FXML
	private ScrollPane answerScrollPane;

	@FXML
	private VBox answerVBox;

	private void addRightAnswer() {
		ArrayList<String> rightAnswers = questionPackages.get(countQuestion).getRightAnswers();
		for (String answer : rightAnswers) {
			FXMLLoader loader = new FXMLLoader();
			try {
				Node node = loader.load(getClass().getResource("rightAnswerFXML.fxml").openStream());
				answerVBox.getChildren().add(node);
				// get the controller
				RightAnswerController controller = (RightAnswerController) loader.getController();
				controller.setContent(answer); // set label
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void addWrongAnswer() {
		ArrayList<String> wrongAnswers = questionPackages.get(countQuestion).getWrongAnswers();
		for (String answer : wrongAnswers) {
			FXMLLoader loader = new FXMLLoader();

			try {
				Node node = loader.load(getClass().getResource("wrongAnswerFXML.fxml").openStream());
				answerVBox.getChildren().add(node);
				// get the controller
				WrongAnswerController controller = (WrongAnswerController) loader.getController();
				controller.setContent(answer); // set label
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void getCheckBoxValues() {

		ArrayList<String> answers = new ArrayList<String>();
		for (int i = 0; i < answerVBox.getChildren().size(); i++) {
			Node node = answerVBox.getChildren().get(i);
			if (node instanceof HBox) {
				for (Node anchorNode : ((HBox) node).getChildren()) {
					if (anchorNode instanceof AnchorPane) {
						for (Node checkNode : ((AnchorPane) anchorNode).getChildren()) {
							if (checkNode instanceof CheckBox) {
								Boolean checkBoolean = ((CheckBox) checkNode).isSelected();
								if (checkBoolean) {
									for (Node textNode : ((AnchorPane) anchorNode).getChildren()) {
										if (textNode instanceof TextArea) {
											answers.add(((TextArea) textNode).getText());
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
	
	private void setCheckBoxValues() {
		
		String question = getQuestionText();
		ArrayList<String> values = new ArrayList<String>();
		
		for (String key : answersOfQuestionsHashedMap.keySet()) {
			if (key.equals(question)) {
				values.addAll(answersOfQuestionsHashedMap.get(key));
			}
		}
		int index = 0;
		
		for (int i = 0; i < answerVBox.getChildren().size(); i++) {
			Node node = answerVBox.getChildren().get(i);
			if (node instanceof HBox) {
				for (Node anchorNode : ((HBox) node).getChildren()) {
					if (anchorNode instanceof AnchorPane) {
						for (Node checkNode : ((AnchorPane) anchorNode).getChildren()) {
							if (checkNode instanceof CheckBox) {
								for (Node textNode : ((AnchorPane) anchorNode).getChildren()) {
									if (textNode instanceof TextArea) {
										//System.out.print(((TextArea) textNode).getText());
										for (String value : values) {
											if (value.equals(((TextArea) textNode).getText())) {
												((CheckBox) checkNode).setSelected(true);
												index++;
											}
										}
									}
//								((CheckBox) checkNode).setSelected(values.get(index));
//								index++;
//								System.out.print(((CheckBox) checkNode).isSelected());
//								System.out.println(answersOfQuestionsHashedMap);
//								for (String key : answersOfQuestionsHashedMap.keySet()) {
//									if (key.equals(question)) {
//										System.out.print(answersOfQuestionsHashedMap.get(key));
//									}
								}
								
							}
						}
					}
				}
			}
		}
	}
	
	
	public void evaluateAnswers(HashedMap<String, ArrayList<String>> answers) {
		
		
		
		int doneRight = 0;
		int wrongAnswers = 0;
		int checkMultipleAnswers = 0;
		
		
		for (QAModel question : questionPackages) {
			String tempQuestion = question.getQuestion();
			ArrayList<String> rightAnswers = question.getRightAnswers();
			
			for (String key : answersOfQuestionsHashedMap.keySet()) {
				if (tempQuestion.equals(key)) {
					
					
					if (rightAnswers.size() == answersOfQuestionsHashedMap.get(key).size()) {
							for (String answersOfQuestionsHashedMapRightAnswers : answersOfQuestionsHashedMap.get(key)) {
								if (rightAnswers.contains(answersOfQuestionsHashedMapRightAnswers)) {
									checkMultipleAnswers++;
								} else {
									wrongAnswers++;
									checkMultipleAnswers = 0;
									break;
								}
								
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
		try {
			Node node = loader.load(getClass().getResource("resultFXML.fxml").openStream());
			answerVBox.getChildren().add(node);
			// get the controller
			ResultController controller = (ResultController) loader.getController();
			controller.setContent("Du hast " + doneRight + " Frage(n) richtig beantwortet." + "\n" + "Du hast " + wrongAnswers + " Frage(n) falsch beantwortet.");
			questionTextArea.setText("Ergebnis: ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("Du hast " + doneRight + " Frage(n) richtig beantwortet.");
		System.out.println("Du hast " + wrongAnswers + " Frage(n) falsch beantwortet.");
		
		
	}

	@FXML
	private Button nextButton;

	@FXML
	void nextButtonTapped(ActionEvent event) {
		
		getCheckBoxValues();
		
		if (nextButton.getText().equals("Fertig")) {
			removeAnswersFromVBox();
			evaluateAnswers(answersOfQuestionsHashedMap);
			
			return;
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
				if (answersOfQuestionsHashedMap.get(key) != null) {
					setCheckBoxValues();
				}
			}
		}
		
		
		
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

			System.out.print(countQuestion);
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
		
		if (countQuestion < sizeOfQuestionPackage) {
			nextButton.setText("weiter");
		}

	}

	private void removeAnswersFromVBox() {
		int size = answerVBox.getChildren().size();
		questionTextArea.setText("");
		for (int i = 0; i < size; i++) {
			answerVBox.getChildren().remove(0);
		}
	}

}