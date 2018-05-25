package application.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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

	static int idZoneMax;

	Font fontLblUpSize;

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
	Button changerImage;
	@FXML
	TextField titreTheme;

	@FXML
	public void initialize() {
		fontLblUpSize = Font.font(Main.POLICE, 12);
		System.out.println(idZoneMax + "      zone zone nb Max");
		titreTheme.setText(themeAModifier.getNom());
		modifierQuestion.setDisable(true);
		supprimerQuestion.setDisable(true);
		modifierZone.setDisable(true);
		supprimerZone.setDisable(true);
		setListeQuestions(themeAModifier.getQuestions());
		setListeZonesWithQuestion(null);
		Scaler.updateSize(Main.width, vbox);
		fontLblUpSize = listeQuestions.getItems().get(0).getFont();
	}

	@FXML
	public void changerImage() throws IOException {
		FileChooser explorateur = new FileChooser();
		explorateur.setTitle("Changer l'image de fond");
		List<String> extensions = new ArrayList<String>();
		extensions.add("*.png");
		extensions.add("*.jpg");
		extensions.add("*.jpeg");
		extensions.add("*.bmp");
		explorateur.getExtensionFilters().add(new ExtensionFilter("Toutes les images ...", extensions));
		File image = explorateur.showOpenDialog(primaryStage);
		if (image != null) {
			File copieData = new File("./src/application/data/" + image.getName());
			try (InputStream sourceFile = new java.io.FileInputStream(image);
					OutputStream destinationFile = new FileOutputStream(copieData)) {
				byte buffer[] = new byte[512 * 1024];
				int nbLecture;
				while ((nbLecture = sourceFile.read(buffer)) != -1) {
					destinationFile.write(buffer, 0, nbLecture);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			themeAModifier.setImageFond(new Image(new FileInputStream(copieData.getAbsolutePath())));
			themeAModifier.setUrlImage(copieData.getName());
		}
	}

	@FXML
	public void selectionQuestion() {
		if (listeQuestions.getSelectionModel().getSelectedItem() != null)
			if (!listeQuestions.getSelectionModel().getSelectedItem().getText().equals("Aucune ...")) {
				modifierQuestion.setDisable(false);
				supprimerQuestion.setDisable(false);
				for (Question question : themeAModifier.getQuestions())
					if (question.getIntitule().equals(listeQuestions.getSelectionModel().getSelectedItem().getText()))
						setListeZonesWithQuestion(question);
			} else {
				setListeZonesWithQuestion(null);
				modifierQuestion.setDisable(true);
				supprimerQuestion.setDisable(true);
			}
	}

	@FXML
	public void selectionZone() {
		if (listeZones.getSelectionModel().getSelectedItem() != null) {
			modifierZone.setDisable(false);
			supprimerZone.setDisable(false);
		} else {
			modifierZone.setDisable(true);
			supprimerZone.setDisable(true);
		}
	}

	@FXML
	public void supprimerQuestion() {
		Label qstLbl = listeQuestions.getSelectionModel().getSelectedItem();
		if (qstLbl != null) {
			Question qstTmp = null;
			for (Question qst : themeAModifier.getQuestions())
				if (qst.getIntitule().equals(qstLbl.getText()))
					qstTmp = qst;
			themeAModifier.getQuestions().remove(qstTmp);
			setListeQuestions(themeAModifier.getQuestions());
			selectionQuestion();
		}
	}

	@FXML
	public void supprimerZone() {
		Label zoneLbl = listeZones.getSelectionModel().getSelectedItem();
		if (zoneLbl != null) {
			Zone zoneTmp = null;
			for (Zone zone : themeAModifier.getZones())
				if (zone.toString().equals(zoneLbl.getText()))
					zoneTmp = zone;
			themeAModifier.getZones().remove(zoneTmp);
			Question repTmp = null;
			for (Question qst : themeAModifier.getQuestions())
				for (Zone rep : qst.getReponses())
					if (rep == zoneTmp) {
						qst.getReponses().remove(rep);
						break;
					}
			Question qstTmp = null;
			if (listeQuestions.getSelectionModel().getSelectedItem() != null)
				for (Question qst : themeAModifier.getQuestions())
					if (qst.getIntitule().equals(listeQuestions.getSelectionModel().getSelectedItem().getText()))
						qstTmp = qst;
			setListeZonesWithQuestion(qstTmp);
			selectionZone();
		}
	}

	private void setListeZonesWithQuestion(Question question) {
		listeZones.getItems().clear();
		if (question != null)
			for (Zone zone : question.getReponses()) {
				Label lblTheme = new Label(zone.toString());
				lblTheme.setAlignment(Pos.CENTER);
				lblTheme.setTextAlignment(TextAlignment.CENTER);
				lblTheme.setFont(fontLblUpSize);
				listeZones.getItems().add(lblTheme);
				System.out.println(zone.toString() + " " + listeZones.getItems().size());
			}
		else
			for (Zone zone : themeAModifier.getZones()) {
				Label lblTheme = new Label(zone.toString());
				lblTheme.setAlignment(Pos.CENTER);
				lblTheme.setTextAlignment(TextAlignment.CENTER);
				lblTheme.setFont(fontLblUpSize);
				listeZones.getItems().add(lblTheme);
				System.out.println(zone.toString() + " " + listeZones.getItems().size());
			}
	}

	private void setListeQuestions(List<Question> questions) {
		listeQuestions.getItems().clear();
		Label lblTheme = new Label("Aucune ...");
		lblTheme.setAlignment(Pos.CENTER);
		lblTheme.setTextAlignment(TextAlignment.CENTER);
		lblTheme.setFont(fontLblUpSize);
		listeQuestions.getItems().add(lblTheme);
		if (questions != null)
			for (Question qst : questions) {
				lblTheme = new Label(qst.getIntitule());
				lblTheme.setAlignment(Pos.CENTER);
				lblTheme.setTextAlignment(TextAlignment.CENTER);
				lblTheme.setFont(fontLblUpSize);
				listeQuestions.getItems().add(lblTheme);
				System.out.println(qst.getIntitule() + " " + listeQuestions.getItems().size());
			}
		System.out.println(listeQuestions.getItems().size());
	}

	@FXML
	public void gotoCreerQuestion() {
		if (themeAModifier.getImageFond() != null) {
			Question newQ = new Question("");
			themeAModifier.addQuestion(newQ);
			editionCreationQuestionsController.setQuestion(newQ);

			editionCreationQuestionsController.primaryStage = primaryStage;
			try {
				VBox root = null;
				root = FXMLLoader.load(getClass().getResource("creationEditionQuestions.fxml"));
				Scene scene = new Scene(root, Main.width, Main.height);
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			changerImage.setStyle("-fx-border-color: red;-fx-border-color: red;-fx-border-width: 2px;");
	}

	@FXML
	public void gotoEditQuestion() {
		if (themeAModifier.getImageFond() != null) {
			Question qSelect = null;
			for (Question question : themeAModifier.getQuestions())
				if (question.getIntitule().equals(listeQuestions.getSelectionModel().getSelectedItem().getText()))
					qSelect = question;
			System.out.println(qSelect + " : " + qSelect.getIntitule());
			editionCreationQuestionsController.setQuestion(qSelect);

			editionCreationQuestionsController.primaryStage = primaryStage;
			try {
				VBox root = null;
				root = FXMLLoader.load(getClass().getResource("creationEditionQuestions.fxml"));
				Scene scene = new Scene(root, Main.width, Main.height);
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			changerImage.setStyle("-fx-border-color: red;-fx-border-color: red;-fx-border-width: 2px;");
	}

	@FXML
	public void gotoCreerZone() {
		if (themeAModifier.getImageFond() != null) {
			Zone newZ = new Zone(idZoneMax);
			idZoneMax++;
			themeAModifier.addZone(newZ);
			EditionCreationZonesController.setZone(newZ);

			EditionCreationZonesController.primaryStage = primaryStage;
			try {
				VBox root = null;
				root = FXMLLoader.load(getClass().getResource("Creation Zone.fxml"));
				Scene scene = new Scene(root, Main.width, Main.height);
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			changerImage.setStyle("-fx-border-color: red;-fx-border-color: red;-fx-border-width: 2px;");
	}

	@FXML
	public void gotoEditZone() {
		if (themeAModifier.getImageFond() != null) {
			Zone zSelect = null;
			for (Zone zone : themeAModifier.getZones())
				if (zone.toString().equals(listeZones.getSelectionModel().getSelectedItem().getText()))
					zSelect = zone;
			System.out.println(zSelect + " : " + zSelect.getIndex());
			EditionCreationZonesController.setZone(zSelect);

			EditionCreationZonesController.primaryStage = primaryStage;
			try {
				VBox root = null;
				root = FXMLLoader.load(getClass().getResource("Creation Zone.fxml"));
				Scene scene = new Scene(root, Main.width, Main.height);
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			changerImage.setStyle("-fx-border-color: red;-fx-border-color: red;-fx-border-width: 2px;");
	}

	@FXML
	public void sauvegarder() throws IOException, SQLException {
		System.out.println(themeAModifier.getUrlImage());
		Main.bdd.executeUpdateCmd("DELETE FROM POINT WHERE ID_ZONE IN (SELECT ID_ZONE FROM ZONE WHERE NOM_THEME='"
				+ themeAModifier.getNom() + "');");
		Main.bdd.executeUpdateCmd("DELETE FROM REPONSE WHERE ID_ZONE IN (SELECT ID_ZONE FROM ZONE WHERE NOM_THEME='"
				+ themeAModifier.getNom() + "');");
		Main.bdd.executeUpdateCmd("DELETE FROM ZONE WHERE NOM_THEME='" + themeAModifier.getNom() + "';");
		Main.bdd.executeUpdateCmd("DELETE FROM QUESTION WHERE NOM_THEME='" + themeAModifier.getNom() + "';");
		Main.bdd.executeUpdateCmd("DELETE FROM THEME WHERE NOM_THEME='" + themeAModifier.getNom() + "';");
		// AJOUTER QUERY POUR SAUVEGARDER
		Main.bdd.executeUpdateCmd(
				"INSERT INTO THEME VALUES ('" + themeAModifier.getNom() + "','" + themeAModifier.getUrlImage() + "');");
		ResultSet qustsDB = Main.bdd.executeQueryCmd("SELECT MAX(ID_QUESTION) FROM QUESTION;");
		int i = 0;
		if (qustsDB.next()) {
			System.out.println(qustsDB.getInt("MAX(ID_QUESTION)"));
			i = qustsDB.getInt("MAX(ID_QUESTION)");
		}
		int k = i;
		for (Question qst : themeAModifier.getQuestions()) {
			Main.bdd.executeUpdateCmd("INSERT INTO QUESTION VALUES (" + (++i) + ",'" + qst.getIntitule() + "','"
					+ themeAModifier.getNom() + "');");
			System.out.println("INSERT INTO QUESTION VALUES (" + (i) + ",'" + qst.getIntitule() + "','"
					+ themeAModifier.getNom() + "');");
		}
		ResultSet pointsDB = Main.bdd.executeQueryCmd("SELECT MAX(ID_POINT) FROM POINT;");
		i = 0;
		if (pointsDB.next()) {
			i = pointsDB.getInt("MAX(ID_POINT)");
		}
		for (Zone zone : themeAModifier.getZones()) {
			Main.bdd.executeUpdateCmd(
					"INSERT INTO ZONE VALUES (" + zone.getIndex() + ",'" + themeAModifier.getNom() + "');");
			System.out.println(zone.getPoints().size());
			for (int l = 0; l < zone.getPoints().size(); l += 2)
				Main.bdd.executeUpdateCmd("INSERT INTO POINT VALUES (" + (++i) + "," + zone.getPoints().get(l) + ","
						+ zone.getPoints().get(l + 1) + "," + zone.getIndex() + ");");
		}
		ResultSet repsDB = Main.bdd.executeQueryCmd("SELECT MAX(ID_REP) FROM REPONSE;");
		int j = 0;
		if (repsDB.next()) {
			j = repsDB.getInt("MAX(ID_REP)");
		}
		for (Question qst : themeAModifier.getQuestions()) {
			k++;
			for (Zone zone : qst.getReponses())
				Main.bdd.executeUpdateCmd(
						"INSERT INTO REPONSE VALUES (" + (++j) + "," + k + "," + zone.getIndex() + ");");
		}

		VBox root = new VBox();

		SelecThemeController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("selectionnerTheme.fxml"));
		Scene scene = new Scene(root);

		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
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
