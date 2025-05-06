package controllers;

import entities.Formation;
import entities.Categorie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.FormationService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ModifierFormationController {

    @FXML
    private TextField nomformation;
    @FXML
    private TextField descriptionformation;
    @FXML
    private TextField textimage;

    private Formation formation;
    private FormationService formationService = new FormationService();

    public void setFormation(Formation formation) {
        this.formation = formation;
        nomformation.setText(formation.getNom());
        descriptionformation.setText(formation.getDescription());
        textimage.setText(formation.getImage());
        // Si besoin, gérez la catégorie ou d'autres champs complémentaires.
    }

    @FXML
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        // Définir les filtres pour n'afficher que les images
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Images", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        // Obtenir la fenêtre actuelle
        Stage stage = (Stage) nomformation.getScene().getWindow();

        // Afficher le dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            textimage.setText(selectedFile.getName());
        }
    }
    @FXML
    private void annuler() {
        retournerALaListe();
    }

    @FXML
    private void retournerALaListe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeFormations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomformation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à la liste des formations.");
        }
    }
    @FXML
    private void enregistrerModification() {
        // Récupérer les valeurs des champs
        String nom = nomformation.getText().trim();
        String description = descriptionformation.getText().trim();
        String image = textimage.getText().trim();

        // Validation du nom
        if (nom.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Le nom de la formation est obligatoire.");
            nomformation.requestFocus();
            return;
        }

        if (nom.length() < 3 || nom.length() > 50) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Le nom doit contenir entre 3 et 50 caractères.");
            nomformation.requestFocus();
            return;
        }

        // Validation de la description
        if (description.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "La description est obligatoire.");
            descriptionformation.requestFocus();
            return;
        }

        if (description.length() < 10) {
            showAlert(Alert.AlertType.WARNING, "Attention", "La description doit contenir au moins 10 caractères.");
            descriptionformation.requestFocus();
            return;
        }

        // Mise à jour de l'objet formation
        formation.setNom(nom);
        formation.setDescription(description);
        formation.setImage(image);

        try {
            formationService.modifier(formation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation modifiée avec succès !");
            retournerALaListe();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de la formation.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
