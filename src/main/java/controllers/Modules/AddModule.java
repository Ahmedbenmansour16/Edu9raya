package controllers.Modules;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import entities.Module;
import services.ModuleService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class AddModule {
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

    private final ModuleService moduleService = new ModuleService();
    private static final String DEFAULT_STYLE = "-fx-border-color: #34C759; -fx-border-width: 1; -fx-border-radius: 5;";
    private static final String ERROR_STYLE = "-fx-border-color: #FF4D4D; -fx-border-width: 2; -fx-border-radius: 5;";
    private static final String IMAGE_DIRECTORY = "C:/freelance/edu9raya/src/main/resources/images/";
    private String imagePath;

    @FXML
    private void initialize() {
        messageErreur.setVisible(false);
        messageErreur.setOpacity(0.0);
        nomTextField.setStyle(DEFAULT_STYLE);
        enseignantTextField.setStyle(DEFAULT_STYLE);
        dureeTextField.setStyle(DEFAULT_STYLE);
        coefficientTextField.setStyle(DEFAULT_STYLE);
        addFocusListener(nomTextField);
        addFocusListener(enseignantTextField);
        addFocusListener(dureeTextField);
        addFocusListener(coefficientTextField);
        dureeTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                dureeTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        File imageDir = new File(IMAGE_DIRECTORY);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
    }

    private void addFocusListener(javafx.scene.Node node) {
        node.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                node.setStyle(DEFAULT_STYLE);
            }
        });
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
                String originalFilename = selectedFile.getName();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = "module-" + UUID.randomUUID().toString() + extension;
                Path targetPath = Paths.get(IMAGE_DIRECTORY, newFilename);
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = newFilename;
                Image image = new Image(targetPath.toUri().toString());
                imageView.setImage(image);
            } catch (IOException e) {
                showErrorMessage("Error copying the image: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnAjouterModule(ActionEvent event) {
        messageErreur.setVisible(false);
        messageErreur.setOpacity(0.0);
        resetFieldStyles();
        String name = nomTextField.getText().trim();
        String teacher = enseignantTextField.getText().trim();
        String durationStr = dureeTextField.getText().trim();
        String coefficient = coefficientTextField.getText().trim();
        if (name.isEmpty()) {
            showErrorMessage("The name is required.");
            highlightField(nomTextField);
            return;
        }
        if (name.length() < 3) {
            showErrorMessage("The name must be at least 3 characters long.");
            highlightField(nomTextField);
            return;
        }
        if (!name.matches("^[a-zA-Z0-9\\s]+$")) {
            showErrorMessage("The name can only contain letters, numbers, and spaces.");
            highlightField(nomTextField);
            return;
        }
        if (teacher.isEmpty()) {
            showErrorMessage("The teacher name is required.");
            highlightField(enseignantTextField);
            return;
        }
        if (teacher.length() < 3) {
            showErrorMessage("The teacher name must be at least 3 characters long.");
            highlightField(enseignantTextField);
            return;
        }
        if (!teacher.matches("^[a-zA-Z\\s]+$")) {
            showErrorMessage("The teacher name can only contain letters and spaces.");
            highlightField(enseignantTextField);
            return;
        }
        if (durationStr.isEmpty()) {
            showErrorMessage("The duration is required.");
            highlightField(dureeTextField);
            return;
        }
        int duration;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration <= 0) {
                showErrorMessage("The duration must be a positive number of hours.");
                highlightField(dureeTextField);
                return;
            }
            if (duration > 1000) {
                showErrorMessage("The duration cannot exceed 1000 hours.");
                highlightField(dureeTextField);
                return;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("The duration must be a valid number of hours.");
            highlightField(dureeTextField);
            return;
        }
        if (coefficient.isEmpty()) {
            showErrorMessage("The coefficient is required.");
            highlightField(coefficientTextField);
            return;
        }
        if (!coefficient.matches("^[0-9]+$")) {
            showErrorMessage("The coefficient must be a positive integer.");
            highlightField(coefficientTextField);
            return;
        }
        int coeffValue = Integer.parseInt(coefficient);
        if (coeffValue <= 0) {
            showErrorMessage("The coefficient must be greater than 0.");
            highlightField(coefficientTextField);
            return;
        }
        if (imagePath == null || imagePath.isEmpty()) {
            showErrorMessage("Please choose an image.");
            return;
        }
        try {
            Module module = new Module(name, teacher, duration, coefficient, imagePath);
            moduleService.add(module);
            clearForm();
            showSuccessAlert(event);
        } catch (Exception e) {
            showErrorMessage("Error while adding: " + e.getMessage());
        }
    }

    private void showSuccessAlert(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Module Added");
        alert.setContentText("The module has been added successfully!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #F5F5F5; -fx-font-family: 'Nunito';");
        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #34C759; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
        );
        alert.setOnHidden(evt -> showModuleList(event));
        alert.showAndWait();
    }

    private void highlightField(javafx.scene.Node field) {
        field.setStyle(DEFAULT_STYLE);
    }

    private void resetFieldStyles() {
        nomTextField.setStyle(DEFAULT_STYLE);
        enseignantTextField.setStyle(DEFAULT_STYLE);
        dureeTextField.setStyle(DEFAULT_STYLE);
        coefficientTextField.setStyle(DEFAULT_STYLE);
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

    private void clearForm() {
        nomTextField.clear();
        enseignantTextField.clear();
        dureeTextField.clear();
        coefficientTextField.clear();
        imageView.setImage(new Image(getClass().getResource("/images/downloadImage.png").toExternalForm()));
        imagePath = null;
        resetFieldStyles();
    }

    @FXML
    private void showModuleList(ActionEvent event) {
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
    private void showAddModule(ActionEvent event) {
        clearForm();
        messageErreur.setVisible(false);
        messageErreur.setOpacity(0.0);
    }

    // Sidebar navigation methods for sidebar buttons
    @FXML
    private void ouvrirListeCours() { openView("/Cours/CoursList.fxml"); }
    @FXML
    private void ouvrirAjouterCours() { openView("/Cours/AddCous.fxml"); }
    @FXML
    private void ouvrirListeModules() { openView("/Module/ModuleList.fxml"); }
    @FXML
    private void ouvrirAjouterModule() { openView("/Module/AddModule.fxml"); }
    @FXML
    private void ouvrirListeCategories() { openView("/Categorie/CategorieList.fxml"); }
    @FXML
    private void ouvrirAjouterCategorie() { openView("/Categorie/CategorieDetails.fxml"); }
    @FXML
    private void refreshFormations() { openView("/ListeFormations.fxml"); }
    @FXML
    private void ouvrirAjouterFormation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    private void openView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) nomTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }
} 