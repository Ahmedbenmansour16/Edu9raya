package controllers;

import entities.Reclamation;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ReclamationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NewReclamationController {
    @FXML
    private TextField subjectField;
    @FXML
    private TextArea justificationField;
    @FXML
    private Button uploadImageBtn;
    @FXML
    private ImageView imagePreview;
    @FXML
    private Button submitBtn;
    @FXML
    private Button backBtn;
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

    private ReclamationService reclamationService = new ReclamationService();
    private String imagePath;

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
        uploadImageBtn.setOnAction(e -> handleImageUpload());
        submitBtn.setOnAction(e -> handleSubmit());
        backBtn.setOnAction(e -> naviguerVersReclamations());
    }

    private void setupNavigation() {
        homeBtn.setOnAction(event -> naviguerVersHome());
        formationBtn.setOnAction(event -> naviguerVersFormation());
        profileBtn.setOnAction(event -> naviguerVersProfile());
        logoutBtn.setOnAction(event -> handleLogout());
    }

    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(uploadImageBtn.getScene().getWindow());
        if (file != null) {
            try {
                // Define the Symfony uploads directory path (as suggested by Claude)
                String symfonyUploadsPath = "C:\\Users\\monta\\OneDrive\\Bureau\\Edu9raya-Edu9raya\\public\\uploads";
                File uploadsDir = new File(symfonyUploadsPath);

                // Create the directory if it doesn't exist
                if (!uploadsDir.exists()) {
                    uploadsDir.mkdirs();
                }

                // Generate a unique filename (similar to Symfony's approach)
                String uniqueFileName = generateUniqueFileName(file.getName());

                // Define the destination file
                File destination = new File(uploadsDir, uniqueFileName);

                // Copy the image to the uploads directory
                Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Store only the filename (not the full path) for Symfony compatibility
                imagePath = uniqueFileName;

                // Display the image preview
                imagePreview.setImage(new Image(destination.toURI().toString()));

                System.out.println("Image saved to: " + destination.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de traiter l'image: " + e.getMessage());
            }
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        // Utiliser le même format que Symfony (MD5 + extension)
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 32) + extension;
    }
    // Helper methods (you may need to implement these)
    private String md5(String input) {
        // Implement MD5 hash generation
        return input; // Placeholder
    }

    private String uniqid() {
        // Implement unique ID generation
        return String.valueOf(System.currentTimeMillis()); // Placeholder
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void handleSubmit() {
        String sujet = subjectField.getText();
        String justification = justificationField.getText();
        if (sujet.isEmpty() || justification.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        User currentUser = SessionController.getInstance().getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non authentifié.");
            return;
        }

        // S'assurer que seul le nom du fichier est stocké, sans le chemin
        String imagePathToStore = imagePath;
        if (imagePath != null && (imagePath.contains("/") || imagePath.contains("\\"))) {
            // Extraire juste le nom du fichier si c'est un chemin complet
            imagePathToStore = imagePath.substring(imagePath.lastIndexOf(File.separator) + 1);
        }

        // Créer la réclamation avec le nom du fichier uniquement
        Reclamation reclamation = new Reclamation(currentUser.getId(), sujet, justification,
                imagePathToStore, LocalDateTime.now(), "en_attente");
        try {
            reclamationService.ajouter(reclamation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation envoyée avec succès.");
            naviguerVersReclamations();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'envoi de la réclamation: " + e.getMessage());
        }
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

    private void naviguerVersReclamations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Reclamation.fxml"));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers vos réclamations.");
        }
    }

    private void handleLogout() {
        SessionController.getInstance().logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) submitBtn.getScene().getWindow();
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