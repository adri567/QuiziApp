package com.QuiziApp.HelperClasses;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class Utility {
	
	
	private Utility() {}
	
	public static final Utility sharedInstance = new Utility();
	
	public ArrayList<String> findFoldersInDirectory(String directoryPath) {
	    File directory = new File(directoryPath);
		
	    FileFilter directoryFileFilter = new FileFilter() {
	        public boolean accept(File file) {
	            return file.isDirectory();
	        }
	    };
			
	    File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
	    ArrayList<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
	    for (File directoryAsFile : directoryListAsFile) {
	        foldersInDirectory.add(directoryAsFile.getName());
	    }

	    return foldersInDirectory;
	}


}
