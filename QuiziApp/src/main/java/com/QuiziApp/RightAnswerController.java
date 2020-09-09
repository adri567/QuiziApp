package com.QuiziApp;

	import javafx.fxml.FXML;
	import javafx.scene.control.CheckBox;
	import javafx.scene.control.TextArea;
	import javafx.scene.layout.HBox;
import junit.framework.Test;

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
	    
	    public boolean getValueOfRightCheckBox() {
	    	
	    	System.out.println(rightCheckBox.isSelected());
	    	
	    	return rightCheckBox.isSelected();
	    }

	

}
