package application.gestionThemes;

import java.util.List;

import javafx.scene.shape.Polygon;

public class Zone extends Polygon {
	
	private int index;
	
	public Zone(int i, List<Double> coordonnes) {
		index=i;
		this.getPoints().addAll(coordonnes);
	}

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

}
