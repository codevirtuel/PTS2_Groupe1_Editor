package application.view;

import java.io.IOException;

import application.Main;
import application.gestionThemes.ItemListZone;
import application.gestionThemes.Question;
import application.gestionThemes.Zone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

public class editionCreationQuestionsController {

	private static Question questionAModifier;
	private Question sauvegardeQuestion;

	@FXML
	VBox vbox;

	@FXML
	TextField intituleQuestion;

	@FXML
	ListView<ItemListZone> zones;

	@FXML
	AnchorPane paneImage;

	@FXML
	Label txtErreur;

	@FXML
	public void initialize() {
		if(!questionAModifier.getIntitule().equals("")) {
			sauvegardeQuestion = new Question(questionAModifier.getIntitule());
			sauvegardeQuestion.setReponses(questionAModifier.getReponses());
		}
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

		intituleQuestion.setText(questionAModifier.getIntitule());

		for (Zone zone : questionAModifier.getReponses()) {
			ItemListZone itemZone = new ItemListZone(zone);
			itemZone.setOnMouseClicked(event -> selectionZone(itemZone));
			itemZone.getCroix().setOnMouseClicked(event -> removeZone(itemZone));
			zones.getItems().add(itemZone);
		}

		for (Zone zone : editionQuestionsZonesController.getThemeAModifier().getZones()) {
			zone.setOpacity(0.3);
			zone.setFill(Color.rgb((int) (Math.random() * 150 + 100), (int) (Math.random() * 150 + 100),
					(int) (Math.random() * 150 + 100)));
			zone.setStroke(Color.BLACK);
			zone.setStrokeWidth(1);
			System.out.println(zone.getBoundsInLocal());
			paneImage.getChildren().add(zone);
		}

		System.out.println(paneImage.getPrefWidth() + ";" + paneImage.getPrefHeight());
		Scaler.updateSize(Main.width, vbox);
		System.out.println(paneImage.getPrefWidth() + ";" + paneImage.getPrefHeight());
	}

	@FXML
	private void addZone(MouseEvent e) {
		if (e.getTarget() instanceof Zone) {
			ItemListZone itemZone = new ItemListZone((Zone) e.getTarget());
			if (!zones.getItems().contains(itemZone)) {
				questionAModifier.getReponses().add(((Zone) e.getTarget()));
				zones.getItems().add(itemZone);
				itemZone.setOnMouseClicked(event -> selectionZone(itemZone));
				itemZone.getCroix().setOnMouseClicked(event -> removeZone(itemZone));
				Scaler.updateSize(Main.width, itemZone);
			}
			else
				txtErreur.setText("La zone a déjà été ajoutée.");
		}
	}

	public static void setQuestion(Question qSelect) {
		questionAModifier = qSelect;
	}

	public void removeZone(ItemListZone itemList) {
		System.out.println("Supp zone");
		zones.getItems().remove(itemList);
		selectionZone(null);
	}

	public void selectionZone(ItemListZone itemList) {
		System.out.println("clique on pt");
		System.out.println(itemList);
		if (zones.getItems().contains(itemList) || itemList == null) {
			for (Node node : paneImage.getChildren()) {
				System.out.println(node);
				if (node instanceof Zone) {
					Zone zoneTmp = (Zone) node;
					if (itemList != null && zoneTmp == itemList.getZone()) {
						zoneTmp.setStroke(Color.BLUE);
						zoneTmp.setStrokeWidth(3);
					} else {
						zoneTmp.setStroke(Color.BLACK);
						zoneTmp.setStrokeWidth(1);
					}
				}
			}
		}
	}

	@FXML
	public void creerZoneAssocier() throws IOException {
		questionAModifier.setIntitule(intituleQuestion.getText());
		Zone newZ = new Zone(editionQuestionsZonesController.idZoneMax);
		editionQuestionsZonesController.idZoneMax++;
		EditionCreationZonesController.setZone(newZ);
		EditionCreationZonesController.questionAssociation = questionAModifier;
		VBox root = new VBox();

		root = FXMLLoader.load(getClass().getResource("Creation Zone.fxml"));
		Scene scene = new Scene(root);

		Main.primaryStage.setResizable(false);

		Main.primaryStage.setScene(scene);
	}

	@FXML
	public void valider() throws IOException {
		questionAModifier.setIntitule(intituleQuestion.getText());
		if (zones.getItems().size() > 0 && questionAModifier.getIntitule().length() > 2) {
			VBox root = new VBox();

			root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
			Scene scene = new Scene(root);

			Main.primaryStage.setResizable(false);

			Main.primaryStage.setScene(scene);
		}
		else if(zones.getItems().size() > 0) {
			txtErreur.setText("L'intitulé de la question doit faire plus de 2 caractères.");
			intituleQuestion.setStyle("-fx-border-color: red;-fx-border-color: red;-fx-border-width: 2px;");
		}
		else {
			txtErreur.setText("La queston doit avoir au moins une zone de réponse.");
			zones.setStyle("-fx-border-color: red;-fx-border-color: red;-fx-border-width: 2px;");
		}
	}

	@FXML
	public void quitter() throws IOException {
		if(sauvegardeQuestion != null) {
			questionAModifier.setIntitule(sauvegardeQuestion.getIntitule());
			questionAModifier.setReponses(sauvegardeQuestion.getReponses());
		}
		else {
			editionQuestionsZonesController.getThemeAModifier().getQuestions().remove(questionAModifier);
		}
		VBox root = new VBox();

		root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
		Scene scene = new Scene(root);

		Main.primaryStage.setResizable(false);

		Main.primaryStage.setScene(scene);
	}
}
