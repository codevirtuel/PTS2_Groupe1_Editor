package application.view;

import java.util.ArrayList;

import application.Main;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class accueilController {
	
	public int scale = 1;
	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	@FXML
	Button creer;
	@FXML
	Button modifSuppr;
	@FXML
	Button parametres;
	@FXML
	Button quitter;
	@FXML
	Label titre;
	@FXML
	ImageView logo;
	
	@FXML
	public void initialize() {
		updateSize(Main.width,vbox);
	}
	
	
	public void updateSize(double newWidth,Pane root) {		
		double factor = newWidth/720;
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		/*for(Node node : root.getChildren()) {
			scale(node,factor);
			System.out.println(node);
		}*/
		scale(creer, factor);
		scale(modifSuppr, factor);
		scale(parametres, factor);
		scale(quitter, factor);
		scale(titre, factor);
		scale(logo, factor);
	}
	
	public void scale(Node obj, double factor) {
		if(obj instanceof Label) {
			((Labeled) obj).setFont(new Font(((Labeled) obj).getFont().getSize()*factor));
		}
		else if(obj instanceof ImageView) {
			((ImageView) obj).setFitHeight(((ImageView) obj).getFitHeight()*factor);
			((ImageView) obj).setFitWidth(((ImageView) obj).getFitWidth()*factor);
		}
		else{
			((Region) obj).setPrefSize(((Region) obj).getPrefWidth()*factor, ((Region) obj).getPrefHeight()*factor);
		}
	}
	
}
