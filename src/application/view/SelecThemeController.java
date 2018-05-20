package application.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.image.Image;
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
	public void initialize() throws SQLException {
		modifier.setDisable(true);
		supprimer.setDisable(true);
		List<String> themes = /* Get from BD */ new ArrayList<String>();
		ResultSet themesBD = Main.bdd.executeCmd("SELECT * FROM THEME;");
		while (themesBD.next()) {
			System.out.println(themesBD.getString("NOM_THEME"));
			themes.add(themesBD.getString("NOM_THEME"));
		}
		for (String theme : themes) {
			Label lblTheme = new Label(theme);
			lblTheme.setAlignment(Pos.CENTER);
			lblTheme.setTextAlignment(TextAlignment.CENTER);
			lblTheme.setFont(Font.font(Main.POLICE, 12));
			listeThemes.getItems().add(lblTheme);
			System.out.println(theme + " " + listeThemes.getItems().size());
		}
		Scaler.updateSize(Main.width, vbox);
	}

	@FXML
	public void selectionTheme() {
		if (listeThemes.getSelectionModel().getSelectedItem() != null) {
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
		try {
			ResultSet zonesBD;
			zonesBD = Main.bdd.executeCmd("SELECT ID_ZONE FROM ZONE WHERE NOM_THEME='"
					+ listeThemes.getSelectionModel().getSelectedItem().getText() + "';");
			while (zonesBD.next()) {
				Zone zoneTmp = new Zone(zonesBD.getInt("ID_ZONE"));
				ResultSet pointsBD = Main.bdd
						.executeCmd("SELECT POS_X,POS_Y FROM POINT WHERE ID_ZONE=" + zonesBD.getInt("ID_ZONE") + ";");
				while (pointsBD.next()) {
					zoneTmp.getPoints().add(pointsBD.getDouble("POS_X"));
					zoneTmp.getPoints().add(pointsBD.getDouble("POS_Y"));
				}
				themeTmp.addZone(zoneTmp);
			}
			ResultSet questionsBD;
			questionsBD = Main.bdd.executeCmd("SELECT * FROM QUESTION WHERE NOM_THEME='"
					+ listeThemes.getSelectionModel().getSelectedItem().getText() + "';");
			while (questionsBD.next()) {
				Question questionTmp = new Question(questionsBD.getString("INTITULE_QUESTION"));
				ResultSet repsBD = Main.bdd.executeCmd(
						"SELECT ZONE.ID_ZONE FROM REPONSE INNER JOIN ZONE ON REPONSE.ID_ZONE=ZONE.ID_ZONE WHERE ID_QUESTION="
								+ questionsBD.getInt("ID_QUESTION") + ";");
				while (repsBD.next()) {
					Zone zoneRep = themeTmp.getZoneWithID(repsBD.getInt("ID_ZONE"));
					if (zoneRep != null)
						questionTmp.getReponses().add(zoneRep);
				}
				themeTmp.addQuestion(questionTmp);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ResultSet themeBD = Main.bdd.executeCmd("SELECT URL_IMAGE FROM THEME WHERE NOM_THEME='"+themeTmp.getNom()+"';");
			themeBD.next();
			if (themeBD.getString("URL_IMAGE")!=null) {
				Image imgFond;
				System.out.println(getClass().getResource("../data/" + themeBD.getString("URL_IMAGE")).getPath());
				imgFond = new Image(new FileInputStream(
						getClass().getResource("../data/" + themeBD.getString("URL_IMAGE")).getPath()));
				themeTmp.setImageFond(imgFond);
			}
		} catch (FileNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		editionQuestionsZonesController.setThemeAModifier(themeTmp);

		editionQuestionsZonesController.primaryStage = primaryStage;
		try {
			VBox root = null;
			root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
			Scene scene = new Scene(root, Main.width, Main.height);
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
		Scene scene = new Scene(root, Main.width, Main.height);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
