package application.view;

import application.Main;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class editionQuestionsZonesController {
	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		System.out.println(vbox.getPrefWidth()+"x"+vbox.getPrefHeight());
	}
}
