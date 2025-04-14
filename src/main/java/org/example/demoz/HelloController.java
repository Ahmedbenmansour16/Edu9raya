package org.example.demoz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class HelloController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    public void initialize() {
        // Charger une vue par défaut (ici, par exemple la vue Book Front)
        loadView("/view/FrontBookView.fxml");
    }

    @FXML
    private void onBookViewButtonClick() {
        loadView("/view/BookView.fxml");
    }

    @FXML
    private void onCatViewButtonClick() {
        loadView("/view/CatView.fxml");
    }

    @FXML
    private void onFrontBookViewButtonClick() {
        // Cette méthode charge la page "Book Front" (par exemple FrontBookView.fxml)
        loadView("/view/FrontBookView.fxml");
    }

    /**
     * Méthode utilitaire qui charge le fichier FXML spécifié dans la zone de contenu.
     * @param fxmlPath Chemin du fichier FXML à charger.
     */
    private void loadView(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(node);
            // Ancrer le node pour qu'il occupe tout l'espace de contentPane
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
