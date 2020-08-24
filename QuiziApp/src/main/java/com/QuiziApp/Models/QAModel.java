package com.QuiziApp.Models;

import java.util.ArrayList;

/**
 * 
 * Die Klasse QAModel wird benötigt, um die Fragen und Antworten im Programm zu
 * nutzen.
 * 
 * @author Adrian Suthold
 * @version 1.0
 * @since 08.06.2020
 * 
 *        Copyright © 2020 Adrian Suthold. All rights reserved.
 */

public class QAModel {

	// Variablen für die Frage, richtige und falsche Antworten
	String question = new String();
	ArrayList<String> rightAnswers = new ArrayList<String>();
	ArrayList<String> wrongAnswers = new ArrayList<String>();

	public QAModel() {
	}

	// Frage bekommen
	public String getQuestion() {
		return question;
	}

	// Frage setzen
	public void setQuestion(String question) {
		this.question = question;
	}

	// richtige Antwort bekommen
	public ArrayList<String> getRightAnswers() {
		return rightAnswers;
	}

	// richtige Antwort setzen
	public void setRightAnswer(String answer) {
		rightAnswers.add(answer);
	}

	// falsche Antwort bekommen
	public ArrayList<String> getWrongAnswers() {
		return wrongAnswers;
	}

	// falsche Antwort setzen
	public void setWrongAnswer(String answer) {
		wrongAnswers.add(answer);
	}

	public QAModel toModel(ArrayList<QAModel> qamodel) {
		var model = new QAModel();
		model.question = qamodel.get(0).getQuestion();
		model.rightAnswers.addAll(qamodel.get(0).rightAnswers);
		model.wrongAnswers.addAll(qamodel.get(0).wrongAnswers);
		return model;
	}

}
