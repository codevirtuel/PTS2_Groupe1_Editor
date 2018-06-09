package application;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import application.database.Connect;

import application.view.AccueilController;
import application.view.Interface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Main extends Application {

	public static int width = 1280;
	public static int height = 720;
	public static final String POLICE="Roboto";
	public static Connect bdd;
	public static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		AudioInputStream audioIn;
		try {
				audioIn = AudioSystem.getAudioInputStream(new File("src/application/data/musique.wav"));
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {

			Main.primaryStage=primaryStage;

			changeInterface(Interface.ACCUEIL);
			primaryStage.show();
		} catch(Exception e) {
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
			System.out.println("view/"+inter.toString());
			root = FXMLLoader.load(Main.class.getResource("view/"+inter.toString()));
		} catch (IOException e) {
			System.err.println("===========================\nErreur dans la fonction changeInterface()\n===========================");
			e.printStackTrace();
		}
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());

		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
	}
}
