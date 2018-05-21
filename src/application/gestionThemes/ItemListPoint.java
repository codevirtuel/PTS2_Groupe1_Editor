package application.gestionThemes;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class ItemListPoint extends HBox {
	
	private Double x,y;
	private ImageView croix;
	
	public ItemListPoint(Double x, Double y) {
		this.x=x;
		this.y=y;
		this.setAlignment(Pos.CENTER);
		croix = new ImageView(new Image(getClass().getResourceAsStream("../view/croixrouge.jpg")));
		croix.setFitWidth(10);
		croix.setFitHeight(10);
		this.getChildren().add(croix);
		Label zoneToString = new Label("( "+x.doubleValue()+" ; "+y.doubleValue()+" )");
		zoneToString.setAlignment(Pos.CENTER);
		zoneToString.setFont(Font.font(Main.POLICE, 12));
		this.getChildren().add(zoneToString);
	}
	
	public ImageView getCroix() {
		return croix;
	}
}
