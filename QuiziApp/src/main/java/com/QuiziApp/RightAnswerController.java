package com.QuiziApp;

	import javafx.fxml.FXML;
	import javafx.scene.control.CheckBox;
	import javafx.scene.control.TextArea;
	import javafx.scene.layout.HBox;

public class RightAnswerController {

	    @FXML
	    private HBox rightAnswerHBox;

	    @FXML
	    private CheckBox rightCheckBox;

	    @FXML
	    private TextArea rightAnswerTextArea;
	    
	    
	    public void setContent(String txt) {
	    	rightAnswerTextArea.setText(txt);
	    	rightAnswerTextArea.setMinHeight(rightAnswerTextArea.getText().length() / 4);
	    }

	

}
