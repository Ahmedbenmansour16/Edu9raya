package controllers;

import entities.Reclamation;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ReclamationService;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NewReclamationController {
    @FXML
    private TextField subjectField;
    @FXML
    private TextArea justificationField;
    @FXML
    private Button uploadImageBtn;
    @FXML
    private ImageView imagePreview;
    @FXML
    private Button submitBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button formationBtn;
    @FXML
    private Button reclamationsBtn;
    @FXML
    private Button profileBtn;
    @FXML
    private Label userNameLabel;
    @FXML
    private Button logoutBtn;

    private ReclamationService reclamationService = new ReclamationService();
    private String imagePath;

    @FXML
    private void initialize() {
        User currentUser = SessionController.getInstance().getCurrentUser();
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getPrenom() + " " + currentUser.getNom());
        } else {
            redirectToLogin();
            return;
        }

        setupNavigation();
        uploadImageBtn.setOnAction(e -> handleImageUpload());
        submitBtn.setOnAction(e -> handleSubmit());
        backBtn.setOnAction(e -> naviguerVersReclamations());
    }

    private void setupNavigation() {
        homeBtn.setOnAction(event -> naviguerVersHome());
        formationBtn.setOnAction(event -> naviguerVersFormation());
        profileBtn.setOnAction(event -> naviguerVersProfile());
        logoutBtn.setOnAction(event -> handleLogout());
    }

    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(uploadImageBtn.getScene().getWindow());
        if (file != null) {
            imagePath = file.getAbsolutePath();
            imagePreview.setImage(new Image("file:" + imagePath));
        }
    }

    private void handleSubmit() {
        String sujet = subjectField.getText();
        String justification = justificationField.getText();
        if (sujet.isEmpty() || justification.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        // Set dateEnvoi to current time and statut to "en attente"
        Reclamation reclamation = new Reclamation(sujet, justification, imagePath, LocalDateTime.now(), "en attente");
        try {
            reclamationService.ajouter(reclamation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation envoyée avec succès.");
            naviguerVersReclamations();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'envoi de la réclamation.");
        }
    }

    private void naviguerVersHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page d'accueil.");
        }
    }

    private void naviguerVersFormation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeFormationsfront.fxml"));
            Stage stage = (Stage) formationBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers les formations.");
        }
    }

    private void naviguerVersProfile() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
            Stage stage = (Stage) profileBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers votre profil.");
        }
    }

    private void naviguerVersReclamations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Reclamation.fxml"));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers vos réclamations.");
        }
    }

    private void handleLogout() {
        SessionController.getInstance().logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page de connexion.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}