package application.view;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/*
 * 	Syntaxe :
 * 	updateSize(nouvelleLongueur,main container)
 */

public class Scaler {
	
	private static final double DEFAUT_WIDTH=640;
	
	public static void updateSize(double newWidth,Pane root) {		
		double factor = newWidth/DEFAUT_WIDTH;
		ArrayList<Node> nodes = getAllNodes(root);
		for(Node node : nodes) {
			scale(node,factor);
		}
		scale(root,factor);
	}
	
	public static void scale(Node obj, double factor) {
		
		//AnchorPane
		if(obj.getParent() instanceof AnchorPane) {
			double anchorFactor = Math.round(factor);
			//Left anchor
			if(AnchorPane.getLeftAnchor(obj) != null) AnchorPane.setLeftAnchor(obj, AnchorPane.getLeftAnchor(obj)*anchorFactor);
			//Right anchor
			if(AnchorPane.getRightAnchor(obj) != null) AnchorPane.setRightAnchor(obj, AnchorPane.getRightAnchor(obj)*anchorFactor);
			//Top anchor
			if(AnchorPane.getTopAnchor(obj) != null) AnchorPane.setTopAnchor(obj, AnchorPane.getTopAnchor(obj)*anchorFactor);
			//Bottom anchor
			if(AnchorPane.getBottomAnchor(obj) != null) AnchorPane.setBottomAnchor(obj, AnchorPane.getBottomAnchor(obj)*anchorFactor);
		}
		
		//HBox spacing
		if(obj.getParent() instanceof HBox) {
			HBox parent = (HBox) obj.getParent();
			parent.setSpacing(parent.getSpacing()*factor);
		}
		
		if(obj instanceof Label) {
			((Labeled) obj).setFont(new Font(((Labeled) obj).getFont().getSize()*factor));
			if(((Region) obj).getPrefWidth()>=0 && ((Region) obj).getPrefHeight()>=0)((Region) obj).setPrefSize(((Region) obj).getPrefWidth()*factor, ((Region) obj).getPrefHeight()*factor);
			else if(((Region) obj).getPrefWidth()>=0)((Region) obj).setPrefWidth(((Region) obj).getPrefWidth()*factor);
			else if(((Region) obj).getPrefHeight()>=0)((Region) obj).setPrefHeight(((Region) obj).getPrefHeight()*factor);
		}
		else if(obj instanceof ImageView) {
			((ImageView) obj).setFitHeight(((ImageView) obj).getFitHeight()*factor);
			((ImageView) obj).setFitWidth(((ImageView) obj).getFitWidth()*factor);
		}
		else if(obj instanceof ListView) {
			((Region) obj).setPrefSize(((Region) obj).getPrefWidth()*factor, ((Region) obj).getPrefHeight()*factor);
			if(!(((ListView) obj).getItems().size()<1) && ((ListView) obj).getItems().get(0) instanceof Node)
			{
				for(Object node : ((ListView) obj).getItems()) {
					if(node instanceof Pane)
						updateSize(factor*DEFAUT_WIDTH, ((Pane)node));
					else
						scale(((Node)node),factor);
				}
			}
		}
		else if(obj instanceof Button) {
			((Button) obj).setFont(new Font(((Button) obj).getFont().getSize()*factor));
			((Region) obj).setMinSize(((Region) obj).getPrefWidth()*factor, ((Region) obj).getPrefHeight()*factor);
		}
		else if(obj instanceof Circle) {
			((Circle) obj).setRadius(((Circle) obj).getRadius()*factor);
		}
		else if(obj instanceof Text) {
			((Text) obj).setFont(new Font(((Text) obj).getFont().getSize()*factor));
			((Text) obj).setWrappingWidth(((Text) obj).getWrappingWidth()*factor);
		}
		else if(obj instanceof TextField) {
			((TextField) obj).setFont(new Font(((TextField) obj).getFont().getSize()*factor));
			((Region) obj).setPrefSize(((Region) obj).getPrefWidth()*factor, ((Region) obj).getPrefHeight()*factor);
		}
		else if(obj instanceof Polygon) {
			for(Double coordonnee : ((Polygon)obj).getPoints())
				coordonnee = new Double(coordonnee.doubleValue()*factor);
		}
		else{
			((Region) obj).setPrefSize(((Region) obj).getPrefWidth()*factor, ((Region) obj).getPrefHeight()*factor);
		}
		
		//Padding resize
		if(obj instanceof Region) {
			((Region) obj).setPadding(new Insets(
					((Region) obj).getPadding().getTop()*factor,
					((Region) obj).getPadding().getRight()*factor,
					((Region) obj).getPadding().getBottom()*factor,
					((Region) obj).getPadding().getLeft()*factor)
			);
			
			//Margin resize
			Insets margin = new Insets(0,0,0,0);
			if(obj.getParent() instanceof HBox) {
				margin = ((HBox) obj.getParent()).getMargin(obj);
				if(margin != null) {
					Insets newMargin = new Insets(margin.getTop()*factor,margin.getRight()*factor,margin.getBottom()*factor,margin.getLeft()*factor);
					((HBox) obj.getParent()).setMargin(obj, newMargin);
				}
			}
			if(obj.getParent() instanceof VBox) {
				margin = ((VBox) obj.getParent()).getMargin(obj);
				if(margin != null) {
					Insets newMargin = new Insets(margin.getTop()*factor,margin.getRight()*factor,margin.getBottom()*factor,margin.getLeft()*factor);
					((VBox) obj.getParent()).setMargin(obj, newMargin);
				}
			}
			
		}
		
		
		
	}
	


	public static ArrayList<Node> getAllNodes(Parent root) {
	    ArrayList<Node> nodes = new ArrayList<Node>();
	    addAllDescendents(root, nodes);
	    return nodes;
	}
	
	private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
	    for (Node node : parent.getChildrenUnmodifiable()) {
	        nodes.add(node);
	        if (node instanceof Parent)
	            addAllDescendents((Parent)node, nodes);
	    }
	}
	
}
