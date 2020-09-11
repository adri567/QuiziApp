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
import com.sun.javafx.collections.MappingChange.Map;
import com.sun.javafx.scene.web.Printable;

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
import javafx.scene.shape.Box;
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
					nextButton.setDisable(false);
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

		

		test();
		for (String key : answersOfQuestionsHashedMap.keySet()) {
			System.out.print("Key:" + key);
			System.out.println("Value: " + answersOfQuestionsHashedMap.get(key));
		}
		// groups.clear();
		// etupSectionListView();

	}

	@FXML
	private TextArea questionTextArea;

	public void setQuestionText() {

		questionTextArea.setText(questionPackages.get(countQuestion).getQuestion());

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

	void test() {

		ArrayList<String> answerStrings = new ArrayList<String>();
		for (int i = 0; i < answerVBox.getChildren().size(); i++) {
			Node node = answerVBox.getChildren().get(i);
			// String answerId = answerVBox.getChildren().get(i).getId();
			if (node instanceof HBox) {
				for (Node anchorNode : ((HBox) node).getChildren()) {
					if (anchorNode instanceof AnchorPane) {
						for (Node checkNode : ((AnchorPane) anchorNode).getChildren()) {
							if (checkNode instanceof CheckBox) {
								Boolean checkBoolean = ((CheckBox) checkNode).isSelected();
								if (checkBoolean) {
									for (Node textNode : ((AnchorPane) anchorNode).getChildren()) {
										if (textNode instanceof TextArea) {
											answerStrings.add(((TextArea) textNode).getText());
											
											answersOfQuestionsHashedMap.put(questionPackages.get(countQuestion).getQuestion(), answerStrings );
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

	@FXML
	private Button nextButton;

	@FXML
	void nextButtonTapped(ActionEvent event) {

		removeAnswersFromVBox();

		int sizeOfQuestionPackage = questionPackages.size() - 1;
		countQuestion++;

		if (countQuestion > 0) {
			backButton.setDisable(false);
			setQuestionText();
			addWrongAnswer();
			addRightAnswer();
		}

		if (countQuestion == sizeOfQuestionPackage) {
			nextButton.setDisable(true);
		}

	}

	@FXML
	private Button backButton;

	@FXML
	void backButtonTapped(ActionEvent event) {

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

	}

	private void removeAnswersFromVBox() {
		int size = answerVBox.getChildren().size();
		questionTextArea.setText("");
		for (int i = 0; i < size; i++) {
			answerVBox.getChildren().remove(0);
		}
	}

}
