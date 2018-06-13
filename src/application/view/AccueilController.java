package application.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AccueilController {

	@FXML
	VBox vbox;
	@FXML
	Button creerTheme;
	@FXML
	Button modifierSupprimerTheme;
	@FXML
	Button parametres;
	@FXML
	Button quitter;

	@FXML
	public void initialize() {
		parametres.setFont(Font.font("Arial Black", 15));
		Scaler.updateSize(Main.width, vbox);
	}

	public void gotoAjoutTheme() throws IOException {
		Main.changeInterface(Interface.AJOUT_THEME);
	}

	public void gotoSelecTheme() throws IOException {
		Main.changeInterface(Interface.SELECTION_THEME);
	}

	public void gotoParams() throws IOException {
		Main.changeInterface(Interface.PARAMETRES);
	}

	public void quit() throws IOException {
		Main.primaryStage.close();
	}

}
