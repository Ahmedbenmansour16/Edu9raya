package controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Book;
import model.Categorie;
import service.BookService;
import service.CategorieService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BookViewController {

    @FXML
    private TextField idBookField;
    @FXML
    private TextField nomBookField;
    @FXML
    private TextArea descriptionField;

    // ComboBox pour sélectionner la catégorie (rempli via CategorieService)
    @FXML
    private ComboBox<Categorie> catBookCombo;

    // ComboBox pour la disponibilité ("oui"/"non")
    @FXML
    private ComboBox<String> dispoBookCombo;

    // Labels pour afficher le chemin du fichier PDF et de l'image
    @FXML
    private Label pdfFileLabel;
    @FXML
    private Label pictureLabel;

    @FXML
    private Label statusLabel;

    // TableView et colonnes d'affichage des books
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, Integer> idColumn;
    @FXML
    private TableColumn<Book, String> idBookColumn;
    @FXML
    private TableColumn<Book, String> nomBookColumn;
    // Colonne de catégorie : affiche le nom via la liste des catégories
    @FXML
    private TableColumn<Book, String> catBookColumn;
    @FXML
    private TableColumn<Book, String> dispoBookColumn;
    @FXML
    private TableColumn<Book, String> descriptionColumn;
    @FXML
    private TableColumn<Book, String> pdfFileColumn;
    @FXML
    private TableColumn<Book, String> fileIdColumn;
    @FXML
    private TableColumn<Book, String> pictureColumn;
    // Nouvelle colonne pour le bouton "Voir détails"
    @FXML
    private TableColumn<Book, Void> detailsColumn;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<Categorie> categorieList = FXCollections.observableArrayList();

    private BookService bookService = new BookService();
    private CategorieService categorieService = new CategorieService();

    @FXML
    public void initialize() {
        // Configuration des colonnes standards
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idBookColumn.setCellValueFactory(new PropertyValueFactory<>("idBook"));
        nomBookColumn.setCellValueFactory(new PropertyValueFactory<>("nomBook"));
        catBookColumn.setCellValueFactory(cellData -> {
            int catId = cellData.getValue().getCatBook();
            for (Categorie c : categorieList) {
                if (c.getId() == catId) {
                    return new ReadOnlyStringWrapper(c.getNomCat());
                }
            }
            return new ReadOnlyStringWrapper(String.valueOf(catId));
        });
        dispoBookColumn.setCellValueFactory(new PropertyValueFactory<>("dispoBook"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pdfFileColumn.setCellValueFactory(new PropertyValueFactory<>("pdfFile"));
        fileIdColumn.setCellValueFactory(new PropertyValueFactory<>("fileId"));
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("picture"));

        // Ajout de la colonne "Voir détails" (bouton)
        addDetailsButtonToTable();

        // Charger les catégories dans le ComboBox
        loadCategories();

        // Remplir le ComboBox de disponibilité avec "oui" et "non"
        dispoBookCombo.setItems(FXCollections.observableArrayList("oui", "non"));
        dispoBookCombo.getSelectionModel().selectFirst();

        // Charger les books existants dans la TableView
        loadBookData();

        // Listener sur la TableView pour charger les informations du book sélectionné dans les champs
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idBookField.setText(newSelection.getIdBook());
                nomBookField.setText(newSelection.getNomBook());
                descriptionField.setText(newSelection.getDescription());
                dispoBookCombo.setValue(newSelection.getDispoBook());
                pdfFileLabel.setText(newSelection.getPdfFile());
                pictureLabel.setText(newSelection.getPicture());
                // Sélectionner la catégorie correspondante dans le ComboBox
                for (Categorie c : categorieList) {
                    if (c.getId() == newSelection.getCatBook()) {
                        catBookCombo.setValue(c);
                        break;
                    }
                }
            }
        });
    }

    /**
     * Ajoute une colonne contenant un bouton "Voir détails" pour chaque book.
     */
    private void addDetailsButtonToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                final TableCell<Book, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Voir détails");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Book book = getTableView().getItems().get(getIndex());
                            openBookDetails(book);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        detailsColumn.setCellFactory(cellFactory);
        // La colonne "detailsColumn" est déjà déclarée dans le FXML avec fx:id="detailsColumn"
    }

    /**
     * Ouvre une nouvelle fenêtre affichant les détails du book grâce au fichier BookDetails.fxml.
     */
    private void openBookDetails(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BookDetails.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la vue détail et lui transmettre l'objet Book
            BookDetailsController controller = loader.getController();
            controller.setBook(book);

            Stage stage = new Stage();
            stage.setTitle("Détails du Book");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Erreur lors de l'ouverture des détails.");
        }
    }

    /**
     * Charge les catégories depuis la table "categorie" via le CategorieService dans le ComboBox.
     */
    private void loadCategories() {
        List<Categorie> list = categorieService.getAllCategories();
        categorieList.setAll(list);
        catBookCombo.setItems(categorieList);
        catBookCombo.setConverter(new StringConverter<Categorie>() {
            @Override
            public String toString(Categorie categorie) {
                return categorie != null ? categorie.getNomCat() : "";
            }
            @Override
            public Categorie fromString(String string) {
                return null;
            }
        });
    }

    /**
     * Charge tous les books depuis la base via le BookService et les affiche dans la TableView.
     */
    private void loadBookData() {
        bookList.setAll(bookService.getAllBooks());
        bookTable.setItems(bookList);
        bookTable.refresh();
        statusLabel.setText("Données des books chargées.");
    }

    /**
     * Ouvre un FileChooser pour sélectionner un fichier PDF.
     */
    @FXML
    private void handleSelectPdfFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pdfFileLabel.setText(file.getAbsolutePath());
        }
    }

    /**
     * Ouvre un FileChooser pour sélectionner une image.
     */
    @FXML
    private void handleSelectPictureFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pictureLabel.setText(file.getAbsolutePath());
        }
    }

    /**
     * Gère l'insertion d'un nouveau book via le BookService.
     */
    @FXML
    private void handleAddBook(ActionEvent event) {
        String idBook = idBookField.getText().trim();
        String nomBook = nomBookField.getText().trim();
        String description = descriptionField.getText().trim();
        String dispoBook = dispoBookCombo.getValue();
        String pdfFile = pdfFileLabel.getText();
        String picture = pictureLabel.getText();
        Categorie selectedCategorie = catBookCombo.getValue();

        // Vérifier que les champs obligatoires sont remplis
        if (idBook.isEmpty() || nomBook.isEmpty() || description.isEmpty() ||
                dispoBook == null || pdfFile.isEmpty() || picture.isEmpty() ||
                selectedCategorie == null) {
            statusLabel.setText("Merci de remplir tous les champs obligatoires.");
            return;
        }

        int catBook = selectedCategorie.getId();

        Book book = new Book();
        book.setIdBook(idBook);
        book.setNomBook(nomBook);
        book.setCatBook(catBook);
        book.setDispoBook(dispoBook);
        book.setDescription(description);
        book.setPdfFile(pdfFile);
        book.setFileId(""); // Vous pouvez ajouter la logique pour générer le file_id si nécessaire
        book.setPicture(picture);

        boolean success = bookService.createBook(book);
        if (success) {
            bookList.add(book);
            bookTable.refresh();
            statusLabel.setText("Book ajouté !");
        } else {
            statusLabel.setText("Erreur lors de l'ajout du book.");
        }
    }

    /**
     * Gère la mise à jour du book sélectionné via le BookService.
     */
    @FXML
    private void handleUpdateBook(ActionEvent event) {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Veuillez sélectionner un book à mettre à jour.");
            return;
        }

        String idBook = idBookField.getText().trim();
        String nomBook = nomBookField.getText().trim();
        String description = descriptionField.getText().trim();
        String dispoBook = dispoBookCombo.getValue();
        String pdfFile = pdfFileLabel.getText();
        String picture = pictureLabel.getText();
        Categorie selectedCategorie = catBookCombo.getValue();

        if (idBook.isEmpty() || nomBook.isEmpty() || description.isEmpty() ||
                dispoBook == null || pdfFile.isEmpty() || picture.isEmpty() ||
                selectedCategorie == null) {
            statusLabel.setText("Merci de remplir tous les champs pour la mise à jour.");
            return;
        }

        selected.setIdBook(idBook);
        selected.setNomBook(nomBook);
        selected.setDescription(description);
        selected.setDispoBook(dispoBook);
        selected.setPdfFile(pdfFile);
        selected.setPicture(picture);
        selected.setCatBook(selectedCategorie.getId());

        boolean success = bookService.updateBook(selected);
        if (success) {
            bookTable.refresh();
            statusLabel.setText("Book mis à jour !");
        } else {
            statusLabel.setText("Erreur lors de la mise à jour du book.");
        }
    }

    /**
     * Gère la suppression du book sélectionné via le BookService.
     */
    @FXML
    private void handleDeleteBook(ActionEvent event) {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Veuillez sélectionner un book à supprimer.");
            return;
        }

        boolean success = bookService.deleteBook(selected.getId());
        if (success) {
            bookList.remove(selected);
            bookTable.refresh();
            statusLabel.setText("Book supprimé !");
        } else {
            statusLabel.setText("Erreur lors de la suppression du book.");
        }
    }
}
