package controllers;

import entities.Reclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.ReclamationService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class DetailReclamationAdminController {

    @FXML
    private Label subjectLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label statutLabel;
    @FXML
    private TextArea justificationArea;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea adminResponseArea;
    @FXML
    private Button saveBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button logoutBtn;

    private Reclamation reclamation;
    private ReclamationService reclamationService = new ReclamationService();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private void initialize() {
        logoutBtn.setOnAction(event -> handleLogout());
        saveBtn.setOnAction(event -> saveChanges());
        backBtn.setOnAction(event -> naviguerVersReclamations());
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        if (reclamation != null) {
            subjectLabel.setText("Sujet: " + reclamation.getSujet());
            dateLabel.setText("Date: " + reclamation.getDateEnvoi().format(formatter));
            statutLabel.setText("Statut: " + reclamation.getStatut());
            justificationArea.setText(reclamation.getJustification());
            adminResponseArea.setText(reclamation.getAdminResponse() != null ? reclamation.getAdminResponse() : "");
            if (reclamation.getImagePath() != null && !reclamation.getImagePath().isEmpty()) {
                imageView.setImage(new Image("file:" + reclamation.getImagePath()));
            }
        }
    }

    private void saveChanges() {
        if (reclamation != null) {
            String newResponse = adminResponseArea.getText();
            reclamation.setAdminResponse(newResponse);
            if (!newResponse.isEmpty()) {
                reclamation.setStatut("Traité");
            }
            try {
                reclamationService.modifier(reclamation);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation mise à jour avec succès.");
                naviguerVersReclamations();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour de la réclamation: " + e.getMessage());
            }
        }
    }

    private void handleLogout() {
        SessionController.getInstance().logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page de connexion.");
        }
    }

    private void naviguerVersReclamations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeReclamationsAdmin.fxml"));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la gestion des réclamations.");
        }
    }

    @FXML
    private void ouvrirListeFormations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeFormations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des formations");
        }
    }

    @FXML
    private void ajouterFormation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setTitle("Ajouter Formation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout.");
        }
    }

    @FXML
    private void ouvrirListeCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des catégories");
        }
    }

    @FXML
    private void ouvrirAjouterCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout de catégorie");
        }
    }

    @FXML
    private void ouvrirListeReclamations() {
        naviguerVersReclamations();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}