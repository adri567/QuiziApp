package com.QuiziApp.HelperClasses;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class Utility {

	private Utility() {
	}

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
	
	public ArrayList<String> getFilesFromFolder(String directoryPath){
		
		File folder = new File(directoryPath);
		File[] listOFiles = folder.listFiles();
		ArrayList<String> files = new ArrayList<String>();
		for (File file : listOFiles) {
			int endOfFileName = file.getName().lastIndexOf(".");
			if (endOfFileName > 0) {
				files.add(file.getName().substring(0, endOfFileName));				
			}
			
			
		}
		
		return files;
	}

}
