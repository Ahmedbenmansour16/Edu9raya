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

public class AjouterCategorieController {

    @FXML
    private TextField nomcategorie;

    // Instanciation du service pour gérer les catégories
    private CategorieService categorieService = new CategorieService();

    // Regex pour valider le nom de la catégorie (lettres, chiffres, espaces et tirets)
    private static final Pattern NOM_PATTERN = Pattern.compile("^[\\p{L}0-9\\s-]{3,50}$");

    /**
     * Méthode appelée lors du clic sur le bouton "Ajouter".
     * Après l'ajout, on revient à la page ListCategorie.
     */
    @FXML
    private void ajouterCategorie() {
        String nom = nomcategorie.getText().trim();

        // Validation du nom
        if (nom.isEmpty()) {
            afficherErreur("Erreur de saisie", "Le nom de la catégorie ne peut pas être vide.");
            return;
        }

        if (!NOM_PATTERN.matcher(nom).matches()) {
            afficherErreur("Format invalide", "Le nom doit contenir entre 3 et 50 caractères (lettres, chiffres, espaces et tirets uniquement).");
            return;
        }

        try {
            // Vérification si la catégorie existe déjà
            if (categorieService.existeParNom(nom)) {
                afficherErreur("Doublon", "Une catégorie avec ce nom existe déjà.");
                return;
            }

            Categorie categorie = new Categorie(nom);
            categorieService.ajouter(categorie);

            // Affichage d'une confirmation
            afficherInfo("Succès", "Catégorie ajoutée avec succès !");

            // Après l'ajout, rediriger vers la page ListCategorie
            retournerALaListe();
        } catch (SQLException ex) {
            afficherErreur("Erreur de base de données", "Erreur lors de l'ajout : " + ex.getMessage());
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

    /**
     * Méthode utilitaire pour charger l'interface ListCategorie.fxml.
     */
    @FXML
    private void retournerALaListe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomcategorie.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            afficherErreur("Erreur de navigation", "Erreur lors du chargement de la page ListCategorie: " + ex.getMessage());
        }
    }
}