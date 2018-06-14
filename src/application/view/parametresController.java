package application.view;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class parametresController {
	
	//Gestion volume
	@FXML
	Slider slider;
	@FXML
	Label currentVolume;
	
	//Gestion r�solutions
	@FXML
	ComboBox resolutions;

	
	@FXML
	VBox vbox;
	
	@FXML
	HBox hbox;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		populateResolutions();
		slider.setValue(Main.volume);
	}
	
	@FXML 
	public void goToAccueil() throws IOException {
		VBox root = new VBox();
		root = FXMLLoader.load(getClass().getResource("Editeur - Accueil.fxml"));
		Scene scene = new Scene(root,Main.width,Main.height);
		
		Main.primaryStage.setResizable(false);
		Main.primaryStage.setScene(scene);
		Main.primaryStage.show();
	}
	

	
	@FXML
	public void sauvegarder() throws IOException {
		updateResolution();
		saveOptions();
		Main.clip.stop();
		Main.clip.play(Main.volume/100);
		System.out.println(Main.clip.getVolume());
		goToAccueil();
	}
	
	@FXML
	public void saveOptions() throws InvalidFileFormatException, IOException {	
		File saveFile = new File("options.ini");
		//remove old file if exists
		if(saveFile.exists())
			saveFile.delete();
		
		saveFile.createNewFile();
		
		Ini ini = new Ini(new File("options.ini"));
		//Create sections
		ini.put("resolution", "width",Main.width);
		ini.put("resolution", "height",Main.height);
		ini.put("other", "volume",Main.volume);
		
		ini.store();
		System.out.println("Config saved !");
	}
	
	@FXML
	public void updateVolume() {
		Main.volume = Math.round(slider.getValue());
		currentVolume.setText(Math.round(Main.volume)+"%");
	}
	
	@FXML
	public void populateResolutions() {
		ObservableList<String> liste = FXCollections.observableArrayList
				("640 x 360",
				"896 x 504",
				"1280 x 720",
				"1664 x 936",
				"1920 x 1080");
		resolutions.setItems(liste);
		
		//D�fini la r�solution par d�faut en fonction de l'actuelle
		switch(Main.height) {
		case 360:
			resolutions.getSelectionModel().select(0); break;
		case 504:
			resolutions.getSelectionModel().select(1); break;
		case 720:
			resolutions.getSelectionModel().select(2); break;
		case 936:
			resolutions.getSelectionModel().select(3); break;
		case 1080:
			resolutions.getSelectionModel().select(4); break;
		}
	}
	
	@FXML
	public void updateResolution() {
		String reso = (String) resolutions.getValue();
		switch(reso) {
		case "640 x 360":
			Main.width = 640;
			Main.height = 360;
			break;
		case "896 x 504":
			Main.width = 896;
			Main.height = 504;
			break;
		case "1280 x 720":
			Main.width = 1280;
			Main.height = 720;
			break;
		case "1664 x 936":
			Main.width = 1664;
			Main.height = 936;
			break;
		case "1920 x 1080":
			Main.width = 1980;
			Main.height = 1080;
			break;
		}
		System.out.println("Updated Resolution to "+Main.width+" x "+Main.height);
	}
}
