package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Categorie;
import service.CategorieService;

import java.util.List;

public class CategorieViewController {

    @FXML
    private TextField idCatField;
    @FXML
    private TextField nomCatField;
    @FXML
    private Label statusLabel;

    @FXML
    private TableView<Categorie> categorieTable;
    @FXML
    private TableColumn<Categorie, Integer> idColumn;
    @FXML
    private TableColumn<Categorie, String> idCatColumn;
    @FXML
    private TableColumn<Categorie, String> nomCatColumn;

    private ObservableList<Categorie> categorieList = FXCollections.observableArrayList();

    private CategorieService categorieService = new CategorieService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCatColumn.setCellValueFactory(new PropertyValueFactory<>("idCat"));
        nomCatColumn.setCellValueFactory(new PropertyValueFactory<>("nomCat"));

        loadCategorieData();

        // Ajouter un listener pour charger la sélection dans les champs
        categorieTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idCatField.setText(newSelection.getIdCat());
                nomCatField.setText(newSelection.getNomCat());
            }
        });
    }

    /**
     * Charge les catégories depuis la base via le service et met à jour la TableView.
     */
    private void loadCategorieData() {
        List<Categorie> list = categorieService.getAllCategories();
        System.out.println("Nombre de catégories récupérées : " + list.size());
        categorieList.setAll(list);
        categorieTable.setItems(categorieList);
        categorieTable.refresh();
        statusLabel.setText("Données des catégories chargées.");
    }

    /**
     * Ajoute une nouvelle catégorie.
     */
    @FXML
    private void handleAddCategorie(ActionEvent event) {
        String idCat = idCatField.getText().trim();
        String nomCat = nomCatField.getText().trim();

        if (idCat.isEmpty() || nomCat.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        Categorie newCategorie = new Categorie();
        newCategorie.setIdCat(idCat);
        newCategorie.setNomCat(nomCat);

        boolean success = categorieService.createCategorie(newCategorie);
        if (success) {
            categorieList.add(newCategorie);
            categorieTable.refresh();
            statusLabel.setText("Catégorie ajoutée !");
        } else {
            statusLabel.setText("Erreur lors de l'ajout de la catégorie.");
        }
    }

    /**
     * Met à jour la catégorie sélectionnée avec les informations des champs.
     */
    @FXML
    private void handleUpdateCategorie(ActionEvent event) {
        Categorie selected = categorieTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Veuillez sélectionner une catégorie à mettre à jour.");
            return;
        }

        String newIdCat = idCatField.getText().trim();
        String newNomCat = nomCatField.getText().trim();

        if (newIdCat.isEmpty() || newNomCat.isEmpty()) {
            statusLabel.setText("Les champs ne doivent pas être vides pour la mise à jour.");
            return;
        }

        selected.setIdCat(newIdCat);
        selected.setNomCat(newNomCat);

        boolean success = categorieService.updateCategorie(selected);
        if (success) {
            categorieTable.refresh();
            statusLabel.setText("Catégorie mise à jour !");
        } else {
            statusLabel.setText("Erreur lors de la mise à jour de la catégorie.");
        }
    }

    /**
     * Supprime la catégorie sélectionnée.
     */
    @FXML
    private void handleDeleteCategorie(ActionEvent event) {
        Categorie selected = categorieTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Veuillez sélectionner une catégorie à supprimer.");
            return;
        }

        boolean success = categorieService.deleteCategorie(selected.getId());
        if (success) {
            categorieList.remove(selected);
            categorieTable.refresh();
            statusLabel.setText("Catégorie supprimée !");
        } else {
            statusLabel.setText("Erreur lors de la suppression de la catégorie.");
        }
    }
}
