package controllers.Cours;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class UpdateCours implements Initializable {
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
    @FXML
    private Button updateButton;

    private Cour cour;
    private final CourService courService = new CourService();
    private final ModuleService moduleService = new ModuleService();
    private static final String DEFAULT_STYLE = "-fx-border-color: #34C759; -fx-border-width: 1; -fx-border-radius: 5;";
    private static final String ERROR_STYLE = "-fx-border-color: #FF4D4D; -fx-border-width: 2; -fx-border-radius: 5;";
    private static final String PDF_DIRECTORY = "C:/freelance/edu9raya/src/main/resources/pdfs/";
    private String pdfPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public void initializeCourse(Cour cour) {
        this.cour = cour;
        codeCoursTextField.setText(cour.getCodeCours());
        titreTextField.setText(cour.getTitre());
        niveauTextField.setText(cour.getNiveau());
        categorieTextField.setText(cour.getCategorie());
        descriptionTextArea.setText(cour.getDescription());
        pdfPath = cour.getPdfPath();
        if (cour.getModule() != null) {
            moduleComboBox.getSelectionModel().select(cour.getModule());
        }
        if (pdfPath != null && !pdfPath.isEmpty()) {
            File pdfFile = new File(PDF_DIRECTORY + pdfPath);
            if (pdfFile.exists()) {
                pdfLabel.setText(pdfPath);
            } else {
                pdfLabel.setText("No PDF selected");
                pdfPath = null;
            }
        } else {
            pdfLabel.setText("No PDF selected");
            pdfPath = null;
        }
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

    private boolean validateFields() {
        messageErreur.setVisible(false);
        messageErreur.setOpacity(0.0);
        String codeCours = codeCoursTextField.getText().trim();
        if (!codeCours.equals(cour.getCodeCours())) {
            if (codeCours.isEmpty()) {
                showErrorMessage("The course code is required.");
                highlightField(codeCoursTextField);
                return false;
            }
            if (codeCours.length() < 3) {
                showErrorMessage("The course code must be at least 3 characters long.");
                highlightField(codeCoursTextField);
                return false;
            }
            if (!codeCours.matches("^[a-zA-Z0-9]+$")) {
                showErrorMessage("The course code can only contain letters and numbers.");
                highlightField(codeCoursTextField);
                return false;
            }
        }
        String titre = titreTextField.getText().trim();
        if (!titre.equals(cour.getTitre())) {
            if (titre.isEmpty()) {
                showErrorMessage("The title is required.");
                highlightField(titreTextField);
                return false;
            }
            if (titre.length() < 3) {
                showErrorMessage("The title must be at least 3 characters long.");
                highlightField(titreTextField);
                return false;
            }
            if (!titre.matches("^[a-zA-Z0-9\\s]+$")) {
                showErrorMessage("The title can only contain letters, numbers, and spaces.");
                highlightField(titreTextField);
                return false;
            }
        }
        String niveau = niveauTextField.getText().trim();
        if (!niveau.equals(cour.getNiveau())) {
            if (niveau.isEmpty()) {
                showErrorMessage("The level is required.");
                highlightField(niveauTextField);
                return false;
            }
            if (!niveau.matches("^[a-zA-Z\\s]+$")) {
                showErrorMessage("The level can only contain letters and spaces.");
                highlightField(niveauTextField);
                return false;
            }
        }
        String categorie = categorieTextField.getText().trim();
        if (!categorie.equals(cour.getCategorie())) {
            if (categorie.isEmpty()) {
                showErrorMessage("The category is required.");
                highlightField(categorieTextField);
                return false;
            }
            if (!categorie.matches("^[a-zA-Z\\s]+$")) {
                showErrorMessage("The category can only contain letters and spaces.");
                highlightField(categorieTextField);
                return false;
            }
        }
        Module module = moduleComboBox.getValue();
        if (module == null || !module.equals(cour.getModule())) {
            if (module == null) {
                showErrorMessage("Please select a module.");
                highlightField(moduleComboBox);
                return false;
            }
        }
        String description = descriptionTextArea.getText().trim();
        if (!description.equals(cour.getDescription())) {
            if (description.isEmpty()) {
                showErrorMessage("The description is required.");
                highlightField(descriptionTextArea);
                return false;
            }
            if (description.length() < 10) {
                showErrorMessage("The description must be at least 10 characters long.");
                highlightField(descriptionTextArea);
                return false;
            }
        }
        if (pdfPath == null || pdfPath.isEmpty()) {
            showErrorMessage("Please choose a PDF file.");
            return false;
        }
        return true;
    }

    @FXML
    void btnUpdateCours(ActionEvent event) {
        if (!validateFields()) {
            return;
        }
        String codeCours = codeCoursTextField.getText().trim();
        if (!codeCours.equals(cour.getCodeCours())) {
            cour.setCodeCours(codeCours);
        }
        String titre = titreTextField.getText().trim();
        if (!titre.equals(cour.getTitre())) {
            cour.setTitre(titre);
        }
        String niveau = niveauTextField.getText().trim();
        if (!niveau.equals(cour.getNiveau())) {
            cour.setNiveau(niveau);
        }
        String categorie = categorieTextField.getText().trim();
        if (!categorie.equals(cour.getCategorie())) {
            cour.setCategorie(categorie);
        }
        Module module = moduleComboBox.getValue();
        if (module != null && !module.equals(cour.getModule())) {
            cour.setModule(module);
        }
        String description = descriptionTextArea.getText().trim();
        if (!description.equals(cour.getDescription())) {
            cour.setDescription(description);
        }
        if (pdfPath != null && !pdfPath.equals(cour.getPdfPath())) {
            cour.setPdfPath(pdfPath);
        }
        try {
            courService.update(cour);
            showSuccessAlert(event);
        } catch (Exception e) {
            showErrorMessage("Error while updating: " + e.getMessage());
        }
    }

    private void showSuccessAlert(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Course Updated");
        alert.setContentText("The course has been updated successfully!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #F5F5F5; -fx-font-family: 'Nunito';");
        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #34C759; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
        );
        alert.setOnHidden(evt -> showCoursList(event));
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
    void showCoursList(ActionEvent event) {
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
    void showAddCours(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/AddCours.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error while loading AddCours.fxml: " + e.getMessage());
            showErrorMessage("Navigation error: " + e.getMessage());
        }
    }
} 