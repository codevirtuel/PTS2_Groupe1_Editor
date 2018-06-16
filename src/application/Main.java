package application;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.ini4j.Ini;

import application.database.Connect;

import application.view.AccueilController;
import application.view.Interface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

public class Main extends Application {

	public static int width = 1280;
	public static int height = 720;
	public static final String POLICE = "Roboto";
	public static Connect bdd;
	public static Stage primaryStage;
	public static double volume;
	public static AudioClip clip;

	@Override
	public void start(Stage primaryStage) {
		Ini ini = null;
		try {
			ini = new Ini(new File("options.ini"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		volume = Double.parseDouble(ini.get("other", "volume"));
		// Clip clip = AudioSystem.getClip();
		clip = new AudioClip(getClass().getResource("data/musiques/MiamiPath.mp3").toString());
		clip.play(volume / 100);
		clip.setCycleCount(Integer.MAX_VALUE);
		try {

			Main.primaryStage = primaryStage;
			primaryStage.setTitle("Fin'e Test - Editeur");
			changeInterface(Interface.ACCUEIL);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {

	}

	public static void main(String[] args) {
		try {
			bdd = new Connect("./src/application/database/database", "root", "root");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		launch(args);
	}

	public static void changeInterface(Interface inter) {
		VBox root = new VBox();
		try {
			System.out.println("view/" + inter.toString());
			root = FXMLLoader.load(Main.class.getResource("view/" + inter.toString()));
		} catch (IOException e) {
			System.err.println(
					"===========================\nErreur dans la fonction changeInterface()\n===========================");
			e.printStackTrace();
		}
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());

		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
	}
}
