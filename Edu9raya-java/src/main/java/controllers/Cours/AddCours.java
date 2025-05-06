package controllers.Cours;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import entities.Cour;
import entities.Module;
import services.CourService;
import services.ModuleService;
import services.MailService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

public class AddCours {
    @FXML
    private TextField codeCoursTextField;
    @FXML
    private TextField titreTextField;
    @FXML
    private TextField niveauTextField;
    @FXML
    private TextField categorieTextField;
    @FXML
    private ComboBox<Module> moduleComboBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label pdfLabel;
    @FXML
    private Button choisirPdfButton;
    @FXML
    private Label messageErreur;

    private final CourService courService = new CourService();
    private final ModuleService moduleService = new ModuleService();
    private static final String DEFAULT_STYLE = "-fx-border-color: #34C759; -fx-border-width: 1; -fx-border-radius: 5;";
    private static final String ERROR_STYLE = "-fx-border-color: #FF4D4D; -fx-border-width: 2; -fx-border-radius: 5;";
    private static final String PDF_DIRECTORY = "C:/freelance/edu9raya/src/main/resources/pdfs/";
    private String pdfPath;

    @FXML
    private void initialize() {
        messageErreur.setVisible(false);
        messageErreur.setOpacity(0.0);
        codeCoursTextField.setStyle(DEFAULT_STYLE);
        titreTextField.setStyle(DEFAULT_STYLE);
        niveauTextField.setStyle(DEFAULT_STYLE);
        categorieTextField.setStyle(DEFAULT_STYLE);
        moduleComboBox.setStyle(DEFAULT_STYLE);
        descriptionTextArea.setStyle(DEFAULT_STYLE);
        addFocusListener(codeCoursTextField);
        addFocusListener(titreTextField);
        addFocusListener(niveauTextField);
        addFocusListener(categorieTextField);
        addFocusListener(moduleComboBox);
        addFocusListener(descriptionTextArea);
        List<Module> modules = moduleService.retrieveAll();
        moduleComboBox.getItems().addAll(modules);
        moduleComboBox.setCellFactory(lv -> new ListCell<Module>() {
            @Override
            protected void updateItem(Module module, boolean empty) {
                super.updateItem(module, empty);
                setText(empty || module == null ? "" : module.getNom());
            }
        });
        moduleComboBox.setButtonCell(new ListCell<Module>() {
            @Override
            protected void updateItem(Module module, boolean empty) {
                super.updateItem(module, empty);
                setText(empty || module == null ? "" : module.getNom());
            }
        });
        File pdfDir = new File(PDF_DIRECTORY);
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
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
    private void choisirPdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String originalFilename = selectedFile.getName();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = "course-" + UUID.randomUUID().toString() + extension;
                Path targetPath = Paths.get(PDF_DIRECTORY, newFilename);
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                pdfPath = newFilename;
                pdfLabel.setText(originalFilename);
            } catch (IOException e) {
                showErrorMessage("Error copying the PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnAjouterCours(ActionEvent event) {
        messageErreur.setVisible(false);
        messageErreur.setOpacity(0.0);
        resetFieldStyles();
        String codeCours = codeCoursTextField.getText().trim();
        String titre = titreTextField.getText().trim();
        String niveau = niveauTextField.getText().trim();
        String categorie = categorieTextField.getText().trim();
        Module module = moduleComboBox.getValue();
        String description = descriptionTextArea.getText().trim();
        if (codeCours.isEmpty()) {
            showErrorMessage("The course code is required.");
            highlightField(codeCoursTextField);
            return;
        }
        if (codeCours.length() < 3) {
            showErrorMessage("The course code must be at least 3 characters long.");
            highlightField(codeCoursTextField);
            return;
        }
        if (!codeCours.matches("^[a-zA-Z0-9]+$")) {
            showErrorMessage("The course code can only contain letters and numbers.");
            highlightField(codeCoursTextField);
            return;
        }
        if (titre.isEmpty()) {
            showErrorMessage("The title is required.");
            highlightField(titreTextField);
            return;
        }
        if (titre.length() < 3) {
            showErrorMessage("The title must be at least 3 characters long.");
            highlightField(titreTextField);
            return;
        }
        if (!titre.matches("^[a-zA-Z0-9\\s]+$")) {
            showErrorMessage("The title can only contain letters, numbers, and spaces.");
            highlightField(titreTextField);
            return;
        }
        if (niveau.isEmpty()) {
            showErrorMessage("The level is required.");
            highlightField(niveauTextField);
            return;
        }
        if (!niveau.matches("^[a-zA-Z\\s]+$")) {
            showErrorMessage("The level can only contain letters and spaces.");
            highlightField(niveauTextField);
            return;
        }
        if (categorie.isEmpty()) {
            showErrorMessage("The category is required.");
            highlightField(categorieTextField);
            return;
        }
        if (!categorie.matches("^[a-zA-Z\\s]+$")) {
            showErrorMessage("The category can only contain letters and spaces.");
            highlightField(categorieTextField);
            return;
        }
        if (module == null) {
            showErrorMessage("Please select a module.");
            highlightField(moduleComboBox);
            return;
        }
        if (description.isEmpty()) {
            showErrorMessage("The description is required.");
            highlightField(descriptionTextArea);
            return;
        }
        if (description.length() < 10) {
            showErrorMessage("The description must be at least 10 characters long.");
            highlightField(descriptionTextArea);
            return;
        }
        if (pdfPath == null || pdfPath.isEmpty()) {
            showErrorMessage("Please choose a PDF file.");
            return;
        }
        try {
            Cour cour = new Cour(codeCours, titre, niveau, categorie, module, description, pdfPath);
            courService.add(cour);
            MailService.sendMail(
                    "magukochebaane@gmail.com",
                    "New Course Added: " + cour.getTitre(),
                    "A new course has been added:\n\n" +
                            "Title: " + cour.getTitre() + "\n" +
                            "Category: " + cour.getCategorie() + "\n" +
                            "Level: " + cour.getNiveau() + "\n\n" +
                            "Best regards,\nEdu9raya System"
            );
            clearForm();
            showSuccessAlert(event);
        } catch (Exception e) {
            showErrorMessage("Error while adding: " + e.getMessage());
        }
    }

    private void showSuccessAlert(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Course Added");
        alert.setContentText("The course has been added successfully!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #F5F5F5; -fx-font-family: 'Nunito';");
        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #34C759; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
        );
        alert.setOnHidden(evt -> showCoursList(event));
        alert.showAndWait();
    }

    private void highlightField(javafx.scene.Node field) {
        field.setStyle(ERROR_STYLE);
    }

    private void resetFieldStyles() {
        codeCoursTextField.setStyle(DEFAULT_STYLE);
        titreTextField.setStyle(DEFAULT_STYLE);
        niveauTextField.setStyle(DEFAULT_STYLE);
        categorieTextField.setStyle(DEFAULT_STYLE);
        moduleComboBox.setStyle(DEFAULT_STYLE);
        descriptionTextArea.setStyle(DEFAULT_STYLE);
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
        codeCoursTextField.clear();
        titreTextField.clear();
        niveauTextField.clear();
        categorieTextField.clear();
        moduleComboBox.getSelectionModel().clearSelection();
        descriptionTextArea.clear();
        pdfLabel.setText("No PDF selected");
        pdfPath = null;
        resetFieldStyles();
    }

    @FXML
    private void showCoursList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/CoursList.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error while loading CoursList.fxml: " + e.getMessage());
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    private void showAddCours(ActionEvent event) {
        clearForm();
        messageErreur.setVisible(false);
        messageErreur.setOpacity(0.0);
    }

    @FXML
    private void refreshFormations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeFormations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) codeCoursTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirListeCours() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/CoursList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) codeCoursTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirAjouterCours() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/AddCous.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) codeCoursTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirListeModules() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Module/ModuleList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) codeCoursTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirAjouterModule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Module/AddModule.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) codeCoursTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirListeCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie/CategorieList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) codeCoursTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirAjouterCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie/CategorieDetails.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) codeCoursTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirAjouterFormation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) codeCoursTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }
} 