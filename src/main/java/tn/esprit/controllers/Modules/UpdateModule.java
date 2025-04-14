package tn.esprit.controllers.Modules;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.models.Module;
import tn.esprit.services.ModuleService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.UUID;

public class UpdateModule implements Initializable {

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField enseignantTextField;

    @FXML
    private TextField dureeTextField;

    @FXML
    private TextField coefficientTextField;

    @FXML
    private ImageView imageView;

    @FXML
    private Button choisirImageButton;

    @FXML
    private Label messageErreur;

    @FXML
    private Button updateButton;

    private Module module;
    private final ModuleService moduleService = new ModuleService();
    private static final String DEFAULT_STYLE = "-fx-border-color: #34C759; -fx-border-width: 1; -fx-border-radius: 5;";
    private static final String ERROR_STYLE = "-fx-border-color: #FF4D4D; -fx-border-width: 2; -fx-border-radius: 5;";
    private static final String IMAGE_DIRECTORY = "C:/freelance/edu9raya/src/main/resources/images/"; // Absolute path for image storage
    private String imagePath; // Stores the filename of the selected or existing image

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageErreur.setVisible(false);
        messageErreur.setOpacity(0.0);

        // Initialize default styles for all fields
        nomTextField.setStyle(DEFAULT_STYLE);
        enseignantTextField.setStyle(DEFAULT_STYLE);
        dureeTextField.setStyle(DEFAULT_STYLE);
        coefficientTextField.setStyle(DEFAULT_STYLE);

        // Add focus listeners to remove error border when the user starts editing
        addFocusListener(nomTextField);
        addFocusListener(enseignantTextField);
        addFocusListener(dureeTextField);
        addFocusListener(coefficientTextField);

        // Add input filter to dureeTextField to allow only numeric input
        dureeTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                dureeTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Add input filter to coefficientTextField to allow only numeric input
        coefficientTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                coefficientTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Ensure the images directory exists
        File imageDir = new File(IMAGE_DIRECTORY);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
    }

    private void addFocusListener(javafx.scene.Node node) {
        node.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                node.setStyle(DEFAULT_STYLE); // Reset to default border when focused
            }
        });
    }

    // Initialize form with module data
    public void initializeModule(Module module) {
        this.module = module;
        nomTextField.setText(module.getNom());
        enseignantTextField.setText(module.getEnseignant());
        dureeTextField.setText(String.valueOf(module.getDuree()));
        coefficientTextField.setText(module.getCoefficient());
        imagePath = module.getImage(); // Store current image filename

        // Load the existing image if it exists
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(IMAGE_DIRECTORY + imagePath);
            if (imageFile.exists()) {
                imageView.setImage(new Image(imageFile.toURI().toString()));
            } else {
                // Fallback to default image if the file is missing
                imageView.setImage(new Image(getClass().getResource("/images/downloadImage.png").toExternalForm()));
                imagePath = null; // Reset imagePath to ensure validation catches missing images
            }
        } else {
            // Use default image if no image is set
            imageView.setImage(new Image(getClass().getResource("/images/downloadImage.png").toExternalForm()));
            imagePath = null;
        }
    }

    @FXML
    private void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Generate a unique filename
                String originalFilename = selectedFile.getName();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = "module-" + UUID.randomUUID().toString() + extension;

                // Copy the file to the images folder
                Path targetPath = Paths.get(IMAGE_DIRECTORY, newFilename);
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Store the new filename
                imagePath = newFilename;

                // Display the image in the ImageView
                Image image = new Image(targetPath.toUri().toString());
                imageView.setImage(image);
            } catch (IOException e) {
                showErrorMessage("Error copying the image: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean validateFields() {
        messageErreur.setVisible(false);
        messageErreur.setOpacity(0.0);

        // Validate Name if changed
        String name = nomTextField.getText().trim();
        if (!name.equals(module.getNom())) {
            if (name.isEmpty()) {
                showErrorMessage("The name is required.");
                highlightField(nomTextField);
                return false;
            }
            if (name.length() < 3) {
                showErrorMessage("The name must be at least 3 characters long.");
                highlightField(nomTextField);
                return false;
            }
            if (!name.matches("^[a-zA-Z0-9\\s]+$")) {
                showErrorMessage("The name can only contain letters, numbers, and spaces.");
                highlightField(nomTextField);
                return false;
            }
        }

        // Validate Teacher if changed
        String teacher = enseignantTextField.getText().trim();
        if (!teacher.equals(module.getEnseignant())) {
            if (teacher.isEmpty()) {
                showErrorMessage("The teacher name is required.");
                highlightField(enseignantTextField);
                return false;
            }
            if (teacher.length() < 3) {
                showErrorMessage("The teacher name must be at least 3 characters long.");
                highlightField(enseignantTextField);
                return false;
            }
            if (!teacher.matches("^[a-zA-Z\\s]+$")) {
                showErrorMessage("The teacher name can only contain letters and spaces.");
                highlightField(enseignantTextField);
                return false;
            }
        }

        // Validate Duration if changed
        String durationStr = dureeTextField.getText().trim();
        int duration;
        if (!durationStr.equals(String.valueOf(module.getDuree()))) {
            if (durationStr.isEmpty()) {
                showErrorMessage("The duration is required.");
                highlightField(dureeTextField);
                return false;
            }
            try {
                duration = Integer.parseInt(durationStr);
                if (duration <= 0) {
                    showErrorMessage("The duration must be a positive number of hours.");
                    highlightField(dureeTextField);
                    return false;
                }
                if (duration > 1000) {
                    showErrorMessage("The duration cannot exceed 1000 hours.");
                    highlightField(dureeTextField);
                    return false;
                }
            } catch (NumberFormatException e) {
                showErrorMessage("The duration must be a valid number of hours.");
                highlightField(dureeTextField);
                return false;
            }
        }

        // Validate Coefficient if changed
        String coefficient = coefficientTextField.getText().trim();
        if (!coefficient.equals(module.getCoefficient())) {
            if (coefficient.isEmpty()) {
                showErrorMessage("The coefficient is required.");
                highlightField(coefficientTextField);
                return false;
            }
            if (!coefficient.matches("^[0-9]+$")) {
                showErrorMessage("The coefficient must be a positive integer.");
                highlightField(coefficientTextField);
                return false;
            }
            int coeffValue = Integer.parseInt(coefficient);
            if (coeffValue <= 0) {
                showErrorMessage("The coefficient must be greater than 0.");
                highlightField(coefficientTextField);
                return false;
            }
        }

        // Validate Image
        if (imagePath == null || imagePath.isEmpty()) {
            showErrorMessage("Please choose an image.");
            return false;
        }

        return true;
    }

    @FXML
    void btnUpdateModule(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        // Update only changed fields
        String name = nomTextField.getText().trim();
        if (!name.equals(module.getNom())) {
            module.setNom(name);
        }

        String teacher = enseignantTextField.getText().trim();
        if (!teacher.equals(module.getEnseignant())) {
            module.setEnseignant(teacher);
        }

        String durationStr = dureeTextField.getText().trim();
        if (!durationStr.equals(String.valueOf(module.getDuree()))) {
            int duration = Integer.parseInt(durationStr);
            module.setDuree(duration);
        }

        String coefficient = coefficientTextField.getText().trim();
        if (!coefficient.equals(module.getCoefficient())) {
            module.setCoefficient(coefficient);
        }

        // Update image if changed
        if (imagePath != null && !imagePath.equals(module.getImage())) {
            module.setImage(imagePath);
        }

        // Save changes to the database
        try {
            moduleService.update(module);
            // Show success alert and redirect on close
            showSuccessAlert(event);
        } catch (Exception e) {
            showErrorMessage("Error while updating: " + e.getMessage());
        }
    }

    private void showSuccessAlert(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Module Updated");
        alert.setContentText("The module has been updated successfully!");

        // Apply custom styles to match template CSS
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #F5F5F5; -fx-font-family: 'Nunito';");
        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #34C759; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
        );

        // Redirect to module list when alert is closed
        alert.setOnHidden(evt -> showModuleList(event));
        alert.showAndWait();
    }

    private void showErrorMessage(String message) {
        messageErreur.setText(message);
        messageErreur.setStyle("-fx-text-fill: #FF4D4D; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: bold;");
        messageErreur.setVisible(true);
        FadeTransition fade = new FadeTransition(Duration.millis(500), messageErreur);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void highlightField(javafx.scene.Node field) {
        field.setStyle(ERROR_STYLE);
    }

    @FXML
    void showModuleList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modules/ModuleList.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error while loading ModuleList.fxml: " + e.getMessage());
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    void showAddModule(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modules/AddModule.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error while loading AddModule.fxml: " + e.getMessage());
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }
}