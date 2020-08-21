package com.QuiziApp.HelperClasses;
import java.util.ArrayList;
import java.util.List;

import com.QuiziApp.Models.QAModel;

/**
* 
* Die Klasse QAModel wird benötigt, um die Fragen und Antworten im
* Programm zu nutzen.
* 
* @author  Adrian Suthold
* @version 1.0
* @since   08.06.2020 
* 
* Copyright © 2020 Adrian Suthold. All rights reserved.
*/


public class WordsearchAlgorithm {

	// Variablen Marks

	//private char[] endOfQuestionPackage = { '{', 'e', 'n', 'd', 'q', '}'}; 
	
	final private String endOfQuestionPackage = "{endq}";
	final private String startOfQuestion = "{q}";
	final private String endOfQuestion = "{/q}";
	final private String startOfAnswer = "{a}";
	final private String endOfRightAnswer = "{/ta}";
	final private String endOfWrongAnswer = "{/fa}";
	//private String[] findMarks = { startOfQuestion, endOfQuestion, startOfAnswer, endOfRightAnswer, endOfWrongAnswer };

	
	// Zählvariable für die Anzahl an Werten in der ArrayList
	private int placeInList = 0;
	
	//String[] findSymbols = { "{q}", "{/q}", "{a}", "{/ta}", "{/fa}" };
	

	/*
	 * Diese Methode Filter die richtigen Fragen und Antworten aus einem Question Package
	 */
//	public ArrayList<QAModel> extractQuestionPackage(String text) {
//		
//		String wordText = text;
//		ArrayList<QAModel> questions = filterQuestionsFromPackage(wordText);
//		
//		return questions;
// 	}
	
	/*
	 * Diese Methode Filtert aus den gegebenen QuestionPackage alle Fragen und antworten die zusammengehören
	 */
	public ArrayList<QAModel> filterQuestionsFromPackage(String pack) {
		
		// Der übergebene String wird zum charArray gemacht
		char[] charArray = pack.toCharArray();
		StringBuilder converter = new StringBuilder(pack);
		
		// Diese Variable ist dafür da, damit immer an der richtigen Stelle das Leerzeichen einsortiert wird
		int append = 0;
		
		// Diese Schleife schaut, ob vor der { ein Leerzeichen ist. Wenn nicht, wird eins eingefügt.
			for (int i = 0; i < charArray.length; i++) {
				if (charArray[i] == '{' && charArray[i - 1] != ' ' ) {
					converter.insert(i + append , " ");
					append += 1;
				}
			}
		
		
		// In dieser Variable befindet sich das questionPackage
		String questionPackage = converter.toString();
		
		// Diese ArrayList beinhaltet am Ende alle sortierten Fragen
		ArrayList<QAModel> finalPackage = new ArrayList<QAModel>();
		
		//int counter = 0;
//		int countRightAnswer = 0;
//		int countWrongAnswer = 0;
		
		// Temporäre Variable, die ein String beinhält
		ArrayList<String> temp = new ArrayList<String>();
		
		// Diese Variable beinhaltet ein fertigen String, der mit dem StringBuffer nochmal überarbeitet wurde
		String result = new String();
		
		// In dieser Variable befindet sich eine Frage mit den passenden Antworten
		QAModel singlePackage = new QAModel();
		
		// Hier werden alle Absätze durch eine Leertaste ersetzt, damit es später nicht zu formatierungsfehlern kommt.
		String str = questionPackage.replaceAll("\n", " ");
		String[] splitted = str.split(" ");
		
		// Hier wird gezählt wie viele questionPackages es gibt
		for (String word : splitted) {
			if (word.contentEquals(endOfQuestionPackage)) {
				placeInList += 1;
			}
		}
		
	
			// Diese Switch case Answeisung filtert die richtigen Fragen und Antworten raus
			for (String word : splitted) {
				switch (word) {
				case startOfQuestion:
					temp = new ArrayList<String>();
					break;
				case endOfQuestion: 
					result = createString(temp);
					singlePackage.setQuestion(result);
					break;
				case startOfAnswer: 
					temp = new ArrayList<String>();
					break;
				case endOfRightAnswer:
					result = createString(temp);
					singlePackage.setRightAnswer(result);
					//countRightAnswer += 1;
					break;
				case endOfWrongAnswer:
					result = createString(temp);
					singlePackage.setWrongAnswer(result);
					//countWrongAnswer += 1;
					break;
				case endOfQuestionPackage: 
					finalPackage.add(singlePackage);
					singlePackage = new QAModel();
					//counter += 1;
				default:
					temp.add(word);
					break;
				}			
			}
		
		return finalPackage;
		
	}
	
	private String createString(ArrayList<String> pack) {
		
		StringBuffer stringBuffer = new StringBuffer();
		for (String element : pack){
		    stringBuffer.append(element).append(" ");
		}
		String result = stringBuffer.toString();
		
		
		return result;
	}

	
//	/*
//	 * Diese Methode Filtert die Fragen mit den Antworten aus dem gesammten Text herraus. 
//	 * Das Schlüsselwort {endq} wird dafür verwendet.  
//	 */
//	private List<String> filterQuestionPackage(String text) {
//		
//		// In dieser Variable steht der Text in Form eines char Arrays drin
//		char[] wordtextInCharArray = text.toCharArray();
//		
//		// Zählvariable für die länge eines Arrays
//		int length = 0;		
//		
//		// temporäre Variable, wo die marks drin gespeichert werden
//		StringBuilder temp = new StringBuilder();	
//		
//		// Variable für die fertigen fragen
//		StringBuilder question = new StringBuilder();	
//		
//		// ArrayList für die fertigen Fragen
//		List<String> questionPackage = new ArrayList<String>();
//		
//		
//		// jedes zeichen wird druchlaufen
//		for (int i = 0; i < wordtextInCharArray.length; i++) {		
//			
//			// es wird überprüft, ob das Zeichen aus dem Text, gleich den Zeichen von dem Mark ist. 
//			// Zudem wird so lange in die if-Abfrage eingegangen, solange length < 6 ist.
//			if (wordtextInCharArray[i] == endOfQuestionPackage[length] && length < 6) {
//				
//				// die Mark Zeichen werden temporär gespeichert
//				temp.append(wordtextInCharArray[i]);
//				
//				// wird jedes mal um eins hochgezählt
//				length++;
//				
//				// die fertige Frage wird in der ArrayList gespeichert
//				questionPackage.add(placeInList, question.toString());
//				
//				// wenn length == 6 ist, werden alle Variable auf null gesetzt und playInList immer um eins hochgezählt
//				if (length == 6) {
//					length = 0;
//					placeInList++;
//					question = new StringBuilder();
//					temp = new StringBuilder();
//				} 
//			} else {
//				
//				// Wenn die temporäre Variable ungleich null ist, dann wird die if-Anweisung ausgeführt 
//				if (temp.length() != 0) {
//					
//					// temporäre char Variable wird erstellt
//					char[] tempChar = temp.toString().toCharArray();
//					
//					// alle Zeichen werden der Variable question eingefügt
//					for(char c : tempChar) {
//						question.append(c);
//					}
//					
//					// die temporäre Variable und die Variable length werden auf null gestezt
//					temp = new StringBuilder();
//					length = 0;
//				}
//				
//				// Die restlichen Zeichen werden hier eingefügt
//				question.append(wordtextInCharArray[i]);
//				
//			}
//			
//		}
//		
//		return questionPackage;
//		
//	}

	
}
