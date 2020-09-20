package com.QuiziApp;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

/**
 * 
 * Diese Klasse fügt eine TextArea hinzu.
 * 
 * 
 * @author Jannik Walter
 * @version 2.5
 * @since 13.09.2020
 * 
 *        Copyright © 2020 Jannik Walter. All rights reserved.
 */

public class ResultController {

    @FXML
    private HBox resultHBox;

    @FXML
    private TextArea resultTextArea;
    
    
    /// Diese Methode setzt den Text der textArea
    ///
    /// parameter txt: hier steht der Text drin
    public void setContent(String txt) {
    	resultTextArea.setText(txt);
    	
    }

}