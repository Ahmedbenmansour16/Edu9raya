package controllers;

import entities.Categorie;
import entities.Formation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.CategorieService;
import services.FormationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class AjouterFormation {

    @FXML
    private TextField nomformation;

    @FXML
    private TextField descriptionformation;

    @FXML
    private TextField textimage;

    @FXML
    private ComboBox<Categorie> choisircategorie;

    @FXML
    private Button enregistrer;

    private FormationService formationService = new FormationService();
    private CategorieService categorieService = new CategorieService();
    private ObservableList<Categorie> listeCategorie = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Charger la liste des catégories
        try {
            List<Categorie> categories = categorieService.recuperer();
            listeCategorie.setAll(categories);

            choisircategorie.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Categorie item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getNom());
                }
            });
            choisircategorie.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Categorie item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getNom());
                }
            });

            choisircategorie.setItems(listeCategorie);

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des catégories.");
        }
    }

    @FXML
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Images", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Define the Symfony uploads directory path
                String symfonyUploadsPath = "C:\\Users\\monta\\OneDrive\\Bureau\\Edu9raya-Edu9raya\\public\\uploads\\";
                File uploadsDir = new File(symfonyUploadsPath);

                // Create the directory if it doesn't exist
                if (!uploadsDir.exists()) {
                    uploadsDir.mkdirs();
                }

                // Generate a unique filename (similar to Symfony's approach)
                String uniqueFileName = generateUniqueFileName(selectedFile.getName());

                // Define the destination file
                File destFile = new File(uploadsDir, uniqueFileName);

                // Copy the image to the uploads directory
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Store only the filename (not the full path) for Symfony compatibility
                textimage.setText(uniqueFileName);

                System.out.println("Image saved to: " + destFile.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de copier l'image: " + e.getMessage());
            }
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        // Utiliser le même format que Symfony (UUID + extension)
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 32) + extension;
    }

    @FXML
    private void enregistrerFormation() {
        String nom = nomformation.getText().trim();
        String description = descriptionformation.getText().trim();
        String image = textimage.getText().trim();
        Categorie categorie = choisircategorie.getSelectionModel().getSelectedItem();

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

        // Validation de la catégorie
        if (categorie == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une catégorie.");
            choisircategorie.requestFocus();
            return;
        }

        // Création de l'objet Formation
        Formation formation = new Formation(categorie, nom, description, image);
        try {
            int formationId = formationService.ajouter(formation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation ajoutée avec succès !");
            ouvrirNiveau1(formationId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'enregistrement de la formation.");
        }
    }

    private void ouvrirNiveau1(int formationId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Niveau1.fxml"));
            Parent root = loader.load();

            // Récupérer le controller de Niveau1
            Niveau1Controller controller = loader.getController();
            controller.setFormationId(formationId);

            Stage stage = (Stage) nomformation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le niveau 1.");
        }
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}