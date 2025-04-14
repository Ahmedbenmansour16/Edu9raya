package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import model.Book;
import model.Categorie;
import service.BookService;
import service.CategorieService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FrontBookController {

    @FXML
    private FlowPane bookContainer;

    // Listes pour stocker les livres et les catégories
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<Categorie> categorieList = FXCollections.observableArrayList();

    private BookService bookService = new BookService();
    private CategorieService categorieService = new CategorieService();

    @FXML
    public void initialize() {
        // Charger la liste des livres depuis le BookService
        List<Book> books = bookService.getAllBooks();
        bookList.setAll(books);

        // Charger la liste des catégories depuis le CategorieService
        List<Categorie> cats = categorieService.getAllCategories();
        categorieList.setAll(cats);

        // Pour chaque livre, créer une "carte" affichant les informations voulues
        for (Book book : bookList) {
            VBox card = createBookCard(book);
            bookContainer.getChildren().add(card);
        }
    }

    /**
     * Crée une "carte" (VBox) pour un livre donné.
     * La carte affiche le nom du livre, le nom de la catégorie et la disponibilité,
     * et inclut un bouton "Voir plus" qui ouvre la page de détails.
     *
     * @param book L'objet Book à afficher.
     * @return Un VBox contenant la carte du livre.
     */
    private VBox createBookCard(Book book) {
        VBox card = new VBox(10); // Espacement de 10 pixels entre les éléments
        card.setPadding(new Insets(10));
        // Style inline pour la carte (vous pouvez aussi utiliser une classe CSS "card")
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        card.setPrefWidth(200);

        // Nom du livre
        Label nameLabel = new Label(book.getNomBook());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Récupération du nom de la catégorie
        String catName = "Inconnue";
        for (Categorie c : categorieList) {
            if (c.getId() == book.getCatBook()) {
                catName = c.getNomCat();
                break;
            }
        }
        Label categoryLabel = new Label("Catégorie : " + catName);

        // Disponibilité
        Label availLabel = new Label("Disponibilité : " + book.getDispoBook());

        // Bouton "Voir plus"
        Button detailsBtn = new Button("Voir plus");
        // Appliquer un style inline ou via une classe CSS (par exemple, "card-button")
        detailsBtn.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: #FFFFFF;");
        detailsBtn.setOnAction(e -> {
            // Appel à la méthode pour ouvrir la page de détails du book
            openBookDetails(book);
        });

        card.getChildren().addAll(nameLabel, categoryLabel, availLabel, detailsBtn);
        return card;
    }

    /**
     * Ouvre une nouvelle fenêtre affichant les détails du book.
     * Charge le fichier BookDetails.fxml et transmet l'objet Book au contrôleur.
     *
     * @param book L'objet Book dont les détails doivent être affichés.
     */
    private void openBookDetails(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BookDetails.fxml"));
            Parent root = loader.load();

            // Récupération du contrôleur et passage de l'objet book
            BookDetailsController controller = loader.getController();
            controller.setBook(book);

            Stage stage = new Stage();
            stage.setTitle("Détails du Book");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
