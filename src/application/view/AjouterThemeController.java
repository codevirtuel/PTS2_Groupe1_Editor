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

import application.Main;
import application.gestionThemes.Question;
import application.gestionThemes.Theme;
import application.gestionThemes.Zone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class AjouterThemeController {

	@FXML
	VBox vbox;
	@FXML
	TextField nomTheme;
	@FXML
	TextField urlImage;
	@FXML
	ImageView apercuImage;

	static Stage primaryStage;
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
			themeACreer.setImageFond(new Image(new FileInputStream(copieData.getAbsolutePath())));
			themeACreer.setUrlImage(copieData.getName());
			apercuImage.setImage(themeACreer.getImageFond());
			urlImage.setText(themeACreer.getUrlImage());
		}
	}

	@FXML
	public void valider() throws IOException {
		themeACreer.setNom(nomTheme.getText());
		try {
			ResultSet listeTheme = Main.bdd.executeQueryCmd("SELECT NOM_THEME FROM THEME;");
			boolean unuse = true;
			while (listeTheme.next())
				if (listeTheme.getString("NOM_THEME").equals(themeACreer.getNom())) {
					unuse = false;
					nomTheme.setStyle("-fx-border-color:red;");
				}
			if (unuse)
				try {
					Main.bdd.executeUpdateCmd("INSERT INTO THEME VALUES ('" + themeACreer.getNom() + "','"
							+ themeACreer.getUrlImage() + "');");
				} catch (SQLException e) {
					e.printStackTrace();
				}

			VBox root = new VBox();

			editionQuestionsZonesController.setThemeAModifier(themeACreer);
			editionQuestionsZonesController.primaryStage = primaryStage;
			root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
			Scene scene = new Scene(root);

			primaryStage.setResizable(false);

			primaryStage.setScene(scene);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
