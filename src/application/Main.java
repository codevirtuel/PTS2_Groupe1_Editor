package application;

import java.sql.SQLException;

import application.database.Connect;

import application.view.AccueilController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Main extends Application {

	public static int width = 1280;
	public static int height = 720;

	@Override
	public void start(Stage primaryStage) {
		try {

			VBox root = new VBox();

			AccueilController.primaryStage = primaryStage;
			root = FXMLLoader.load(getClass().getResource("view/Editeur - Accueil.fxml"));
			Scene scene = new Scene(root,width,height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setResizable(false);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
		try {
			new Connect("~/BaseDD", "user", "");
			System.out.println("co r�ussie");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
//Coucou MAMENE
