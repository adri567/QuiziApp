package com.QuiziApp;

import java.io.IOException;

import com.QuiziApp.Models.SectionModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class SectionListViewCellController extends ListCell<SectionModel> {

	// Properties
	
	public void initialize() {
		System.out.print("Test");
	}
	
	 public SectionListViewCellController() {
	        loadFXML();
	    }

	    private void loadFXML() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("sectionFXML.fxml"));
	            loader.setController(this);
	            loader.setRoot(this);
	            loader.load();
	        }
	        catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }
	
	    @Override
	    protected void updateItem(SectionModel section, boolean empty) {
	        super.updateItem(section, empty);

	   
	            sectionName.setText(section.getSectionName());
	        
	    }
	
	
    @FXML
    private Label sectionName;

}
