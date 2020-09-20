package com.QuiziApp.HelperClasses;

/**
* 
* Die Klasse ConvertWordDocToText wird benötigt, um den Text von einer Word Datei
* zu bekommen. Mit Hilfe der Library Apache POI ist dies möglich.
* 
* @author  Adrian Suthold
* @version 1.0
* @since   08.06.2020 
* 
* Copyright © 2020 Adrian Suthold. All rights reserved.
*/

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class ConvertWordDocToText {

	// MARK: - Properties
	private XWPFDocument wordDocx;
	private XWPFWordExtractor wordDocumentText;
	private String wordText;

	/*
	 * Methode die den Text aus einem File bekommt. Es kann ein Fehler
	 * zurückgeworfen werden, da das umwandeln nicht immer klappen kann.
	 */
	public String getText(File file) throws Exception {

		// Word File wird hier eingelesen
		wordDocx = new XWPFDocument(new FileInputStream(file));
		wordDocumentText = new XWPFWordExtractor(wordDocx);

		// Text vom Worddokument
		wordText = wordDocumentText.getText();

		// Word text wird zurückgegeben
		return wordText;

	}

}
