package com.QuiziApp.HelperClasses;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
* 
* Die Klasse Utility ist eine Helfer Klasse und benutzt das Singleton Pattern (Einzelstück).
* Es wird sichergestellt, das von der Klasse nur ein Objelt erstellt wird.
* 
* 
* @author  Adrian Suthold
* @version 1.0
* @since   23.08.2020 
* 
* Copyright © 2020 Adrian Suthold. All rights reserved.
*/

public class Utility {

	private Utility() {
	}

	
	public static final Utility sharedInstance = new Utility();

	
	/// Diese Methode wird dazu benötigt um ein Ordner in einem Directory zu finden.
	///
	/// Parameter directoryPath: in diese Variable steht der Ort an dem der Ordner liegt im String Format
	public ArrayList<String> findFoldersInDirectory(String directoryPath) {
		File directory = new File(directoryPath);

		// Wenn der file der gefunden wurde ein Directory ist, wird dieser dann zurückgegeben
		FileFilter directoryFileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};

		// Hier stehen die directorys drin
		File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
		
		ArrayList<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
		
		// Diese For Schleife fügt der ArrayListe den directory Namen hinzu
		for (File directoryAsFile : directoryListAsFile) {
			foldersInDirectory.add(directoryAsFile.getName());
		}

		return foldersInDirectory;
	}
	
	/// Diese Methode gibt uns die Datein aus einem Ordner zurück.
	///
	/// parameter directoryPath: in diese Variable steht der Ort an dem der Ordner liegt im String Format
	public ArrayList<String> getFilesFromFolder(String directoryPath){
		
		File folder = new File(directoryPath);
		File[] listOFiles = folder.listFiles();
		
		ArrayList<String> files = new ArrayList<String>();
		
		// Wir iterieren durch den Ordner und wollen alles Files bekommen
		for (File file : listOFiles) {
			// Alles was nach dem Punkt kommt wird weggeschnitten. Der Punkt wird auch mit abgeschnitten.
			int endOfFileName = file.getName().lastIndexOf(".");
			
			if (endOfFileName > 0) {
				files.add(file.getName().substring(0, endOfFileName));				
			}
			
			
		}
		
		return files;
	}

}
