package application.view;

import java.io.IOException;

import application.Main;
import application.gestionThemes.ItemListPoint;
import application.gestionThemes.ItemListZone;
import application.gestionThemes.Question;
import application.gestionThemes.Zone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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

	public static Question questionAssociation;
	private static Zone zoneAModifier;
	private Zone sauvegardeZone;

	@FXML
	VBox vbox;

	@FXML
	ListView<ItemListPoint> points;

	@FXML
	AnchorPane paneImage;

	@FXML
	Button btNewZone;

	@FXML
	Label txtErreur;

	@FXML
	public void initialize() {
		sauvegardeZone = new Zone(zoneAModifier.getIndex());
		sauvegardeZone.getPoints().addAll(zoneAModifier.getPoints());
		System.out.println(paneImage.getPrefWidth() + ";" + paneImage.getPrefHeight());
		System.out.println("img " + editionQuestionsZonesController.getThemeAModifier().getImageFond().getWidth() + ";"
				+ editionQuestionsZonesController.getThemeAModifier().getImageFond().getHeight());
		
		double height = paneImage.getPrefHeight(), width = paneImage.getPrefWidth(), rapport = height / width;
		
		if (editionQuestionsZonesController.getThemeAModifier().getImageFond().getWidth()
				* rapport > editionQuestionsZonesController.getThemeAModifier().getImageFond().getHeight()) {
			
			height = width * editionQuestionsZonesController.getThemeAModifier().getImageFond().getHeight()
					/ editionQuestionsZonesController.getThemeAModifier().getImageFond().getWidth();
			paneImage.setPrefHeight(height);
		} else {
			
			width = height * editionQuestionsZonesController.getThemeAModifier().getImageFond().getWidth()
					/ editionQuestionsZonesController.getThemeAModifier().getImageFond().getHeight();
			paneImage.setPrefWidth(width);
		}
		
		BackgroundImage bgImage = new BackgroundImage(
				editionQuestionsZonesController.getThemeAModifier().getImageFond(), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(width, height, false, false, false, true));
		paneImage.setBackground(new Background(bgImage));
		zoneAModifier.setOpacity(0.3);
		zoneAModifier.setFill(Color.RED);
		zoneAModifier.setStroke(Color.BLACK);
		paneImage.getChildren().add(zoneAModifier);

		Double x = null;
		boolean xTraite = true;
		for (Double coord : zoneAModifier.getPoints()) {
			if (xTraite) {
				x = coord;
				xTraite = false;
			} else {
				ItemListPoint itemPoint = new ItemListPoint(x, coord);
				points.getItems().add(itemPoint);
				itemPoint.setOnMouseClicked(event -> selectionPoint(itemPoint));
				itemPoint.getCroix().setOnMouseClicked(event -> supprimerPoint(itemPoint));
				Circle point = new Circle(2, Color.RED);
				paneImage.getChildren().add(point);
				point.setTranslateX(x);
				point.setTranslateY(coord);
				xTraite = true;
			}
		}

		System.out.println(paneImage.getPrefWidth() + ";" + paneImage.getPrefHeight());
		Scaler.updateSize(Main.width, vbox);
		System.out.println(paneImage.getPrefWidth() + ";" + paneImage.getPrefHeight());
	}

	@FXML
	private void addPoint(MouseEvent e) {
		Circle point = new Circle(3, Color.RED);
		paneImage.getChildren().add(point);
		double x, y;
		x = (double) (Math.round(e.getX() * 100)) / 100;
		y = (double) (Math.round(e.getY() * 100)) / 100;
		boolean alreadyExists = false;
		for (ItemListPoint itemList : points.getItems())
			if (itemList.getX() == x && itemList.getY() == y)
				alreadyExists = true;
		if (!alreadyExists) {
			zoneAModifier.getPoints().add(x);
			zoneAModifier.getPoints().add(y);
			point.setTranslateX(x);
			point.setTranslateY(y);
			ItemListPoint itemPoint = new ItemListPoint(x, y);
			itemPoint.setOnMouseClicked(event -> selectionPoint(itemPoint));
			itemPoint.getCroix().setOnMouseClicked(event -> supprimerPoint(itemPoint));
			points.getItems().add(itemPoint);
			Scaler.updateSize(Main.width, itemPoint);
		} else {
			txtErreur.setText("Ce point a déjà été ajouté.");
		}
	}

	@FXML
	public void supprimerPoint(ItemListPoint point) {
		Circle circle = null;
		for (Node node : paneImage.getChildren()) {
			System.out.println(node);
			if (node instanceof Circle) {
				Circle circleTmp = (Circle) node;
				System.out.println(circleTmp.getTranslateX() + ";" + circleTmp.getTranslateY());
				System.out.println(circleTmp.getCenterX() + ";" + circleTmp.getCenterY());
				if (circleTmp.getTranslateX() == point.getX().doubleValue()
						&& circleTmp.getTranslateY() == point.getY().doubleValue()) {
					circle = (Circle) node;
				}
			}
		}
		points.getItems().remove(point);
		zoneAModifier.getPoints().remove(point.getX());
		zoneAModifier.getPoints().remove(point.getY());
		if (circle != null)
			paneImage.getChildren().remove(circle);
	}

	@FXML
	public void selectionPoint(ItemListPoint point) {
		System.out.println("clique on pt");
		if(point!=null)
		for (Node node : paneImage.getChildren()) {
			System.out.println(node);
			if (node instanceof Circle) {
				Circle circleTmp = (Circle) node;
				System.out.println(circleTmp.getTranslateX() + ";" + circleTmp.getTranslateY());
				System.out.println(circleTmp.getCenterX() + ";" + circleTmp.getCenterY());
				if (circleTmp.getTranslateX() == point.getX().doubleValue()
						&& circleTmp.getTranslateY() == point.getY().doubleValue()) {
					circleTmp.setFill(Color.BLUE);
				} else {
					circleTmp.setFill(Color.RED);
				}
			}
		}
	}

	public static void setZone(Zone zSelect) {
		zoneAModifier = zSelect;
	}

	@FXML
	public void valider() throws IOException {
		if (points.getItems().size() > 2) {
			if (questionAssociation == null) {
				VBox root = new VBox();

				root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
				Scene scene = new Scene(root);

				Main.primaryStage.setResizable(false);

				Main.primaryStage.setScene(scene);
			} else {
				questionAssociation.getReponses().add(zoneAModifier);
				editionCreationQuestionsController.setQuestion(questionAssociation);

				editionQuestionsZonesController.getThemeAModifier().addZone(zoneAModifier);
				questionAssociation = null;
				try {
					VBox root = null;
					root = FXMLLoader.load(getClass().getResource("creationEditionQuestions.fxml"));
					Scene scene = new Scene(root, Main.width, Main.height);
					Main.primaryStage.setResizable(false);
					Main.primaryStage.setScene(scene);
					Main.primaryStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else
			txtErreur.setText("Il faut que la zone contiennent au moins trois points.");
	}

	@FXML
	public void creerNewZone() throws IOException {
		if (points.getItems().size() > 2) {
			if (questionAssociation == null) {
				Zone newZ = new Zone(editionQuestionsZonesController.idZoneMax);
				editionQuestionsZonesController.idZoneMax++;
				editionQuestionsZonesController.getThemeAModifier().addZone(newZ);
				EditionCreationZonesController.setZone(newZ);
				try {
					VBox root = null;
					root = FXMLLoader.load(getClass().getResource("Creation Zone.fxml"));
					Scene scene = new Scene(root, Main.width, Main.height);
					Main.primaryStage.setResizable(false);
					Main.primaryStage.setScene(scene);
					Main.primaryStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				questionAssociation.getReponses().add(zoneAModifier);
				editionQuestionsZonesController.getThemeAModifier().addZone(zoneAModifier);
				Zone newZ = new Zone(editionQuestionsZonesController.idZoneMax);
				editionQuestionsZonesController.idZoneMax++;
				EditionCreationZonesController.setZone(newZ);
				try {
					VBox root = null;
					root = FXMLLoader.load(getClass().getResource("Creation Zone.fxml"));
					Scene scene = new Scene(root, Main.width, Main.height);
					Main.primaryStage.setResizable(false);
					Main.primaryStage.setScene(scene);
					Main.primaryStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else
			txtErreur.setText("Il faut que la zone contiennent au moins trois points.");
	}

	@FXML
	public void quitter() throws IOException {
		zoneAModifier.getPoints().clear();
		zoneAModifier.getPoints().addAll(sauvegardeZone.getPoints());
		if(questionAssociation == null) {
			editionQuestionsZonesController.getThemeAModifier().getZones().remove(zoneAModifier);
		VBox root = new VBox();

		root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
		Scene scene = new Scene(root);

		Main.primaryStage.setResizable(false);

		Main.primaryStage.setScene(scene);
		}
		else {
			editionCreationQuestionsController.setQuestion(questionAssociation);

			questionAssociation = null;
			try {
				VBox root = null;
				root = FXMLLoader.load(getClass().getResource("creationEditionQuestions.fxml"));
				Scene scene = new Scene(root, Main.width, Main.height);
				Main.primaryStage.setResizable(false);
				Main.primaryStage.setScene(scene);
				Main.primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
