package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ContenuService;
import services.NiveauService;

import java.io.File;
import java.sql.SQLException;

public class Niveau4Controller {

    @FXML
    private ComboBox<String> typeContenuCombo;

    @FXML
    private VBox dynamicContainer;

    private int formationId; // transmis depuis AjouterFormation
    private int niveauId;    // ID de ce niveau obtenu via NiveauService

    // Les champs dynamiques (déclarés ici pour être accessibles lors de l'ajout)
    private TextField descriptionField;
    private TextField filePathField;
    private Button selectFileButton;
    private TextField youtubeField;

    private NiveauService niveauService = new NiveauService();
    private ContenuService contenuService = new ContenuService();

    @FXML
    private void initialize() {
        // Remplir le ComboBox avec les types possibles
        typeContenuCombo.setItems(FXCollections.observableArrayList(
                "Description", "Image", "Vidéo YouTube", "PDF"
        ));

        // Lors du changement de valeur, afficher dynamiquement le champ correspondant
        typeContenuCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            afficherChampSelonType(newVal);
        });
    }

    // Méthode appelée par la vue pour transmettre le formationId et créer/récupérer le niveau
    public void setFormationId(int formationId) {
        this.formationId = formationId;
        try {
            // Création du niveau 4 avec ordre = 4 explicitement
            this.niveauId = niveauService.creerNiveauSiInexistant(new entities.Niveau(formationId, 4));
            System.out.println("Niveau créé avec ordre 4 pour formation " + formationId);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la création du niveau.");
        }
    }

    // Affiche dans le VBox dynamicContainer les champs correspondant au type sélectionné
    private void afficherChampSelonType(String type) {
        dynamicContainer.getChildren().clear();
        if (type == null) {
            return;
        }
        switch (type) {
            case "Description":
                Label lblDesc = new Label("Saisir la description :");
                descriptionField = new TextField();
                descriptionField.setPromptText("Entrez la description");
                dynamicContainer.getChildren().addAll(lblDesc, descriptionField);
                break;
            case "Image":
                Label lblImage = new Label("Uploader une image :");
                filePathField = new TextField();
                filePathField.setPromptText("Chemin de l'image");
                filePathField.setEditable(false);
                selectFileButton = new Button("Choisir un fichier");
                selectFileButton.setOnAction(e -> choisirFichier());
                dynamicContainer.getChildren().addAll(lblImage, filePathField, selectFileButton);
                break;
            case "Vidéo YouTube":
                Label lblVideo = new Label("Saisir le YouTube ID :");
                youtubeField = new TextField();
                youtubeField.setPromptText("YouTube ID");
                dynamicContainer.getChildren().addAll(lblVideo, youtubeField);
                break;
            case "PDF":
                Label lblPdf = new Label("Uploader un fichier PDF :");
                filePathField = new TextField();
                filePathField.setPromptText("Chemin du PDF");
                filePathField.setEditable(false);
                selectFileButton = new Button("Choisir un fichier");
                selectFileButton.setOnAction(e -> choisirFichier());
                dynamicContainer.getChildren().addAll(lblPdf, filePathField, selectFileButton);
                break;
            default:
                break;
        }
    }

    // Méthode pour choisir un fichier et renseigner le champ correspondant
    private void choisirFichier() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }
    @FXML
    private void ajouterContenu() {
        String type = typeContenuCombo.getSelectionModel().getSelectedItem();
        if (type == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un type de contenu.");
            typeContenuCombo.requestFocus();
            return;
        }

        try {
            switch (type) {
                case "Description":
                    String desc = descriptionField.getText().trim();
                    if (desc.isEmpty()) {
                        showAlert(Alert.AlertType.WARNING, "Attention", "La description est obligatoire.");
                        descriptionField.requestFocus();
                        return;
                    }

                    if (desc.length() < 10) {
                        showAlert(Alert.AlertType.WARNING, "Attention", "La description doit contenir au moins 10 caractères.");
                        descriptionField.requestFocus();
                        return;
                    }

                    entities.Contenu contenuDesc = new entities.Contenu(niveauId, "Description", "", desc, "");
                    contenuService.ajouter(contenuDesc);
                    break;

                case "Image":
                    String cheminImage = filePathField.getText().trim();
                    if (cheminImage.isEmpty()) {
                        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez choisir un fichier image.");
                        selectFileButton.requestFocus();
                        return;
                    }

                    // Vérifier l'extension du fichier
                    if (!cheminImage.toLowerCase().endsWith(".jpg") &&
                            !cheminImage.toLowerCase().endsWith(".jpeg") &&
                            !cheminImage.toLowerCase().endsWith(".png") &&
                            !cheminImage.toLowerCase().endsWith(".gif")) {
                        showAlert(Alert.AlertType.WARNING, "Attention", "Le fichier doit être une image (jpg, jpeg, png ou gif).");
                        selectFileButton.requestFocus();
                        return;
                    }

                    entities.Contenu contenuImage = new entities.Contenu(niveauId, "Image", cheminImage, "", "");
                    contenuService.ajouter(contenuImage);
                    break;

                case "Vidéo YouTube":
                    String videoId = youtubeField.getText().trim();
                    if (videoId.isEmpty()) {
                        showAlert(Alert.AlertType.WARNING, "Attention", "L'ID YouTube est obligatoire.");
                        youtubeField.requestFocus();
                        return;
                    }

                    if (!videoId.matches("[A-Za-z0-9_-]{11}")) {
                        showAlert(Alert.AlertType.WARNING, "Attention", "Format d'ID YouTube invalide. Un ID YouTube contient généralement 11 caractères.");
                        youtubeField.requestFocus();
                        return;
                    }

                    entities.Contenu contenuVideo = new entities.Contenu(niveauId, "Vidéo YouTube", "", "", videoId);
                    contenuService.ajouter(contenuVideo);
                    break;

                case "PDF":
                    String cheminPDF = filePathField.getText().trim();
                    if (cheminPDF.isEmpty()) {
                        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez choisir un fichier PDF.");
                        selectFileButton.requestFocus();
                        return;
                    }

                    if (!cheminPDF.toLowerCase().endsWith(".pdf")) {
                        showAlert(Alert.AlertType.WARNING, "Attention", "Le fichier doit être au format PDF.");
                        selectFileButton.requestFocus();
                        return;
                    }

                    entities.Contenu contenuPDF = new entities.Contenu(niveauId, "PDF", cheminPDF, "", "");
                    contenuService.ajouter(contenuPDF);
                    break;

                default:
                    break;
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Contenu ajouté pour ce niveau !");
            // Réinitialiser le formulaire de ce niveau
            dynamicContainer.getChildren().clear();
            typeContenuCombo.getSelectionModel().clearSelection();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter le contenu.");
        }
    }

    // Passe au niveau suivant (par exemple, ouvrir Niveau5.fxml)
    @FXML
    private void passerNiveau5() {
        // Vous pouvez ajouter ici la logique d'enregistrement ou de vérification du niveau actuel.
        try {
            // Chargement de l'interface du Niveau 5
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Niveau5.fxml"));
            Parent root = loader.load();

            // Transmettre formationId à Niveau5Controller
            Niveau5Controller controller = loader.getController();
            controller.setFormationId(formationId);

            // Changement de scène
            Stage stage = (Stage) typeContenuCombo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le Niveau 5.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
