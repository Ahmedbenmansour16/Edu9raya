package controllers;

import entities.Formation;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.FormationService;
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
    private TextField searchField;
    @FXML
    private Button btnAjouterFormation;
    @FXML
    private Button logoutBtn;

    private FormationService formationService = new FormationService();
    private ObservableList<Formation> formationList = FXCollections.observableArrayList();

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
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnView = new Button("Voir");
            private final Button btnEdit = new Button("Modifier");
            private final Button btnDelete = new Button("Supprimer");
            private final HBox pane = new HBox(5);

            {
                btnView.getStyleClass().add("btn-view");
                btnEdit.getStyleClass().add("btn-edit");
                btnDelete.getStyleClass().add("btn-delete");

                btnView.setOnAction(event -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    openFormationDetails(formation);
                });

                btnEdit.setOnAction(event -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    openModifierFormation(formation);
                });

                btnDelete.setOnAction(event -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    deleteFormation(formation);
                });

                pane.getChildren().addAll(btnView, btnEdit, btnDelete);
                pane.setAlignment(Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        // Ajout de la colonne Action
        addButtonToTable();

        // Configurer le bouton de déconnexion
        logoutBtn.setOnAction(event -> handleLogout());

        // Charger les formations
        loadFormations();
    }

    private void handleLogout() {
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

    @FXML
    private void refreshFormations() {
        loadFormations();
    }

    @FXML
    private void searchFormation(ActionEvent event) {
        String keyword = searchField.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            tableFormation.setItems(formationList);
            return;
        }
        ObservableList<Formation> filteredList = FXCollections.observableArrayList();
        for (Formation formation : formationList) {
            if (formation.getNom().toLowerCase().contains(keyword)
                    || formation.getDescription().toLowerCase().contains(keyword)
                    || formation.getCategorie().getNom().toLowerCase().contains(keyword)) {
                filteredList.add(formation);
            }
        }
        tableFormation.setItems(filteredList);
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
    private void ouvrirListeReclamations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeReclamationsAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la gestion des réclamations");
        }
    }

    @FXML
    private void ajouterFormation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableFormation.getScene().getWindow();
            stage.setTitle("Ajouter Formation");
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
                supprimerContenusAssocies(formation.getId());
                supprimerNiveauxAssocies(formation.getId());
                formationService.supprimer(formation);
                formationList.remove(formation);
                tableFormation.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation supprimée avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la formation: " + e.getMessage());
            }
        }
    }

    private void supprimerContenusAssocies(int formationId) throws SQLException {
        Connection cnx = MyDatabase.getInstance().getCnx();
        String sql = "DELETE FROM contenu WHERE niveau_id IN (SELECT id FROM niveau WHERE formation_id = ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, formationId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Contenus associés aux niveaux de la formation " + formationId + " supprimés: " + rowsAffected + " lignes");
        }
    }

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