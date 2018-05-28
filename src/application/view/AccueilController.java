package application.view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	public void quit() throws IOException {
		primaryStage.close();
	}
	
}
