package controllers;

import entities.Reclamation;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ReclamationService;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class ListeReclamationsAdminController {

    @FXML
    private TableView<Reclamation> tableReclamation;
    @FXML
    private TableColumn<Reclamation, String> colEtudiant;
    @FXML
    private TableColumn<Reclamation, String> colSujet;
    @FXML
    private TableColumn<Reclamation, String> colDate;
    @FXML
    private TableColumn<Reclamation, String> colStatut;
    @FXML
    private TableColumn<Reclamation, Void> colActions;
    @FXML
    private Button logoutBtn;

    private ReclamationService reclamationService = new ReclamationService();
    private ObservableList<Reclamation> reclamationList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configuration des colonnes
        colEtudiant.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("montakaabi@gmail.com")); // Replace with actual student email if linked to User entity
        colSujet.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        colDate.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getDateEnvoi().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        ));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Configure the actions column
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnRepondre = new Button("Répondre");
            private final Button btnVoirModifier = new Button("Voir/Modifier");
            private final HBox pane = new HBox(5);

            {
                btnRepondre.getStyleClass().add("btn-reply");
                btnVoirModifier.getStyleClass().add("btn-view");

                btnRepondre.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    if (reclamation.getStatut().equals("Traité")) {
                        showAlert(Alert.AlertType.WARNING, "Avertissement", "Cette réclamation a déjà été traitée.");
                    } else {
                        openRepondreDialog(reclamation);
                    }
                });

                btnVoirModifier.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    openDetailReclamation(reclamation);
                });

                pane.getChildren().addAll(btnRepondre, btnVoirModifier);
                pane.setAlignment(Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        // Configurer le bouton de déconnexion
        logoutBtn.setOnAction(event -> handleLogout());

        // Charger les réclamations
        loadReclamations();
    }

    private void handleLogout() {
        SessionController.getInstance().logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) tableReclamation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page de connexion.");
        }
    }

    private void loadReclamations() {
        try {
            reclamationList.setAll(reclamationService.recuperer());
            tableReclamation.setItems(reclamationList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des réclamations.");
        }
    }

    private void openRepondreDialog(Reclamation reclamation) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Répondre à la réclamation");
        dialog.setHeaderText("Réclamation: " + reclamation.getSujet());

        // Set the button types
        ButtonType repondreButtonType = new ButtonType("Envoyer la réponse", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(repondreButtonType, ButtonType.CANCEL);

        // Create the dialog content
        VBox dialogContent = new VBox(10);
        Label messageLabel = new Label("Message de l'étudiant:");
        TextArea messageArea = new TextArea(reclamation.getJustification());
        messageArea.setEditable(false);
        messageArea.setPrefHeight(100);

        Label responseLabel = new Label("Votre réponse:");
        TextArea responseArea = new TextArea();
        responseArea.setPromptText("Saisissez votre réponse ici...");
        responseArea.setPrefHeight(100);

        dialogContent.getChildren().addAll(messageLabel, messageArea, responseLabel, responseArea);
        dialog.getDialogPane().setContent(dialogContent);

        // Convert the result to a string when the "Envoyer la réponse" button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == repondreButtonType) {
                return responseArea.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(response -> {
            if (!response.isEmpty()) {
                try {
                    reclamationService.respondToReclamation(reclamation.getId(), response);
                    reclamation.setAdminResponse(response);
                    reclamation.setStatut("Traité");
                    tableReclamation.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Réponse envoyée avec succès.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'envoi de la réponse: " + e.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "La réponse ne peut pas être vide.");
            }
        });
    }

    private void openDetailReclamation(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailReclamationAdmin.fxml"));
            Parent root = loader.load();
            DetailReclamationAdminController controller = loader.getController();
            controller.setReclamation(reclamation);
            Stage stage = (Stage) tableReclamation.getScene().getWindow();
            stage.setTitle("Détail de la réclamation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir les détails de la réclamation.");
        }
    }

    @FXML
    private void ouvrirListeFormations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeFormations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableReclamation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des formations");
        }
    }

    @FXML
    private void ajouterFormation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableReclamation.getScene().getWindow();
            stage.setTitle("Ajouter Formation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout.");
        }
    }

    @FXML
    private void ouvrirListeCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableReclamation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des catégories");
        }
    }

    @FXML
    private void ouvrirAjouterCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableReclamation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout de catégorie");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}