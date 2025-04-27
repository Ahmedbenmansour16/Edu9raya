package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import service.BookService;
import service.CategorieService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatBookController {

    @FXML private BarChart<String, Number> categoryChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML private BarChart<String, Number> availabilityChart;
    @FXML private CategoryAxis availXAxis;
    @FXML private NumberAxis availYAxis;

    @FXML private Label totalBooksLabel;
    @FXML private Label totalCategoriesLabel;

    private final BookService bookService = new BookService();
    private final CategorieService categorieService = new CategorieService();

    @FXML
    public void initialize() {
        // Totaux
        List<?> allBooks = bookService.getAllBooks();
        totalBooksLabel.setText(String.valueOf(allBooks.size()));
        totalCategoriesLabel.setText(String.valueOf(categorieService.getAllCategories().size()));

        // Books par catégorie
        Map<String, Long> byCat = allBooks.stream()
                .collect(Collectors.groupingBy(
                        b -> {
                            int catId = ((model.Book)b).getCatBook();
                            return categorieService.getAllCategories().stream()
                                    .filter(c -> c.getId() == catId)
                                    .findFirst()
                                    .map(model.Categorie::getNomCat)
                                    .orElse("Inconnue");
                        },
                        Collectors.counting()
                ));
        XYChart.Series<String, Number> catSeries = new XYChart.Series<>();
        byCat.forEach((cat, cnt) -> catSeries.getData().add(new XYChart.Data<>(cat, cnt)));
        categoryChart.getData().add(catSeries);

        // Configurer ticks de yAxis à 1
        double maxCat = byCat.values().stream().mapToDouble(Long::doubleValue).max().orElse(1);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(maxCat + 1);
        yAxis.setTickUnit(1);

        // Books par disponibilité
        Map<String, Long> byAvail = allBooks.stream()
                .collect(Collectors.groupingBy(
                        b -> ((model.Book)b).getDispoBook(),
                        Collectors.counting()
                ));
        XYChart.Series<String, Number> availSeries = new XYChart.Series<>();
        byAvail.forEach((disp, cnt) -> availSeries.getData().add(new XYChart.Data<>(disp, cnt)));
        availabilityChart.getData().add(availSeries);

        // Configurer ticks de availYAxis à 1
        double maxAvail = byAvail.values().stream().mapToDouble(Long::doubleValue).max().orElse(1);
        availYAxis.setAutoRanging(false);
        availYAxis.setLowerBound(0);
        availYAxis.setUpperBound(maxAvail + 1);
        availYAxis.setTickUnit(1);
    }
}
