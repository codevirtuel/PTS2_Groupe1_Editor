package application.view;

public enum Interface {
	
	ACCUEIL, SELECTION_THEME, PARAMETRES, AJOUT_THEME, EDITION_THEME, EDITION_QUESTION, EDITION_ZONE;
	
	public String toString() {
		switch(this) {
		case ACCUEIL:
			return "Editeur - Accueil.fxml";
		case SELECTION_THEME:
			return "selectionnerTheme.fxml";
		case PARAMETRES:
			return "parametres.fxml";
		case AJOUT_THEME:
			return "CreationTheme.fxml";
		case EDITION_THEME:
			return "editionQuestionsZones.fxml";
		case EDITION_QUESTION:
			return "creationEditionQuestions.fxml";
		case EDITION_ZONE:
			return "Creation Zone.fxml";
		}
		return null;
	}

}
