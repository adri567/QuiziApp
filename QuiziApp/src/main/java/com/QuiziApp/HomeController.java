package com.QuiziApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.hwpf.sprm.SectionSprmCompressor;
import org.junit.validator.PublicClassValidator;

import com.QuiziApp.HelperClasses.Utility;
import com.QuiziApp.Models.QAModel;
import com.QuiziApp.Models.SectionModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HomeController {

	// Properties
	Stage primaryStage = new Stage();
	ArrayList<String> folders = new ArrayList<String>();
	ArrayList<String> filesOfFolder = new ArrayList<String>();
	ObservableList<String> groups;
	ObservableList<String> files;
	// Methods

	// Diese Methode wird bei jedem Programmstart als erstes aufgerufen
	public void initialize() throws JsonParseException, JsonMappingException, IOException {
		System.out.print("init");
		setupSectionListView();
		setupQuizListView();

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

}
