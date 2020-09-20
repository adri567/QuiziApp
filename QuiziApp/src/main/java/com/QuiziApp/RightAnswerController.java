package com.QuiziApp;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import junit.framework.Test;


/**
 * 
 * Diese Klasse erstellt eine Box mit einer CheckBox und einer TextArea.
 * 
 * 
 * 
 * @author Jannik Walter
 * @version 2.5
 * @since 01.09.2020
 * 
 *        Copyright © 2020 Jannik Walter. All rights reserved.
 */

public class RightAnswerController {

	    @FXML
	    private HBox rightAnswerHBox;

	    @FXML
	    private CheckBox rightCheckBox;

	    @FXML
	    private TextArea rightAnswerTextArea;
	    
	    
	    /// Diese Methode setzt den Text der textArea
	    ///
	    /// parameter txt: hier steht der Text drin 
	    public void setContent(String txt) {
	    	rightAnswerTextArea.setText(txt);
	    	rightAnswerTextArea.setMinHeight(rightAnswerTextArea.getText().length() / 4);
	    }
	    

	

}
