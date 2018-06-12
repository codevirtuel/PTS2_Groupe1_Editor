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
import javafx.scene.image.ImageView;
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

	private static Theme themeAModifier;

	public static Theme getThemeAModifier() {
		return themeAModifier;
	}

	public static void setThemeAModifier(Theme themeAModifier) {
		editionQuestionsZonesController.themeAModifier = themeAModifier;
	}

	public static int idZoneMax;

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
	ImageView imageFond;

	@FXML
	public void initialize() {
		fontLblUpSize = Font.font(Main.POLICE, 12);
		titreTheme.setText(themeAModifier.getNom());
		modifierQuestion.setDisable(true);
		supprimerQuestion.setDisable(true);
		modifierZone.setDisable(true);
		supprimerZone.setDisable(true);
		setListeQuestions(themeAModifier.getQuestions());
		setListeZonesWithQuestion(null);
		imageFond.setImage(themeAModifier.getImageFond());
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
		File image = explorateur.showOpenDialog(Main.primaryStage);
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
			imageFond.setImage(themeAModifier.getImageFond());
		}
	}

	@FXML
	public void selectionQuestion() {
		if (listeQuestions.getSelectionModel().getSelectedItem() != null
				&& !listeQuestions.getSelectionModel().getSelectedItem().getText().equals("Aucune ...")) {
			modifierQuestion.setDisable(false);
			supprimerQuestion.setDisable(false);
			setListeZonesWithQuestion(themeAModifier
					.getQuestionWithIntitule(listeQuestions.getSelectionModel().getSelectedItem().getText()));
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
			themeAModifier.getQuestions().remove(themeAModifier.getQuestionWithIntitule(qstLbl.getText()));
			setListeQuestions(themeAModifier.getQuestions());
			selectionQuestion();
		}
	}

	@FXML
	public void supprimerZone() {
		Label zoneLbl = listeZones.getSelectionModel().getSelectedItem();
		if (zoneLbl != null) {
			Zone zoneTmp = themeAModifier.getZoneWithProperties(zoneLbl.getText());
			themeAModifier.getZones().remove(zoneTmp);
			for (Question qst : themeAModifier.getQuestions())
				for (Zone rep : qst.getReponses())
					if (rep == zoneTmp) {
						qst.getReponses().remove(rep);
						break;
					}
			if (listeQuestions.getSelectionModel().getSelectedItem() != null)
				setListeZonesWithQuestion(themeAModifier.getQuestionWithIntitule(listeQuestions.getSelectionModel().getSelectedItem().getText()));
			else
				setListeZonesWithQuestion(null);
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

			try {
				VBox root = null;
				root = FXMLLoader.load(getClass().getResource("creationEditionQuestions.fxml"));
				Scene scene = new Scene(root, Main.width, Main.height);
				Main.primaryStage.setResizable(false);
				Main.primaryStage.setScene(scene);
				Main.primaryStage.show();
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

			try {
				VBox root = null;
				root = FXMLLoader.load(getClass().getResource("creationEditionQuestions.fxml"));
				Scene scene = new Scene(root, Main.width, Main.height);
				Main.primaryStage.setResizable(false);
				Main.primaryStage.setScene(scene);
				Main.primaryStage.show();
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

			try {
				VBox root = null;
				root = FXMLLoader.load(getClass().getResource("Creation Zone.fxml"));
				Scene scene = new Scene(root, Main.width, Main.height);
				Main.primaryStage.setResizable(false);
				Main.primaryStage.setScene(scene);
				Main.primaryStage.show();
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

			try {
				VBox root = null;
				root = FXMLLoader.load(getClass().getResource("Creation Zone.fxml"));
				Scene scene = new Scene(root, Main.width, Main.height);
				Main.primaryStage.setResizable(false);
				Main.primaryStage.setScene(scene);
				Main.primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			changerImage.setStyle("-fx-border-color: red;-fx-border-color: red;-fx-border-width: 2px;");
	}

	@FXML
	public void sauvegarder() throws IOException, SQLException {
		System.out.println("save");
		if (themeAModifier.getImageFond() != null) {
			changerImage.setStyle("-fx-border-color:green;-fx-border-width: 1px;");
			ResultSet listeTheme = Main.bdd.executeQueryCmd("SELECT NOM_THEME FROM THEME;");
			boolean unuse = true;
			while (listeTheme.next())
				if (!listeTheme.getString("NOM_THEME").equals(themeAModifier.getNom())
						&& listeTheme.getString("NOM_THEME").equals(titreTheme.getText())) {
					unuse = false;
					titreTheme.setStyle("-fx-border-color:red;-fx-border-width: 2px;");
				}
			if (titreTheme.getText().length() < 2) {
				unuse = false;
				titreTheme.setStyle("-fx-border-color:red;-fx-border-width: 2px;");
			}
			if (unuse) {
				titreTheme.setStyle("-fx-border-color:green;-fx-border-width: 1px;");
				System.out.println(themeAModifier.getUrlImage());
				Main.bdd.executeUpdateCmd(
						"DELETE FROM POINT WHERE ID_ZONE IN (SELECT ID_ZONE FROM ZONE WHERE NOM_THEME='"
								+ themeAModifier.getNom() + "');");
				Main.bdd.executeUpdateCmd(
						"DELETE FROM REPONSE WHERE ID_ZONE IN (SELECT ID_ZONE FROM ZONE WHERE NOM_THEME='"
								+ themeAModifier.getNom() + "');");
				Main.bdd.executeUpdateCmd("DELETE FROM ZONE WHERE NOM_THEME='" + themeAModifier.getNom() + "';");
				Main.bdd.executeUpdateCmd("DELETE FROM QUESTION WHERE NOM_THEME='" + themeAModifier.getNom() + "';");
				Main.bdd.executeUpdateCmd("DELETE FROM THEME WHERE NOM_THEME='" + themeAModifier.getNom() + "';");
				// AJOUTER QUERY POUR SAUVEGARDER
				themeAModifier.setNom(titreTheme.getText());
				Main.bdd.executeUpdateCmd("INSERT INTO THEME VALUES ('" + themeAModifier.getNom() + "','"
						+ themeAModifier.getUrlImage() + "');");
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
						Main.bdd.executeUpdateCmd("INSERT INTO POINT VALUES (" + (++i) + "," + zone.getPoints().get(l)
								+ "," + zone.getPoints().get(l + 1) + "," + zone.getIndex() + ");");
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

				root = FXMLLoader.load(getClass().getResource("selectionnerTheme.fxml"));
				Scene scene = new Scene(root);

				Main.primaryStage.setResizable(false);

				Main.primaryStage.setScene(scene);
			}
		} else {
			changerImage.setStyle("-fx-border-color:red;-fx-border-width: 2px;");
		}
	}

	@FXML
	public void quitter() throws IOException {
		VBox root = new VBox();

		root = FXMLLoader.load(getClass().getResource("selectionnerTheme.fxml"));
		Scene scene = new Scene(root);

		Main.primaryStage.setResizable(false);

		Main.primaryStage.setScene(scene);
	}
}
