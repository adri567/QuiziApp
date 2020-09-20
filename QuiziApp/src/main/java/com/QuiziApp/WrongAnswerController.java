package com.QuiziApp;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

/**
 * 
 * 
 *  Diese Klasse erstellt eine Box mit einer CheckBox und einer TextArea.
 * 
 * 
 * 
 * @author Jannik Walter
 * @version 2.5
 * @since 01.09.2020
 * 
 *        Copyright © 2020 Jannik Walter und Adrian Suthold. All rights reserved.
 */

public class WrongAnswerController {

	    @FXML
	    private HBox wrongAnswerHBox;

	    @FXML
	    private CheckBox wrongCheckBox;

	    @FXML
	    private TextArea wrongAnswerTextArea;
	    
	    /// Diese Methode setzt den Text der textArea
	    ///
	    /// parameter txt: hier steht der Text drin
	    public void setContent(String txt) {
	    	wrongAnswerTextArea.setText(txt);
	    	wrongAnswerTextArea.setMinHeight(wrongAnswerTextArea.getText().length() / 4);
	    }

	

}