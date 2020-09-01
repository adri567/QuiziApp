package com.QuiziApp;

	import javafx.fxml.FXML;
	import javafx.scene.control.CheckBox;
	import javafx.scene.control.TextArea;
	import javafx.scene.layout.HBox;

public class WrongAnswerController {

	    @FXML
	    private HBox wrongAnswerHBox;

	    @FXML
	    private CheckBox wrongCheckBox;

	    @FXML
	    private TextArea wrongAnswerTextArea;
	    
	    
	    public void setContent(String txt) {
	    	wrongAnswerTextArea.setText(txt);
	    	wrongAnswerTextArea.setMinHeight(wrongAnswerTextArea.getText().length() / 4);
	    }

	

}