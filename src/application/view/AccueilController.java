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
import javafx.stage.Stage;

public class AccueilController {
	
	public static Stage primaryStage;
	
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
		try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(
                    "src/application/data/musique.wav"));
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
		Scaler.updateSize(Main.width,vbox);
	}
	
	@FXML
	public void gotoAjoutTheme() throws IOException {
		VBox root = new VBox();

		AjouterThemeController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("CreationTheme.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
	}
	
	@FXML
	public void gotoSelecTheme() throws IOException {
		VBox root = new VBox();

		SelecThemeController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("selectionnerTheme.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
	}
	
	@FXML
	public void gotoParams() throws IOException {
		VBox root = new VBox();

		parametresController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("parametres.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
	}
	
	@FXML
	public void quit() throws IOException {
		primaryStage.close();
	}
	
}
