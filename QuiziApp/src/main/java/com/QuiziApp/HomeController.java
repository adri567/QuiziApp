package com.QuiziApp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


import com.QuiziApp.HelperClasses.Utility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
	// Methods

	// Diese Methode wird bei jedem Programmstart als erstes aufgerufen
	public void initialize() throws JsonParseException, JsonMappingException, IOException, InvocationTargetException {
		System.out.print("init");
		setupSectionListView();
		setupQuizListView();
		for (int i = 0; i < 3; i++) {
			addRightAnswer();
			addWrongAnswer();
		}

		
//		selectQuizView.setCellFactory(new Callback<ListView<SectionModel>, ListCell<SectionModel>>() {
//			@Override
//			public ListCell<SectionModel> call(ListView<SectionModel> sectionListView) {
//				return new SectionListViewCellController();
//			}
//		});

		// Instanz von dem object "ObjectMapper" erstellen.
		//var reader = new ObjectMapper();

		// Konvertiert die json datei in eine ArrayList.
//		if (!folders.isEmpty()) {
//			ArrayList<QAModel> model = reader.readValue(new File("Quizis/Chemie/asadasd.json"),
//					new TypeReference<ArrayList<QAModel>>() {
//					});
//
//			for (QAModel qaModel : model) {
//				System.out.println(qaModel.getQuestion());
//				System.out.println(qaModel.getRightAnswers());
//				System.out.println(qaModel.getWrongAnswers());
//			}
//
//		}
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

		//ArrayList<SectionModel> sections = new ArrayList<SectionModel>();

//		for (String folder : folders) {
//			sections.add(new SectionModel(folder));
//		}

		groups = FXCollections.observableArrayList(folders); 

		selectSectionView.setItems(groups);

		//selectQuizView.setCellFactory(sectionCell -> new SectionListViewCellController());

		
	}
	
	@FXML
	private ListView<String> selectQuizView;
	
	public void setupQuizListView() {
		
		
		 selectSectionView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
		    String selectedItem = selectSectionView.getSelectionModel().getSelectedItem();
		     
		    filesOfFolder = Utility.sharedInstance.getFilesFromFolder("Quizis/" + selectedItem);
				
				
			files = FXCollections.observableArrayList(filesOfFolder);

			selectQuizView.setItems(files);
		     
		    });

	} 
	
	
	@FXML
    private Button refreshButton;
	
	// Diese Methode sorgt daf�r das die PrimaryStage neu geladen wird damit ListView refreshed wird.
	@FXML
	  void refreshButtonTapped(ActionEvent event) throws IOException {
		
		groups.clear();
		setupSectionListView();
		
	  }
	

	@FXML
    private TextArea questionTextView;
	
    @FXML
    private ScrollPane answerScrollPane;
    
    @FXML
    private VBox answerVBox;
    
	private void addRightAnswer() {
		FXMLLoader loader = new FXMLLoader();
        try {
            Node node  =  loader.load(getClass().getResource("rightAnswerFXML.fxml").openStream());
            answerVBox.getChildren().add(node);
            //get the controller 
            RightAnswerController controller = (RightAnswerController)loader.getController();
            controller.setContent("This is a test"); //set label 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	private void addWrongAnswer() {
		FXMLLoader loader = new FXMLLoader();
        try {
            Node node  =  loader.load(getClass().getResource("wrongAnswerFXML.fxml").openStream());
            answerVBox.getChildren().add(node);
            //get the controller 
           WrongAnswerController controller = (WrongAnswerController)loader.getController();
            controller.setContent("This is a wrong test"); //set label 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}


    @FXML
    private Button nextButton;

    @FXML
    void backButtonTapped(ActionEvent event) {

    }

    @FXML
    private Button backButton;

    @FXML
    void nextButtonTapped(ActionEvent event) {

    }

 
 
	
	
	

}
