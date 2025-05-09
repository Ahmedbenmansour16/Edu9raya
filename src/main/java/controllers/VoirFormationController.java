package controllers;

import entities.Formation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class VoirFormationController {

    @FXML
    private Label nomFormation;

    @FXML
    private Label descriptionFormation;

    @FXML
    private Label dateFormation;

    @FXML
    private Label categorieFormation;

    @FXML
    private ImageView imageFormation;

    @FXML
    private Button btnListeFormations;

    @FXML
    private Button btnAjouterFormation;

    @FXML
    private Button btnListeCours;

    @FXML
    private Button btnAjouterCours;

    @FXML
    private Button btnListeModules;

    @FXML
    private Button btnAjouterModule;

    @FXML
    private Button btnListeCategories;

    @FXML
    private Button btnAjouterCategorie;

    private Formation formation;
    private static final String UPLOADS_PATH = "C:\\Users\\monta\\OneDrive\\Bureau\\Edu9raya-Edu9raya\\public\\uploads\\";

    @FXML
    private void initialize() {
        // Configure navigation buttons
        btnListeFormations.setOnAction(event -> ouvrirListeFormations());
        btnAjouterFormation.setOnAction(event -> ouvrirAjouterFormation());
        btnListeCours.setOnAction(event -> ouvrirListeCours());
        btnAjouterCours.setOnAction(event -> ouvrirAjouterCours());
        btnListeModules.setOnAction(event -> ouvrirListeModules());
        btnAjouterModule.setOnAction(event -> ouvrirAjouterModule());
        btnListeCategories.setOnAction(event -> ouvrirListeCategories());
        btnAjouterCategorie.setOnAction(event -> ouvrirAjouterCategorie());
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
        displayDetails();
    }

    private void displayDetails() {
        nomFormation.setText(formation.getNom());
        descriptionFormation.setText(formation.getDescription());
        dateFormation.setText(formation.getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        categorieFormation.setText(formation.getCategorie() != null ? formation.getCategorie().getNom() : "Non spécifiée");

        // Load and display the image
        try {
            String fullImagePath = UPLOADS_PATH + formation.getImage();
            File file = new File(fullImagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageFormation.setImage(image);
            } else {
                System.out.println("Image file not found: " + fullImagePath);
                imageFormation.setImage(null); // Optionally set a placeholder image
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageFormation.setImage(null);
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'image: " + e.getMessage());
        }
    }

    @FXML
    private void retourner() {
        ouvrirListeFormations();
    }

    private void ouvrirListeFormations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeFormations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des formations.");
        }
    }

    private void ouvrirAjouterFormation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout de formation.");
        }
    }

    private void ouvrirListeCours() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/CoursList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des cours.");
        }
    }

    private void ouvrirAjouterCours() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/AddCous.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout de cours.");
        }
    }

    private void ouvrirListeModules() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Module/ModuleList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des modules.");
        }
    }

    private void ouvrirAjouterModule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Module/AddModule.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout de module.");
        }
    }

    private void ouvrirListeCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des catégories.");
        }
    }

    private void ouvrirAjouterCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout de catégorie.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}