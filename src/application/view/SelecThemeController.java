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
		ResultSet themesBD = Main.bdd.executeQueryCmd("SELECT * FROM THEME;");
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
	public void supprimerTheme() throws SQLException {
		if (listeThemes.getSelectionModel().getSelectedItem() != null) {
			String nomThemeSupp = listeThemes.getSelectionModel().getSelectedItem().getText();
			listeThemes.getItems().remove(listeThemes.getSelectionModel().getSelectedItem());
			System.out.println(nomThemeSupp.replace("'", "''"));
			Main.bdd.executeUpdateCmd("DELETE FROM POINT WHERE ID_ZONE IN (SELECT ID_ZONE FROM ZONE WHERE NOM_THEME='"
					+ nomThemeSupp.replace("'", "''") + "');");
			Main.bdd.executeUpdateCmd("DELETE FROM REPONSE WHERE ID_ZONE IN (SELECT ID_ZONE FROM ZONE WHERE NOM_THEME='"
					+ nomThemeSupp.replace("'", "''") + "');");
			Main.bdd.executeUpdateCmd("DELETE FROM ZONE WHERE NOM_THEME='" + nomThemeSupp.replace("'", "''") + "';");
			Main.bdd.executeUpdateCmd("DELETE FROM QUESTION WHERE NOM_THEME='" + nomThemeSupp.replace("'", "''") + "';");
			Main.bdd.executeUpdateCmd("DELETE FROM THEME WHERE NOM_THEME='" + nomThemeSupp.replace("'", "''") + "';");
		}
	}

	@FXML
	public void gotoEditTheme() {
		ResultSet zonesDB;
		try {
			zonesDB = Main.bdd.executeQueryCmd("SELECT MAX(ID_ZONE) FROM ZONE;");
			editionQuestionsZonesController.idZoneMax = 1;
			if (zonesDB.next()) {
				editionQuestionsZonesController.idZoneMax = zonesDB.getInt("MAX(ID_ZONE)") + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		editionQuestionsZonesController.setThemeAModifier(chargerTheme(listeThemes.getSelectionModel().getSelectedItem().getText()));

		try {
			VBox root = null;
			root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
			Scene scene = new Scene(root, Main.width, Main.height);
			Main.primaryStage.setResizable(false);
			Main.primaryStage.setScene(scene);
			Main.primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Theme chargerTheme(String theme) {
		Theme themeTmp = new Theme(theme);
		try {
			ResultSet zonesBD;
			zonesBD = Main.bdd.executeQueryCmd("SELECT ID_ZONE FROM ZONE WHERE NOM_THEME='"
					+ theme.replace("'", "''") + "';");
			while (zonesBD.next()) {
				Zone zoneTmp = new Zone(zonesBD.getInt("ID_ZONE"));
				ResultSet pointsBD = Main.bdd
						.executeQueryCmd("SELECT POS_X,POS_Y FROM POINT WHERE ID_ZONE=" + zonesBD.getInt("ID_ZONE") + ";");
				while (pointsBD.next()) {
					zoneTmp.getPoints().add(pointsBD.getDouble("POS_X"));
					zoneTmp.getPoints().add(pointsBD.getDouble("POS_Y"));
				}
				themeTmp.addZone(zoneTmp);
			}
			ResultSet questionsBD;
			questionsBD = Main.bdd.executeQueryCmd("SELECT * FROM QUESTION WHERE NOM_THEME='"
					+ theme.replace("'", "''") + "';");
			while (questionsBD.next()) {
				Question questionTmp = new Question(questionsBD.getString("INTITULE_QUESTION"));
				ResultSet repsBD = Main.bdd.executeQueryCmd(
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
			ResultSet themeBD = Main.bdd.executeQueryCmd("SELECT URL_IMAGE FROM THEME WHERE NOM_THEME='"+themeTmp.getNom().replace("'", "''")+"';");
			themeBD.next();
			if (themeBD.getString("URL_IMAGE")!=null) {
				Image imgFond;
				System.out.println("../data/" + themeBD.getString("URL_IMAGE"));
				imgFond = new Image(new FileInputStream(
						"./src/application/data/" + themeBD.getString("URL_IMAGE")));
				themeTmp.setImageFond(imgFond);
				themeTmp.setUrlImage(themeBD.getString("URL_IMAGE"));
			}
		} catch (FileNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return themeTmp;
	}

	@FXML
	public void annuler() {
		VBox root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("Editeur - Accueil.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root, Main.width, Main.height);

		Main.primaryStage.setResizable(false);

		Main.primaryStage.setScene(scene);
		Main.primaryStage.show();
	}
}
