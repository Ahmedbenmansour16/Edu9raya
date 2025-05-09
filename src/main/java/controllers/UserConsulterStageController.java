package controllers;

import entities.Stage;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import services.StageService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserConsulterStageController implements Initializable {
    @FXML
    private VBox stageContainer;

    @FXML
    private VBox stagesList;
    @FXML
    private TextField recherche;

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

    StageService Ss = new StageService();

    private void handleLogout() {
        // Déconnexion de l'utilisateur
        SessionController.getInstance().logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) userNameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page de connexion.");
        }
    }

    private void setupNavigation() {
        homeBtn.setOnAction(event -> naviguerVersHome());
        formationBtn.setOnAction(event -> naviguerVersFormations());
        reclamationsBtn.setOnAction(event -> naviguerVersReclamations());
        profileBtn.setOnAction(event -> naviguerVersProfile());
    }

    private void naviguerVersHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) homeBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page d'accueil.");
        }
    }

    private void naviguerVersFormations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeFormationsFront.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) formationBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page des formations.");
        }
    }

    private void naviguerVersReclamations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MesReclamations.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) reclamationsBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers vos réclamations.");
        }
    }

    private void naviguerVersProfile() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) profileBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers votre profil.");
        }
    }



    void loadStages() throws Exception {
        List<Stage> stages = Ss.recuperer();
        System.out.println(stages);
        stageContainer.getChildren().clear();
        for (Stage s : stages) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/StageUser.fxml"));
                VBox stageItem = loader.load();
                StageUserController stageController = loader.getController();
                stageController.setData(s);
                stageController.setMainController(this);
                stageContainer.getChildren().add(stageItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void filtrerStages(String searchText) {
        try {
            List<Stage> stages = Ss.recuperer(); // Fetch all stages from the database
            stageContainer.getChildren().clear(); // Clear the current stage list

            for (Stage s : stages) {
                // Check if the search text matches any relevant fields (e.g., title, enterprise, etc.)
                if (s.getTitre().toLowerCase().contains(searchText.toLowerCase()) ||
                        s.getEntreprise().toLowerCase().contains(searchText.toLowerCase()) ||
                        s.getDescription().toLowerCase().contains(searchText.toLowerCase())) {

                    // Load the matching stage into the UI
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/StageUser.fxml"));
                    VBox stageItem = loader.load();
                    StageUserController stageController = loader.getController();
                    stageController.setData(s);
                    stageController.setMainController(this);
                    stageContainer.getChildren().add(stageItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            loadStages();
        } catch (Exception e) {
            e.printStackTrace();
        }

        User currentUser = SessionController.getInstance().getCurrentUser();
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getPrenom() + " " + currentUser.getNom());
        } else {
            redirectToLogin();
            return;
        }
        setupNavigation();
        logoutBtn.setOnAction(event -> handleLogout());

        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.trim();
            filtrerStages(searchText);
        });
    }
}
