package com.QuiziApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class ResultController {

    @FXML
    private HBox resultHBox;

    @FXML
    private TextArea resultTextArea;
    
    public void setContent(String txt) {
    	resultTextArea.setText(txt);
    	
    }

}