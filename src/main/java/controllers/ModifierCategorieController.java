package controllers;

import entities.Categorie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.CategorieService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ModifierCategorieController {

    @FXML
    private TextField nomField;

    private CategorieService categorieService = new CategorieService();
    private Categorie categorie;

    // Regex pour valider le nom de la catégorie (lettres, chiffres, espaces et tirets)
    private static final Pattern NOM_PATTERN = Pattern.compile("^[\\p{L}0-9\\s-]{3,50}$");

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        nomField.setText(categorie.getNom());
    }

    @FXML
    private void modifier() {
        if (categorie == null) {
            afficherErreur("Erreur", "Aucune catégorie à modifier.");
            return;
        }

        String nouveauNom = nomField.getText().trim();

        // Validation du nom
        if (nouveauNom.isEmpty()) {
            afficherErreur("Erreur de saisie", "Le nom de la catégorie ne peut pas être vide.");
            return;
        }

        if (!NOM_PATTERN.matcher(nouveauNom).matches()) {
            afficherErreur("Format invalide", "Le nom doit contenir entre 3 et 50 caractères (lettres, chiffres, espaces et tirets uniquement).");
            return;
        }

        // Si le nom n'a pas changé, pas besoin de vérifier les doublons
        if (!nouveauNom.equals(categorie.getNom())) {
            try {
                // Vérification si la catégorie existe déjà
                if (categorieService.existeParNom(nouveauNom)) {
                    afficherErreur("Doublon", "Une catégorie avec ce nom existe déjà.");
                    return;
                }
            } catch (SQLException e) {
                afficherErreur("Erreur de base de données", "Erreur lors de la vérification du nom : " + e.getMessage());
                return;
            }
        }

        categorie.setNom(nouveauNom);
        try {
            categorieService.modifier(categorie);
            afficherInfo("Succès", "Catégorie modifiée avec succès !");
            // Fermer la fenêtre après modification
            retournerALaListe();
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", "Erreur lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Méthode utilitaire pour afficher une alerte d'erreur
     */
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Méthode utilitaire pour afficher une information
     */
    private void afficherInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void retournerALaListe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            afficherErreur("Erreur de navigation", "Erreur lors du chargement de la page ListCategorie: " + ex.getMessage());
        }
    }

    @FXML
    private void annuler() {
        retournerALaListe();
    }
}