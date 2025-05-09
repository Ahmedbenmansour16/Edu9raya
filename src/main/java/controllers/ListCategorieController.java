package controllers;


import javafx.event.ActionEvent;
import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.CategorieService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListCategorieController implements Initializable {

    @FXML
    private TableView<Categorie> tableCategories;
    @FXML
    private TableColumn<Categorie, Integer> colId;
    @FXML
    private TableColumn<Categorie, String> colNom;
    @FXML
    private TableColumn<Categorie, Void> colAction;
    @FXML
    private TextField searchField;

    private CategorieService categorieService = new CategorieService();
    private ObservableList<Categorie> listeCategories = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        addButtonToTable();
        loadCategories();
    }

    private void loadCategories() {
        try {
            listeCategories.setAll(categorieService.recuperer());
            tableCategories.setItems(listeCategories);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des catégories");
        }
    }

    @FXML
    private void afficherCategories() {
        loadCategories();
        showAlert(Alert.AlertType.INFORMATION, "Information", "Liste des catégories actualisée.");
    }

    private void addButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<Categorie, Void>() {
            private final Button btnVoir = new Button("Voir");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox pane = new HBox(5, btnVoir, btnModifier, btnSupprimer);

            {
                // Voir les détails
                btnVoir.setOnAction((ActionEvent event) -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    ouvrirDetailCategorie(categorie);
                });

                // Modifier la catégorie
                btnModifier.setOnAction((ActionEvent event) -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    ouvrirModifierCategorie(categorie);
                });

                // Supprimer la catégorie avec confirmation
                btnSupprimer.setOnAction((ActionEvent event) -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous supprimer cette catégorie ?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> result = confirmation.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        try {
                            categorieService.supprimer(categorie);
                            loadCategories();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression");
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    private void ouvrirDetailCategorie(Categorie categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailCategorie.fxml"));
            Parent root = loader.load();
            DetailCategorieController detailController = loader.getController();
            detailController.setCategorie(categorie);

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) tableCategories.getScene().getWindow();
            stage.setTitle("Détail de la Catégorie");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le détail");
        }
    }

    private void ouvrirModifierCategorie(Categorie categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCategorie.fxml"));
            Parent root = loader.load();
            ModifierCategorieController modController = loader.getController();
            modController.setCategorie(categorie);

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) tableCategories.getScene().getWindow();
            stage.setTitle("Modifier la Catégorie");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface de modification");
        }
    }
    @FXML
    private void ouvrirListeFormations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeFormations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableCategories.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des formations");
        }
    }
    @FXML
    private void searchCategorie(ActionEvent event) {
        // Récupérer le mot-clé en minuscule et enlever les espaces inutiles
        String keyword = searchField.getText().toLowerCase().trim();

        if (keyword.isEmpty()) {
            // Si le champ est vide, rétablir la liste complète
            tableCategories.setItems(listeCategories);
            return;
        }

        // Créer une liste filtrée
        ObservableList<Categorie> filteredList = FXCollections.observableArrayList();
        for (Categorie categorie : listeCategories) {
            if (categorie.getNom().toLowerCase().contains(keyword)) {
                filteredList.add(categorie);
            }
        }
        tableCategories.setItems(filteredList);
    }

    @FXML
    private void ouvrirAjouterFormation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableCategories.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout de formation");
        }
    }
    @FXML
    private void ouvrirAjouterCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategorie.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) tableCategories.getScene().getWindow();
            stage.setTitle("Ajouter une Catégorie");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout");
        }
    }
    // Méthode utilitaire pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
