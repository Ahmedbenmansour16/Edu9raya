package controllers;

import entities.Formation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class VoirFormationController {

    @FXML
    private VBox vboxDetails;

    private Formation formation;

    public void setFormation(Formation formation) {
        this.formation = formation;
        displayDetails();
    }

    private void displayDetails() {
        vboxDetails.getChildren().clear();

        Label lblInfo = new Label("Informations de la Formation");
        lblInfo.setStyle("-fx-font-size:18; -fx-font-weight:bold;");

        Label lblId = new Label("ID : " + formation.getId());
        Label lblNom = new Label("Nom : " + formation.getNom());
        Label lblDescription = new Label("Description : " + formation.getDescription());
        Label lblImage = new Label("Image : " + formation.getImage());
        Label lblDate = new Label("Date création : " + formation.getDateCreation().toString());
        Label lblCategorie = new Label("Catégorie : " + (formation.getCategorie() != null ? formation.getCategorie().getNom() : ""));

        vboxDetails.getChildren().addAll(lblInfo, lblId, lblNom, lblDescription, lblImage, lblDate, lblCategorie);

    }
    @FXML
    private void retourner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeFormations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) vboxDetails.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
