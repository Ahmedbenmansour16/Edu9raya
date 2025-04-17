package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import service.BookService;
import service.CategorieService;
import model.Book;
import model.Categorie;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatBookController {

    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis categoryAxis;
    @FXML private NumberAxis numberAxis;

    private final BookService bookService = new BookService();
    private final CategorieService categorieService = new CategorieService();

    @FXML
    public void initialize() {
        // Récupérer tous les books et leur catégorie
        List<Book> books = bookService.getAllBooks();
        List<Categorie> cats = categorieService.getAllCategories();

        // Calculer le nombre de books par catégorie
        Map<Integer, Long> counts = books.stream()
                .collect(Collectors.groupingBy(Book::getCatBook, Collectors.counting()));

        // Créer une série de données
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Books par catégorie");

        // Pour chaque catégorie, ajouter au graphique
        for (Categorie c : cats) {
            long count = counts.getOrDefault(c.getId(), 0L);
            series.getData().add(new XYChart.Data<>(c.getNomCat(), count));
        }

        barChart.getData().add(series);
    }
}