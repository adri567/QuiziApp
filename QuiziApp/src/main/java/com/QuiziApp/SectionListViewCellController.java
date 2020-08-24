package com.QuiziApp;

import java.io.IOException;

import com.QuiziApp.Models.SectionModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class SectionListViewCellController extends ListCell<SectionModel> {

	// Properties
	private FXMLLoader mlLoader;
	
	protected void updateItem(SectionModel section) throws IOException {
		
		mlLoader = FXMLLoader.load(getClass().getResource("sectionFXML.fxml"));
		sectionName.
	}
	
	
	
    @FXML
    private Label sectionName;

}
