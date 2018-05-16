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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SelecThemeController {
	
	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	@FXML
	Button modifier;
	@FXML
	Button supprimer;
	@FXML
	ListView<Label> listeThemes;
	
	@FXML
	public void initialize() {
		modifier.setDisable(true);
		supprimer.setDisable(true);
		List<Theme> themes = /*Get from BD*/ new ArrayList<Theme> ();
		/*A SUPPRIMER*/themes.add(new Theme("Nom du theme ..."));
		/*A SUPPRIMER*/themes.add(new Theme("Nom du theme 2 ..."));
		for(Theme theme : themes) {
			Label lblTheme = new Label(theme.getNom());
			lblTheme.setAlignment(Pos.CENTER);
			lblTheme.setTextAlignment(TextAlignment.CENTER);
			lblTheme.setFont(Font.font(Main.POLICE,12));
			listeThemes.getItems().add(lblTheme);
			System.out.println(theme.getNom()+" "+listeThemes.getItems().size());
		}
		Scaler.updateSize(Main.width,vbox);
	}
	
	@FXML
	public void selectionTheme() {
		if(listeThemes.getSelectionModel().getSelectedItem()!=null) {
			modifier.setDisable(false);
			supprimer.setDisable(false);
		}
	}
	
	@FXML
	public void supprimerTheme() {
		listeThemes.getItems().remove(listeThemes.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	public void gotoEditTheme() {
		Theme themeTmp = new Theme(listeThemes.getSelectionModel().getSelectedItem().getText());
		List<Double> points = new ArrayList<Double>();
		List<Double> points2 = new ArrayList<Double>();
		points.add(50.0);
		points.add(20.0);
		points.add(30.0);
		points.add(40.0);
		points.add(10.0);
		points.add(60.0);
		points.add(10.0);
		points.add(20.0);
		points2.add(90.0);
		points2.add(80.0);
		points2.add(40.0);
		points2.add(80.0);
		points2.add(80.0);
		points2.add(60.0);
		themeTmp.addZone(new Zone(1, points));
		themeTmp.addZone(new Zone(3, points2));
		List<Integer> ids = new ArrayList<Integer> ();
		ids.add(new Integer(1));
		themeTmp.addQuestion(new Question("texte Question N°1 ... ?", themeTmp.getZonesWithIDs(ids)));
		themeTmp.addQuestion(new Question("texte Question N°2 ... ?", new ArrayList<Zone>()));
		editionQuestionsZonesController.setThemeAModifier(themeTmp);

		editionQuestionsZonesController.primaryStage = primaryStage;
		try {
			VBox root = null;
			root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
			Scene scene = new Scene(root,Main.width,Main.height);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void annuler() {
		AccueilController.primaryStage = primaryStage;
		VBox root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("view/Editeur - Accueil.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root,Main.width,Main.height);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
