package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;
import model.Categorie;
import service.BookService;
import service.CategorieService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FrontBookController {

    @FXML private TextField searchField;
    @FXML private FlowPane bookContainer;
    @FXML private ComboBox<Categorie> categoryFilterCombo;
    @FXML private ComboBox<String> availabilityFilterCombo;
    @FXML private ComboBox<String> sortOrderCombo;

    private final ObservableList<Book> bookList         = FXCollections.observableArrayList();
    private final ObservableList<Categorie> categorieList = FXCollections.observableArrayList();
    private final BookService bookService               = new BookService();
    private final CategorieService categorieService     = new CategorieService();

    private Categorie allCategoriesOption;

    @FXML
    public void initialize() {
        // 1) Charger données
        List<Book> books = bookService.getAllBooks();
        bookList.setAll(books);

        List<Categorie> cats = categorieService.getAllCategories();
        allCategoriesOption = new Categorie();
        allCategoriesOption.setId(0);
        allCategoriesOption.setNomCat("Toutes catégories");
        categorieList.add(allCategoriesOption);
        categorieList.addAll(cats);

        // 2) Initialisation filtres
        categoryFilterCombo.setItems(categorieList);
        categoryFilterCombo.getSelectionModel().select(allCategoriesOption);
        categoryFilterCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Categorie item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item==null ? null : item.getNomCat());
            }
        });
        categoryFilterCombo.setButtonCell(categoryFilterCombo.getCellFactory().call(null));

        availabilityFilterCombo.setItems(FXCollections.observableArrayList("Tous","oui","non"));
        availabilityFilterCombo.getSelectionModel().selectFirst();

        sortOrderCombo.setItems(FXCollections.observableArrayList("A → Z","Z → A"));
        sortOrderCombo.getSelectionModel().selectFirst();

        // 3) Listeners
        searchField.textProperty().addListener((obs, ov, nv) -> applyFiltersAndSort());
        categoryFilterCombo.valueProperty().addListener((obs, ov, nv) -> applyFiltersAndSort());
        availabilityFilterCombo.valueProperty().addListener((obs, ov, nv) -> applyFiltersAndSort());
        sortOrderCombo.valueProperty().addListener((obs, ov, nv) -> applyFiltersAndSort());

        // 4) Affichage initial
        applyFiltersAndSort();
    }

    private void applyFiltersAndSort() {
        bookContainer.getChildren().clear();

        String keyword    = searchField.getText().trim().toLowerCase();
        Categorie selCat  = categoryFilterCombo.getValue();
        String    selDisp = availabilityFilterCombo.getValue();
        String    order   = sortOrderCombo.getValue();

        List<Book> filtered = bookList.stream()
                .filter(b -> b.getNomBook().toLowerCase().contains(keyword))
                .filter(b -> selCat.getId()==0 || b.getCatBook()==selCat.getId())
                .filter(b -> "Tous".equals(selDisp) || b.getDispoBook().equalsIgnoreCase(selDisp))
                .sorted(Comparator.comparing(Book::getNomBook, String.CASE_INSENSITIVE_ORDER)
                        .reversed().reversed() // trick to handle both orders
                        .thenComparing(b->b.getNomBook()))
                .collect(Collectors.toList());

        if ("Z → A".equals(order)) {
            filtered.sort(Comparator.comparing(Book::getNomBook, String.CASE_INSENSITIVE_ORDER).reversed());
        }

        filtered.forEach(b -> bookContainer.getChildren().add(createBookCard(b)));
    }

    private VBox createBookCard(Book book) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setPrefWidth(200);
        card.setStyle(
                "-fx-background-color:#FFFFFF;"
                        + "-fx-background-radius:10;"
                        + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.1),10,0,0,3);"
        );

        Label nameLabel     = new Label(book.getNomBook());
        nameLabel.setStyle("-fx-font-size:16px;-fx-font-weight:bold;");

        String catName = categorieList.stream()
                .filter(c->c.getId()==book.getCatBook())
                .map(Categorie::getNomCat)
                .findFirst().orElse("Inconnue");
        Label categoryLabel = new Label("Catégorie : "+catName);
        Label availLabel    = new Label("Disponibilité : "+book.getDispoBook());

        Button btn = new Button("Voir plus");
        btn.setStyle("-fx-background-color:#1E90FF;-fx-text-fill:#FFFFFF;");
        btn.setOnAction(e->openBookDetails(book));

        card.getChildren().addAll(nameLabel, categoryLabel, availLabel, btn);
        return card;
    }

    private void openBookDetails(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BookDetails.fxml"));
            Parent root       = loader.load();
            controller.BookDetailsController ctrl = loader.getController();
            ctrl.setBook(book);

            Stage stage = new Stage();
            stage.setTitle("Détails du Book");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
