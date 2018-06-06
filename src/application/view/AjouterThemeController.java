package application.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import application.gestionThemes.Theme;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AjouterThemeController {

	private static final String CHEMIN_DATA = "./src/application/data/";
	private static final String PATTERN_URL = "^[A-Z]{1}:/.+";
	private static final String BORDURE_ROUGE = "-fx-border-color:red;-fx-border-width: 2px;";
	private static final String BORDURE_VERTE = "-fx-border-color:green;-fx-border-width: 1px;";

	@FXML
	VBox vbox;
	@FXML
	TextField nomTheme;
	@FXML
	TextField urlImage;
	@FXML
	ImageView apercuImage;

	Theme themeACreer;

	@FXML
	public void initialize() {
		themeACreer = new Theme("Nom du thème ...");
		Scaler.updateSize(Main.width, vbox);
	}

	@FXML
	public void changerImage() throws FileNotFoundException {
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
			System.out.println(image.getAbsolutePath());
			File copieData = copieImage(image);
			themeACreer.setImageFond(new Image(new FileInputStream(copieData.getAbsolutePath())));
			themeACreer.setUrlImage(copieData.getName());
			apercuImage.setImage(themeACreer.getImageFond());
			urlImage.setText(themeACreer.getUrlImage());
		}
	}

	public File copieImage(File image) {
		File copieData = new File(CHEMIN_DATA + image.getName());
		try (InputStream sourceFile = new FileInputStream(image);
				OutputStream destinationFile = new FileOutputStream(copieData)) {
			byte buffer[] = new byte[512 * 1024];
			int nbLecture;
			while ((nbLecture = sourceFile.read(buffer)) != -1) {
				destinationFile.write(buffer, 0, nbLecture);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copieData;
	}

	@FXML
	public void majFromUrl() throws FileNotFoundException {
		if (!urlImage.getText().equals("")) {
			Pattern urlAbs = Pattern.compile(PATTERN_URL);
			Matcher absMatch = urlAbs.matcher(urlImage.getText().replace("\\", "/"));
			boolean fileFound = true;
			File copieData = null;
			if (absMatch.matches()) {
				copieData = copieImage(new File(urlImage.getText()));
			} else {
				copieData = new File(CHEMIN_DATA + urlImage.getText());
			}
			try {
				themeACreer.setImageFond(new Image(new FileInputStream(copieData.getAbsolutePath())));
			} catch (FileNotFoundException e) {
				fileFound = false;
				urlImage.setStyle(BORDURE_ROUGE);
				themeACreer.setImageFond(null);
				themeACreer.setUrlImage(null);
				apercuImage.setImage(null);
			}
			if (fileFound) {
				themeACreer.setUrlImage(copieData.getName());
				apercuImage.setImage(themeACreer.getImageFond());
				urlImage.setStyle(BORDURE_VERTE);
			}
		}
	}

	@FXML
	public void valider() throws IOException {
		themeACreer.setNom(nomTheme.getText());
		if (themeACreer.getImageFond() != null) {
			urlImage.setStyle(BORDURE_VERTE);
			try {
				ResultSet listeTheme = Main.bdd.executeQueryCmd("SELECT NOM_THEME FROM THEME;");
				boolean unuse = true;
				while (listeTheme.next())
					if (listeTheme.getString("NOM_THEME").equals(themeACreer.getNom())) {
						unuse = false;
						nomTheme.setStyle(BORDURE_ROUGE);
					}
				if (themeACreer.getNom().length() < 2) {
					unuse = false;
					nomTheme.setStyle(BORDURE_ROUGE);
				}
				if (unuse) {
					nomTheme.setStyle(BORDURE_VERTE);
					try {
						Main.bdd.executeUpdateCmd("INSERT INTO THEME VALUES ('" + themeACreer.getNom() + "','"
								+ themeACreer.getUrlImage() + "');");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Main.changeInterface(Interface.EDITION_THEME);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			urlImage.setStyle(BORDURE_ROUGE);
		}
	}

	@FXML
	public void annuler() {
		Main.changeInterface(Interface.ACCUEIL);
	}

}
