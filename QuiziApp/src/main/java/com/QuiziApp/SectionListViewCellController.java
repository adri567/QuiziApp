package com.QuiziApp;

import java.io.IOException;

import com.QuiziApp.Models.SectionModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class SectionListViewCellController extends ListCell<SectionModel> {

	// Properties

	public void initialize() {
		System.out.print("Test");
	}

	@FXML
	private HBox Hbox;

	@FXML
	private FXMLLoader mLLoader;

	// Diese Methode lädt die "sectionFXML" Datei und gibt die Custom Cell in der
	// ListView auf der Startseite aus.
	@Override
	protected void updateItem(SectionModel section, boolean empty) {
		super.updateItem(section, empty);

		setPrefWidth(0);

		if (empty || section == null) {

			setText(null);

		} else {

			if (mLLoader == null) {

				mLLoader = new FXMLLoader(getClass().getResource("sectionFXML.fxml"));
				mLLoader.setController(this);

				try {
					mLLoader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			sectionName.setText(section.getSectionName());
			setText(null);
			// Setzt die Hbox in den Vordergrund
			setGraphic(Hbox);

		}

	}

	@FXML
	private Label sectionName;

}
