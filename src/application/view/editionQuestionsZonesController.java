package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.gestionThemes.Question;
import application.gestionThemes.Theme;
import application.gestionThemes.Zone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class editionQuestionsZonesController {
	
	public static Stage primaryStage;
	private static Theme themeAModifier;
	
	public static Theme getThemeAModifier() {
		return themeAModifier;
	}

	public static void setThemeAModifier(Theme themeAModifier) {
		editionQuestionsZonesController.themeAModifier = themeAModifier;
	}

	@FXML
	VBox vbox;
	@FXML
	ListView<Label> listeQuestions;
	@FXML
	ListView<Label> listeZones;
	@FXML
	Button modifierQuestion;
	@FXML
	Button supprimerQuestion;
	@FXML
	Button modifierZone;
	@FXML
	Button supprimerZone;
	
	@FXML
	public void initialize() {
		modifierQuestion.setDisable(true);
		supprimerQuestion.setDisable(true);
		modifierZone.setDisable(true);
		supprimerZone.setDisable(true);
		for(Question question : themeAModifier.getQuestions()) {
			Label lblTheme = new Label(question.getIntitule());
			lblTheme.setAlignment(Pos.CENTER);
			lblTheme.setTextAlignment(TextAlignment.CENTER);
			lblTheme.setFont(Font.font(Main.POLICE,12));
			listeQuestions.getItems().add(lblTheme);
			System.out.println(question.getIntitule()+" "+listeQuestions.getItems().size());
		}
		for(Zone zone : themeAModifier.getZones()) {
			Label lblTheme = new Label(zone.toString());
			lblTheme.setAlignment(Pos.CENTER);
			lblTheme.setTextAlignment(TextAlignment.CENTER);
			lblTheme.setFont(Font.font(Main.POLICE,12));
			listeZones.getItems().add(lblTheme);
			System.out.println(zone.toString()+" "+listeZones.getItems().size());
		}
		Scaler.updateSize(Main.width,vbox);
	}
	
	@FXML
	public void quitter() throws IOException {
		VBox root = new VBox();

		SelecThemeController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("selectionnerTheme.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
	}
}
