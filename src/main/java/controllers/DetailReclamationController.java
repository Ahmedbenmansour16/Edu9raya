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
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DetailReclamationController {
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
    private Label adminResponseLabel;
    @FXML
    private TextArea adminResponseArea;
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

    private Reclamation reclamation;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
        backBtn.setOnAction(e -> naviguerVersReclamations());
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        if (reclamation != null) {
            subjectLabel.setText("Sujet: " + reclamation.getSujet());
            dateLabel.setText("Date: " + reclamation.getDateEnvoi().format(formatter));
            statutLabel.setText("Statut: " + reclamation.getStatut());
            justificationArea.setText(reclamation.getJustification());
            if (reclamation.getImagePath() != null && !reclamation.getImagePath().isEmpty()) {
                imageView.setImage(new Image("file:" + reclamation.getImagePath()));
            }
            if (reclamation.getAdminResponse() != null && !reclamation.getAdminResponse().isEmpty()) {
                adminResponseLabel.setVisible(true);
                adminResponseArea.setVisible(true);
                adminResponseArea.setText(reclamation.getAdminResponse());
            }
        }
    }

    private void setupNavigation() {
        homeBtn.setOnAction(event -> naviguerVersHome());
        formationBtn.setOnAction(event -> naviguerVersFormation());
        profileBtn.setOnAction(event -> naviguerVersProfile());
        logoutBtn.setOnAction(event -> handleLogout());
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
            Parent root = FXMLLoader.load(getClass().getResource("/ListeFormations.fxml"));
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
            Stage stage = (Stage) backBtn.getScene().getWindow();
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