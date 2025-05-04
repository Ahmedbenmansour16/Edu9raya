package controllers;

import entities.Categorie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DetailCategorieController {

    @FXML
    private Label labelNom;

    // Méthode appelée depuis le contrôleur principal pour envoyer la catégorie
    public void setCategorie(Categorie categorie) {
        labelNom.setText("Nom : " + categorie.getNom());
    }

    @FXML
    private void fermer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) labelNom.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
