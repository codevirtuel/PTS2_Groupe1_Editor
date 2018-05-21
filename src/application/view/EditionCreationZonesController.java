package application.view;

import java.io.IOException;

import application.Main;
import application.gestionThemes.ItemListPoint;
import application.gestionThemes.ItemListZone;
import application.gestionThemes.Question;
import application.gestionThemes.Zone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class EditionCreationZonesController {

	public static Stage primaryStage;
	private static Zone zoneAModifier;
	private Zone sauvegardeZone;
	
	@FXML
	VBox vbox;
	
	@FXML
	ListView<ItemListPoint> points;
	
	@FXML
	AnchorPane paneImage;
	
	@FXML
	public void initialize() {
		sauvegardeZone = new Zone(zoneAModifier.getIndex());
		sauvegardeZone.getPoints().addAll(zoneAModifier.getPoints());
		System.out.println(paneImage.getPrefWidth()+";"+paneImage.getPrefHeight());
		System.out.println("img "+editionQuestionsZonesController.getThemeAModifier().getImageFond().getWidth()+";"+editionQuestionsZonesController.getThemeAModifier().getImageFond().getHeight());
		double height=paneImage.getPrefHeight(),width=paneImage.getPrefWidth(), rapport=height/width;
		if(editionQuestionsZonesController.getThemeAModifier().getImageFond().getWidth()*rapport>editionQuestionsZonesController.getThemeAModifier().getImageFond().getHeight())
		{
			height = width*editionQuestionsZonesController.getThemeAModifier().getImageFond().getHeight()/
					editionQuestionsZonesController.getThemeAModifier().getImageFond().getWidth();
			paneImage.setPrefHeight(height);
		}
		else
		{
			width = height*editionQuestionsZonesController.getThemeAModifier().getImageFond().getWidth()/
					editionQuestionsZonesController.getThemeAModifier().getImageFond().getHeight();
			paneImage.setPrefWidth(width);
		}
		BackgroundImage bgImage = new BackgroundImage(editionQuestionsZonesController.getThemeAModifier().getImageFond(),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(width, height, false, false, false, true));
		paneImage.setBackground(new Background(bgImage));
		
		Double x = null;
		boolean xTraite = true;
		for(Double coord : zoneAModifier.getPoints()) {
			if(xTraite) {
				x=coord;
				xTraite=false;
			}
			else {
				ItemListPoint itemPoint = new ItemListPoint(x,coord);
				points.getItems().add(itemPoint);
				itemPoint.getCroix().setOnMouseClicked(event -> points.getItems().remove(itemPoint));
				Circle point = new Circle(2, Color.RED);
				paneImage.getChildren().add(point);
				point.setTranslateX(x);
				point.setTranslateY(coord);
				xTraite=true;
			}
		}
		
		System.out.println(paneImage.getPrefWidth()+";"+paneImage.getPrefHeight());
		Scaler.updateSize(Main.width,vbox);
		System.out.println(paneImage.getPrefWidth()+";"+paneImage.getPrefHeight());
	}

	@FXML
	private void addPoint(MouseEvent e) {
		Circle point = new Circle(3, Color.RED);
		paneImage.getChildren().add(point);
		double x,y;
		x=(double)(Math.round(e.getX()*100))/100;
		y=(double)(Math.round(e.getY()*100))/100;
		zoneAModifier.getPoints().add(x);
		zoneAModifier.getPoints().add(y);
		point.setTranslateX(x);
		point.setTranslateY(y);
		ItemListPoint itemPoint = new ItemListPoint(x,y);
		itemPoint.getCroix().setOnMouseClicked(event -> points.getItems().remove(itemPoint));
		points.getItems().add(itemPoint);
		Scaler.updateSize(Main.width,itemPoint);
	}

	public static void setZone(Zone zSelect) {
		zoneAModifier = zSelect;
	}

	@FXML
	public void valider() throws IOException {
		VBox root = new VBox();

		editionQuestionsZonesController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
		Scene scene = new Scene(root);

		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
	}

	@FXML
	public void quitter() throws IOException {
		zoneAModifier.getPoints().clear();
		zoneAModifier.getPoints().addAll(sauvegardeZone.getPoints());
		VBox root = new VBox();

		SelecThemeController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
		Scene scene = new Scene(root);

		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
	}
}
