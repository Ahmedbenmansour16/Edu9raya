package controllers;

import entities.Reclamation;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ReclamationService;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReclamationFrontController {
    @FXML
    private VBox reclamationsContainer;
    @FXML
    private Button newReclamationBtn;
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
    @FXML
    private HBox successMessage;
    @FXML
    private Button closeSuccessBtn;

    private ReclamationService reclamationService = new ReclamationService();
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
        chargerReclamations();
        closeSuccessBtn.setOnAction(e -> successMessage.setVisible(false));
    }

    private void setupNavigation() {
        homeBtn.setOnAction(event -> naviguerVersHome());
        formationBtn.setOnAction(event -> naviguerVersFormation());
        profileBtn.setOnAction(event -> naviguerVersProfile());
        logoutBtn.setOnAction(event -> handleLogout());
        newReclamationBtn.setOnAction(event -> naviguerVersNewReclamation());
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

    private void naviguerVersNewReclamation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/NewReclamation.fxml"));
            Stage stage = (Stage) newReclamationBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la page de nouvelle réclamation.");
        }
    }

    private void chargerReclamations() {
        try {
            reclamationsContainer.getChildren().clear();
            List<Reclamation> reclamations = reclamationService.recuperer();
            for (Reclamation reclamation : reclamations) {
                HBox row = new HBox(10);
                row.getStyleClass().add("reclamation-row");
                Label sujetLabel = new Label(reclamation.getSujet());
                Label dateLabel = new Label(reclamation.getDateEnvoi().format(formatter));
                Label statutLabel = new Label(reclamation.getStatut());
                statutLabel.getStyleClass().add("status-label");
                Button viewBtn = new Button("Voir");
                viewBtn.getStyleClass().add("voir-formation-btn");
                viewBtn.setOnAction(e -> ouvrirDetailReclamation(reclamation));
                row.getChildren().addAll(sujetLabel, dateLabel, statutLabel, viewBtn);
                reclamationsContainer.getChildren().add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des réclamations.");
        }
    }

    private void ouvrirDetailReclamation(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailReclamation.fxml"));
            Parent root = loader.load();
            DetailReclamationController controller = loader.getController();
            controller.setReclamation(reclamation);
            Stage stage = (Stage) reclamationsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir les détails de la réclamation.");
        }
    }

    private void handleLogout() {
        SessionController.getInstance().logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) reclamationsContainer.getScene().getWindow();
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