package controllers;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class EnseignantDashboardController {

    @FXML
    private Label labelUserName;

    @FXML
    private void initialize() {
        // Récupérer l'utilisateur courant
        User currentUser = SessionController.getInstance().getCurrentUser();
        if (currentUser != null) {
            labelUserName.setText("Bienvenue, " + currentUser.getPrenom() + " " + currentUser.getNom());
        }
    }

    @FXML
    private void handleLogout() {
        // Déconnexion
        SessionController.getInstance().logout();

        // Redirection vers la page de connexion
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) labelUserName.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la déconnexion");
        }
    }

    @FXML
    private void viewFormations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeFormationsFront.fxml"));
            Stage stage = (Stage) labelUserName.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des formations");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}