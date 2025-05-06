package controllers;

import entities.Formation;
import entities.Reclamation;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.FormationService;
import services.ReclamationService;
import utils.MyDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListeFormationsController {

    @FXML
    private TableView<Formation> tableFormation;
    @FXML
    private TableColumn<Formation, Integer> colId;
    @FXML
    private TableColumn<Formation, String> colNom;
    @FXML
    private TableColumn<Formation, String> colDescription;
    @FXML
    private TableColumn<Formation, String> colImage;
    @FXML
    private TableColumn<Formation, String> colDate;
    @FXML
    private TableColumn<Formation, String> colCategorie;
    @FXML
    private TableColumn<Formation, Void> colActions;
    @FXML
    private VBox reclamationList;
    @FXML
    private TextField searchField;
    @FXML
    private Button notificationBtn;
    @FXML
    private Label notificationCount;
    @FXML
    private VBox notificationBox;
    @FXML
    private Button btnAjouterFormation;
    @FXML
    private Button logoutBtn;

    private FormationService formationService = new FormationService();
    private ReclamationService reclamationService = new ReclamationService();
    private ObservableList<Formation> formationList = FXCollections.observableArrayList();
    private boolean notificationBoxVisible = false;

    @FXML
    private void initialize() {
        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        colDate.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        ));

        colCategorie.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getCategorie().getNom())
        );

        // Configure the actions column
        addButtonToTable();

        // Position the notification box properly under the notification icon
        if (notificationBox != null) {
            AnchorPane.setTopAnchor(notificationBox, 80.0);
            AnchorPane.setRightAnchor(notificationBox, 25.0);
            notificationBox.setVisible(false);
            notificationBox.setManaged(false);

            // Make sure the notification box is on top layer
            notificationBox.setViewOrder(-1);

            // Position relative to notification button at runtime
            notificationBtn.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                double buttonX = notificationBtn.localToScene(0, 0).getX();
                double notificationBoxWidth = notificationBox.getPrefWidth();
                AnchorPane.setRightAnchor(notificationBox, 25.0);
            });
        }

        // Configurer le bouton de déconnexion
        logoutBtn.setOnAction(event -> handleLogout());

        // Configurer le bouton de notification
        notificationBtn.setOnAction(event -> toggleNotificationBox());

        // Charger les formations et le compte des notifications
        loadFormations();
        updateNotificationCount();
    }


    private void handleLogout() {
        // Déconnexion de l'utilisateur
        SessionController.getInstance().logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page de connexion.");
        }
    }

    private void loadFormations() {
        try {
            List<Formation> formations = formationService.recuperer();
            formationList.setAll(formations);
            tableFormation.setItems(formationList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des formations.");
        }
    }
    private void openReclamationDetails(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationDetailsAdmin.fxml"));
            Parent root = loader.load();

            // Set the reclamation to the controller if needed
            // ReclamationDetailsAdminController controller = loader.getController();
            // controller.setReclamation(reclamation);

            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir les détails de la réclamation.");
        }
    }
    private void updateNotificationCount() {
        try {
            List<Reclamation> reclamations = reclamationService.recuperer();
            long count = reclamations.stream()
                    .filter(r -> r.getStatut().equals("en attente"))
                    .count();

            // Update the notification count label
            notificationCount.setText(String.valueOf(count));

            // Update the badge visibility
            if (count > 0) {
                notificationCount.setVisible(true);
                notificationCount.getStyleClass().add("notification-badge");
            } else {
                notificationCount.setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du comptage des réclamations.");
        }
    }
    private void toggleNotificationBox() {
        if (notificationBox.isVisible()) {
            notificationBox.setVisible(false);
            notificationBox.setManaged(false);
            notificationBoxVisible = false;
        } else {
            loadNewReclamations();
            notificationBox.setVisible(true);
            notificationBox.setManaged(true);
            notificationBoxVisible = true;

            // Make sure it appears on top
            notificationBox.toFront();
        }
    }
    private void loadNewReclamations() {
        try {
            reclamationList.getChildren().clear();

            // Add a header to the notification box
            Label headerLabel = new Label("Notifications");
            headerLabel.getStyleClass().add("notification-header");
            reclamationList.getChildren().add(headerLabel);

            List<Reclamation> reclamations = reclamationService.recuperer();
            List<Reclamation> newReclamations = reclamations.stream()
                    .filter(r -> r.getStatut().equals("en attente"))
                    .toList();

            if (newReclamations.isEmpty()) {
                Label noReclamations = new Label("Aucune nouvelle réclamation.");
                noReclamations.getStyleClass().add("no-notifications");
                reclamationList.getChildren().add(noReclamations);
            } else {
                for (Reclamation reclamation : newReclamations) {
                    // Create container for each notification
                    VBox notificationItem = new VBox(5);
                    notificationItem.getStyleClass().add("notification-item");

                    // Create subject label
                    Label subjectLabel = new Label(reclamation.getSujet());
                    subjectLabel.getStyleClass().add("notification-text");

                    // Create date label
                    Label dateLabel = new Label("Envoyé le: " +
                            reclamation.getDateEnvoi().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    dateLabel.getStyleClass().add("notification-date");

                    // Add click handler to open reclamation details
                    notificationItem.setOnMouseClicked(event -> openReclamationDetails(reclamation));

                    // Add to notification item
                    notificationItem.getChildren().addAll(subjectLabel, dateLabel);
                    reclamationList.getChildren().add(notificationItem);
                }
            }

            // Add "View All" link at the bottom
            Label viewAllLabel = new Label("Voir toutes les réclamations");
            viewAllLabel.getStyleClass().add("notification-view-all");
            viewAllLabel.setOnMouseClicked(event -> {
                ouvrirListeReclamations();
                toggleNotificationBox(); // Close notification box
            });
            reclamationList.getChildren().add(viewAllLabel);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des réclamations.");
        }
    }
    @FXML
    private void ouvrirListeReclamations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeReclamationsAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            updateNotificationCount(); // Refresh notification count after viewing reclamations
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la gestion des réclamations");
        }
    }
    @FXML
    private void refreshFormations() {
        loadFormations();
        updateNotificationCount();
    }

    @FXML
    private void searchFormation(ActionEvent event) {
        // Récupérer le texte saisi en minuscule et enlever les espaces
        String keyword = searchField.getText().toLowerCase().trim();

        // Si aucun mot-clé n'est entré, afficher la liste complète
        if (keyword.isEmpty()) {
            tableFormation.setItems(formationList);
            return;
        }

        // Créer une nouvelle liste filtrée
        ObservableList<Formation> filteredList = FXCollections.observableArrayList();

        for (Formation formation : formationList) {
            // On teste si le nom, la description ou la catégorie contient le mot-clé (en minuscule)
            if (formation.getNom().toLowerCase().contains(keyword)
                    || formation.getDescription().toLowerCase().contains(keyword)
                    || formation.getCategorie().getNom().toLowerCase().contains(keyword)) {
                filteredList.add(formation);
            }
        }
        tableFormation.setItems(filteredList);
    }

    @FXML
    private void ouvrirListeCours() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/CoursList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des cours");
        }
    }

    @FXML
    private void ouvrirGestionStage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminConsulterStage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout de cours");
        }
    }

    @FXML
    private void ouvrirAjouterCours() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/AddCous.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout de cours");
        }
    }

    @FXML
    private void ouvrirListeModules() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Module/ModuleList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des modules");
        }
    }

    @FXML
    private void ouvrirAjouterModule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Module/AddModule.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout de module");
        }
    }

    @FXML
    private void ouvrirListeCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
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
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout de catégorie");
        }
    }

    @FXML
    private void ouvrirAjouterFormation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout.");
        }
    }

    private void addButtonToTable() {
        colActions.setCellFactory(param -> new TableCell<Formation, Void>() {
            private final Button btnVoir = new Button("Voir");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox pane = new HBox(5, btnVoir, btnModifier, btnSupprimer);

            {
                // Fixer la largeur de chaque bouton
                btnVoir.setPrefWidth(60);
                btnModifier.setPrefWidth(60);
                btnSupprimer.setPrefWidth(60);

                btnVoir.setOnAction((ActionEvent event) -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    openFormationDetails(formation);
                });

                btnModifier.setOnAction((ActionEvent event) -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    openModifierFormation(formation);
                });

                btnSupprimer.setOnAction((ActionEvent event) -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    deleteFormation(formation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void openFormationDetails(Formation formation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoirFormation.fxml"));
            Parent root = loader.load();
            VoirFormationController controller = loader.getController();
            controller.setFormation(formation);

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setTitle("Détails Formation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir les détails.");
        }
    }

    private void openModifierFormation(Formation formation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierFormation.fxml"));
            Parent root = loader.load();
            ModifierFormationController controller = loader.getController();
            controller.setFormation(formation);

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setTitle("Modifier Formation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de modification.");
        }
    }

    private void deleteFormation(Formation formation) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirmez-vous la suppression ? Cela supprimera également tous les niveaux et contenus associés à cette formation.", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmation");
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                // Supprimer d'abord les contenus associés aux niveaux de cette formation
                supprimerContenusAssocies(formation.getId());

                // Ensuite supprimer les niveaux associés à cette formation
                supprimerNiveauxAssocies(formation.getId());

                // Finalement supprimer la formation
                formationService.supprimer(formation);

                // Mettre à jour l'interface
                formationList.remove(formation);
                tableFormation.refresh();

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation supprimée avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la formation: " + e.getMessage());
            }
        }
    }

    // Méthode pour supprimer les contenus associés aux niveaux
    private void supprimerContenusAssocies(int formationId) throws SQLException {
        Connection cnx = MyDatabase.getInstance().getCnx();
        // Cette requête supprime tous les contenus associés aux niveaux de cette formation
        String sql = "DELETE FROM contenu WHERE niveau_id IN (SELECT id FROM niveau WHERE formation_id = ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, formationId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Contenus associés aux niveaux de la formation " + formationId + " supprimés: " + rowsAffected + " lignes");
        }
    }

    // Méthode pour supprimer les niveaux associés
    private void supprimerNiveauxAssocies(int formationId) throws SQLException {
        Connection cnx = MyDatabase.getInstance().getCnx();
        String sql = "DELETE FROM niveau WHERE formation_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, formationId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Niveaux associés à la formation " + formationId + " supprimés: " + rowsAffected + " lignes");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}