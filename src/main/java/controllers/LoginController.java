package controllers;

import entities.Role;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button connectButton;

    @FXML
    private Button inscrireButton;

    @FXML
    private Label errorLabel;

    private UserService userService = new UserService();

    @FXML
    private void handleConnexion(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs");
            return;
        }

        try {
            User user = userService.authentifier(email, password);
            if (user != null) {
                // Stockage de l'utilisateur dans la session
                SessionController.getInstance().setCurrentUser(user);

                // Redirection selon le rôle
                redirectBasedOnRole(user);
            } else {
                afficherErreur("Email ou mot de passe incorrect");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de la connexion: " + e.getMessage());
        }
    }

    private void redirectBasedOnRole(User user) {
        try {
            String fxmlFile;

            // Déterminer la page à charger selon le rôle le plus élevé
            if (user.hasRole(Role.ADMIN)) {
                fxmlFile = "/ListeFormations.fxml";
            } else if (user.hasRole(Role.ENSEIGNANT)) {
                fxmlFile = "/EnseignantDashboard.fxml"; // À créer plus tard
            } else {
                fxmlFile = "/ListeFormationsFront.fxml";
            }

            // Charger la page correspondante
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Naviguer vers la page
            Stage stage = (Stage) connectButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors du chargement de l'interface: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirInscription(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Inscription.fxml"));
            Stage stage = (Stage) inscrireButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors du chargement de la page d'inscription");
        }
    }

    private void afficherErreur(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}