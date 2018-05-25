package application.view;

import java.awt.Paint;
import java.io.IOException;

import application.Main;
import application.gestionThemes.ItemListZone;
import application.gestionThemes.Question;
import application.gestionThemes.Zone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class editionCreationQuestionsController {

	public static Stage primaryStage;
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
	public void initialize() {
		sauvegardeQuestion = new Question(questionAModifier.getIntitule());
		sauvegardeQuestion.setReponses(questionAModifier.getReponses());
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
			itemZone.getCroix().setOnMouseClicked(event -> zones.getItems().remove(itemZone));
			zones.getItems().add(itemZone);
		}

		for (Zone zone : editionQuestionsZonesController.getThemeAModifier().getZones()) {
			zone.setOpacity(0.3);
			zone.setFill(Color.RED);
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
				itemZone.getCroix().setOnMouseClicked(event -> zones.getItems().remove(itemZone));
				Scaler.updateSize(Main.width, itemZone);
			}
		}
	}

	public static void setQuestion(Question qSelect) {
		questionAModifier = qSelect;
	}

	@FXML
	public void valider() throws IOException {
		questionAModifier.setIntitule(intituleQuestion.getText());
		if (zones.getItems().size() > 0 && questionAModifier.getIntitule().length()>2) {
			VBox root = new VBox();

			SelecThemeController.primaryStage = primaryStage;
			root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
			Scene scene = new Scene(root);

			primaryStage.setResizable(false);

			primaryStage.setScene(scene);
		}
	}

	@FXML
	public void quitter() throws IOException {
		questionAModifier.setIntitule(sauvegardeQuestion.getIntitule());
		questionAModifier.setReponses(sauvegardeQuestion.getReponses());
		VBox root = new VBox();

		SelecThemeController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("editionQuestionsZones.fxml"));
		Scene scene = new Scene(root);

		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
	}
}
