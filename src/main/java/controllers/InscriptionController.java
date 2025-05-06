package controllers;

import entities.Role;
import entities.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class InscriptionController implements Initializable {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button inscrireButton;

    @FXML
    private Hyperlink connectLink;

    @FXML
    private Label errorLabel;

    private UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurer le ComboBox avec les options de rôle
        roleComboBox.setItems(FXCollections.observableArrayList(
                "Étudiant", "Enseignant", "Administrateur"
        ));

        // Par défaut, sélectionner "Étudiant"
        roleComboBox.setValue("Étudiant");
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String selectedRole = roleComboBox.getValue();

        // Validation des champs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs");
            return;
        }

        if (!password.equals(confirmPassword)) {
            afficherErreur("Les mots de passe ne correspondent pas");
            return;
        }

        // Vérification de format email
        if (!isValidEmail(email)) {
            afficherErreur("Format d'email invalide");
            return;
        }

        // Création de l'utilisateur avec le rôle sélectionné
        User user = new User(email, password, nom, prenom);

        // Définir le rôle en fonction de la sélection
        user.setRoles(new ArrayList<>());

        switch (selectedRole) {
            case "Administrateur":
                user.addRole(Role.ADMIN);
                break;
            case "Enseignant":
                user.addRole(Role.ENSEIGNANT);
                break;
            case "Étudiant":
                user.addRole(Role.ETUDIANT);
                break;
        }

        try {
            userService.ajouter(user);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès");
            ouvrirConnexion(null);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'inscription: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        // Simple validation d'email avec regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    @FXML
    private void ouvrirConnexion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) connectLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors du chargement de la page de connexion");
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