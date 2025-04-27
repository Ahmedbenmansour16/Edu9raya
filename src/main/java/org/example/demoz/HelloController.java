package org.example.demoz;

import controller.PdfViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    public void initialize() {
        // Charge par défaut la vue "BookView" (pas FrontBookView ici, car FrontBookView veut un full window)
        loadView("/view/BookView.fxml");
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
        openFrontBookView(); // Different method because we want a NEW Scene!
    }

    @FXML
    private void onStatBookViewClick() {
        loadView("/view/StatBookView.fxml");
    }

    @FXML
    private void onPdfViewButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PdfView.fxml"));
            AnchorPane pane = loader.load();
            PdfViewController ctrl = loader.getController();
            // Charge un PDF par défaut (à remplacer par le chemin réel ou sélection utilisateur)
            ctrl.loadPdf("/path/to/default.pdf");
            contentPane.getChildren().setAll(pane);
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge un FXML dans le contentPane.
     */
    private void loadView(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(node);
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre FrontBookView.fxml dans une nouvelle Scène (sans Hello-view autour).
     */
    private void openFrontBookView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FrontBookView.fxml"));
            Parent root = loader.load();

            // Récupère la Stage actuelle
            Stage stage = (Stage) contentPane.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
