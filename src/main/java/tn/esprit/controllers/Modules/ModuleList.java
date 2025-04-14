package tn.esprit.controllers.Modules;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
<<<<<<< HEAD
import javafx.scene.shape.Rectangle;
import tn.esprit.controllers.Cours.ViewCoursByModule;
=======
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
import tn.esprit.models.Module;
import tn.esprit.services.ModuleService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleList {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vboxContainer;

    @FXML
    private TextField searchField;

    private final ModuleService moduleService = new ModuleService();
    private List<Module> allModules;
    private static final String IMAGE_DIRECTORY = "C:/freelance/edu9raya/src/main/resources/images/";

    @FXML
    private void initialize() {
        loadModules();
        searchField.textProperty().addListener((obs, oldValue, newValue) -> filterModules(newValue));
    }

    private void loadModules() {
        vboxContainer.getChildren().clear();
        allModules = moduleService.retrieveAll();
        if (allModules.isEmpty()) {
            Label noModulesLabel = new Label("No modules found.");
            noModulesLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
            VBox.setMargin(noModulesLabel, new Insets(20, 0, 0, 0));
            vboxContainer.getChildren().add(noModulesLabel);
            return;
        }
        displayModules(allModules);
    }

    private void filterModules(String searchText) {
        vboxContainer.getChildren().clear();
        List<Module> filteredModules = allModules.stream()
                .filter(module -> module.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        module.getEnseignant().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        if (filteredModules.isEmpty()) {
            Label noModulesLabel = new Label("No modules match your search.");
            noModulesLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
            VBox.setMargin(noModulesLabel, new Insets(20, 0, 0, 0));
            vboxContainer.getChildren().add(noModulesLabel);
            return;
        }
        displayModules(filteredModules);
    }

    private void displayModules(List<Module> modules) {
        HBox currentRow = null;
        for (int i = 0; i < modules.size(); i++) {
            if (i % 2 == 0) { // 2 cards per row
                currentRow = new HBox();
                currentRow.setSpacing(120);
                currentRow.setPrefWidth(1180);
                VBox.setMargin(currentRow, new Insets(0, 0, 30, 50));
                vboxContainer.getChildren().add(currentRow);
            }
            Pane card = createModuleCard(modules.get(i));
            currentRow.getChildren().add(card);
        }
    }

    private Pane createModuleCard(Module module) {
        Pane card = new Pane();
<<<<<<< HEAD
        card.setPrefSize(420, 400);
=======
        card.setPrefSize(420, 400); // Increased height to accommodate image
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        card.setStyle("-fx-border-color: #34C759; -fx-border-radius: 5; -fx-background-color: #FFFFFF; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        // Image
        ImageView imageView = new ImageView();
<<<<<<< HEAD
        imageView.setFitWidth(380);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(20); // Center: (420 - 380) / 2 = 20
        imageView.setLayoutY(20);


        // Clip to prevent overflow
        Rectangle clip = new Rectangle(380, 200);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        imageView.setClip(clip);

=======
        imageView.setFitHeight(200);
        imageView.setFitWidth(390);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(50);
        imageView.setLayoutY(20);

>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        // Load module image
        if (module.getImage() != null && !module.getImage().isEmpty()) {
            File imageFile = new File(IMAGE_DIRECTORY + module.getImage());
            if (imageFile.exists()) {
                try {
<<<<<<< HEAD
                    Image image = new Image(imageFile.toURI().toString(), 380, 200, true, true);
                    imageView.setImage(image);
                } catch (Exception e) {
                    imageView.setImage(new Image(getClass().getResource("/images/downloadImage.png").toExternalForm(), 380, 200, true, true));
                }
            } else {
                imageView.setImage(new Image(getClass().getResource("/images/downloadImage.png").toExternalForm(), 380, 200, true, true));
            }
        } else {
            imageView.setImage(new Image(getClass().getResource("/images/downloadImage.png").toExternalForm(), 380, 200, true, true));
=======
                    Image image = new Image(imageFile.toURI().toString());
                    imageView.setImage(image);
                } catch (Exception e) {
                    // Fallback to default image
                    imageView.setImage(new Image(getClass().getResource("/images/downloadImage.png").toExternalForm()));
                }
            } else {
                imageView.setImage(new Image(getClass().getResource("/images/downloadImage.png").toExternalForm()));
            }
        } else {
            imageView.setImage(new Image(getClass().getResource("/images/downloadImage.png").toExternalForm()));
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        }

        // Name
        Label nameLabel = new Label(module.getNom());
        nameLabel.setLayoutX(20);
        nameLabel.setLayoutY(230);
<<<<<<< HEAD
        nameLabel.setPrefWidth(380);
=======
        nameLabel.setPrefWidth(510);
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        nameLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #181D38;");
        nameLabel.setWrapText(true);

        // Teacher
        Label teacherLabel = new Label("Teacher: " + module.getEnseignant());
        teacherLabel.setLayoutX(20);
        teacherLabel.setLayoutY(260);
<<<<<<< HEAD
        teacherLabel.setPrefWidth(380);
=======
        teacherLabel.setPrefWidth(510);
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        teacherLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        teacherLabel.setWrapText(true);

        // Duration
        Label durationLabel = new Label("Duration: " + module.getDuree() + " hours");
        durationLabel.setLayoutX(20);
        durationLabel.setLayoutY(290);
<<<<<<< HEAD
        durationLabel.setPrefWidth(380);
=======
        durationLabel.setPrefWidth(510);
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        durationLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        durationLabel.setWrapText(true);

        // Coefficient
        Label coefficientLabel = new Label("Coefficient: " + module.getCoefficient());
        coefficientLabel.setLayoutX(20);
        coefficientLabel.setLayoutY(320);
<<<<<<< HEAD
        coefficientLabel.setPrefWidth(380);
=======
        coefficientLabel.setPrefWidth(510);
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        coefficientLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        coefficientLabel.setWrapText(true);

        // Update Button
        Button updateButton = new Button("Update");
        updateButton.setLayoutX(20);
        updateButton.setLayoutY(350);
        updateButton.setPrefWidth(120);
        updateButton.setPrefHeight(30);
        updateButton.setStyle("-fx-background-color: #34C759; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;");
        updateButton.setOnMouseEntered(e -> updateButton.setStyle("-fx-background-color: #2EBF4F; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;"));
        updateButton.setOnMouseExited(e -> updateButton.setStyle("-fx-background-color: #34C759; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;"));
        updateButton.setOnAction(e -> handleUpdateModule(module));

        // Delete Button
        Button deleteButton = new Button("Delete");
        deleteButton.setLayoutX(150);
        deleteButton.setLayoutY(350);
        deleteButton.setPrefWidth(120);
        deleteButton.setPrefHeight(30);
        deleteButton.setStyle("-fx-background-color: #181D38; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #12152B; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #181D38; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;"));
        deleteButton.setOnAction(e -> handleDeleteModule(module));

        // View Cours Button
        Button viewCoursButton = new Button("View Cours");
        viewCoursButton.setLayoutX(280);
        viewCoursButton.setLayoutY(350);
        viewCoursButton.setPrefWidth(120);
        viewCoursButton.setPrefHeight(30);
        viewCoursButton.setStyle("-fx-background-color: #34C759; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;");
        viewCoursButton.setOnMouseEntered(e -> viewCoursButton.setStyle("-fx-background-color: #2EBF4F; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;"));
        viewCoursButton.setOnMouseExited(e -> viewCoursButton.setStyle("-fx-background-color: #34C759; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;"));
        viewCoursButton.setOnAction(e -> handleViewCours(module));

        card.getChildren().addAll(imageView, nameLabel, teacherLabel, durationLabel, coefficientLabel, updateButton, deleteButton, viewCoursButton);
        return card;
    }

    private void handleUpdateModule(Module module) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modules/UpdateModule.fxml"));
            Parent root = loader.load();
            UpdateModule controller = loader.getController();
<<<<<<< HEAD
            if (controller == null) {
                throw new IOException("Controller for UpdateModule.fxml is null");
            }
=======
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
            controller.initializeModule(module);
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error while loading UpdateModule.fxml: " + e.getMessage());
<<<<<<< HEAD
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Navigation Failed");
            errorAlert.setContentText("Unable to load the update page: " + e.getMessage());
            errorAlert.showAndWait();
=======
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        }
    }

    private void handleDeleteModule(Module module) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Delete Module");
        confirmationAlert.setContentText("Are you sure you want to delete the module '" + module.getNom() + "'?");

<<<<<<< HEAD
=======
        // Apply custom styles to match template CSS
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        DialogPane dialogPane = confirmationAlert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #F5F5F5; -fx-font-family: 'Nunito';");
        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #34C759; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
        );
        dialogPane.lookupButton(ButtonType.CANCEL).setStyle(
                "-fx-background-color: #181D38; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
        );

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    moduleService.delete(module.getId());
<<<<<<< HEAD
                    loadModules();
=======
                    loadModules(); // Refresh the module list
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Deletion Failed");
                    errorAlert.setContentText("Error while deleting the module: " + e.getMessage());

<<<<<<< HEAD
=======
                    // Style error alert
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
                    DialogPane errorDialogPane = errorAlert.getDialogPane();
                    errorDialogPane.setStyle("-fx-background-color: #F5F5F5; -fx-font-family: 'Nunito';");
                    errorDialogPane.lookupButton(ButtonType.OK).setStyle(
                            "-fx-background-color: #34C759; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
                    );

                    errorAlert.showAndWait();
                }
            }
        });
    }

    private void handleViewCours(Module module) {
<<<<<<< HEAD
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/ViewCoursByModule.fxml"));
            Parent root = loader.load();
            ViewCoursByModule controller = loader.getController();
            controller.initializeWithModule(module);
=======
        // Placeholder for view cours functionality
        /*
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/CoursList.fxml"));
            Parent root = loader.load();
            CoursList controller = loader.getController();
            controller.setModuleId(module.getId());
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
<<<<<<< HEAD
            System.err.println("Error while loading ViewCoursByModule.fxml: " + e.getMessage());
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Navigation Failed");
            errorAlert.setContentText("Unable to load the courses page: " + e.getMessage());
            errorAlert.showAndWait();
        }
=======
            System.err.println("Error while loading CoursList.fxml: " + e.getMessage());
        }
        */
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
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
<<<<<<< HEAD
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Navigation Failed");
            errorAlert.setContentText("Unable to load the module list page: " + e.getMessage());
            errorAlert.showAndWait();
=======
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
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
<<<<<<< HEAD
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Navigation Failed");
            errorAlert.setContentText("Unable to load the add module page: " + e.getMessage());
            errorAlert.showAndWait();
=======
>>>>>>> 3995ed6bc298f7141ffba057c47f0d040947cff1
        }
    }
}