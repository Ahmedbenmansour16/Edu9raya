package controllers.Cours;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import entities.Cour;
import entities.Feedback;
import entities.Rating;
import services.CourService;
import services.FeedbackService;
import services.RatingService;
import services.ViewsService;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CoursList implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vboxContainer;
    @FXML
    private TextField searchField;
    @FXML
    private Button logoutBtn;

    private final CourService courService = new CourService();
    private List<Cour> allCourses;
    private Set<Integer> viewedCourseIds = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String defaultLevel = "Débutant";
        int fakeExcludedCourseId = -1;
        showRecommendationsAndAllCourses(defaultLevel, fakeExcludedCourseId);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterCourses(newVal));
    }

    private void loadCourses() {
        vboxContainer.getChildren().clear();
        allCourses = courService.retrieveAll();
        if (allCourses.isEmpty()) {
            Label noCoursesLabel = new Label("No courses found.");
            noCoursesLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
            VBox.setMargin(noCoursesLabel, new Insets(20, 0, 0, 0));
            vboxContainer.getChildren().add(noCoursesLabel);
            return;
        }
        displayCourses(allCourses);
    }

    private void filterCourses(String searchText) {
        vboxContainer.getChildren().clear();
        if (allCourses == null) allCourses = courService.retrieveAll();
        List<Cour> filteredCourses = allCourses.stream()
                .filter(course -> course.getTitre().toLowerCase().contains(searchText.toLowerCase()) ||
                        course.getCodeCours().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        if (filteredCourses.isEmpty()) {
            Label noCoursesLabel = new Label("No courses match your search.");
            noCoursesLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
            VBox.setMargin(noCoursesLabel, new Insets(20, 0, 0, 0));
            vboxContainer.getChildren().add(noCoursesLabel);
            return;
        }
        displayCourses(filteredCourses);
    }

    private void displayCourses(List<Cour> courses) {
        HBox currentRow = null;
        for (int i = 0; i < courses.size(); i++) {
            if (i % 2 == 0) {
                currentRow = new HBox();
                currentRow.setSpacing(120);
                currentRow.setPrefWidth(1180);
                VBox.setMargin(currentRow, new Insets(0, 0, 30, 50));
                vboxContainer.getChildren().add(currentRow);
            }
            Pane card = createCourseCard(courses.get(i));
            currentRow.getChildren().add(card);
        }
    }

    private Pane createCourseCard(Cour course) {
        Pane card = new Pane();
        card.setPrefSize(420, 650);
        card.setStyle("-fx-border-color: #34C759; -fx-border-radius: 5; -fx-background-color: #FFFFFF; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label titleLabel = new Label(course.getTitre());
        titleLabel.setLayoutX(20);
        titleLabel.setLayoutY(20);
        titleLabel.setPrefWidth(380);
        titleLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #181D38;");
        titleLabel.setWrapText(true);

        Label codeLabel = new Label("Code: " + course.getCodeCours());
        codeLabel.setLayoutX(20);
        codeLabel.setLayoutY(60);
        codeLabel.setPrefWidth(380);
        codeLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        codeLabel.setWrapText(true);

        Label levelLabel = new Label("Level: " + course.getNiveau());
        levelLabel.setLayoutX(20);
        levelLabel.setLayoutY(100);
        levelLabel.setPrefWidth(380);
        levelLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        levelLabel.setWrapText(true);

        Label categoryLabel = new Label("Category: " + course.getCategorie());
        categoryLabel.setLayoutX(20);
        categoryLabel.setLayoutY(140);
        categoryLabel.setPrefWidth(380);
        categoryLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        categoryLabel.setWrapText(true);

        Label descriptionLabel = new Label("Description: " + course.getDescription());
        descriptionLabel.setLayoutX(20);
        descriptionLabel.setLayoutY(180);
        descriptionLabel.setPrefWidth(380);
        descriptionLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        descriptionLabel.setWrapText(true);

        Label moduleNameLabel = new Label("Module: " + (course.getModule() != null ? course.getModule().getNom() : "N/A"));
        moduleNameLabel.setLayoutX(20);
        moduleNameLabel.setLayoutY(260);
        moduleNameLabel.setPrefWidth(380);
        moduleNameLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        moduleNameLabel.setWrapText(true);

        Label moduleTeacherLabel = new Label("Teacher: " + (course.getModule() != null ? course.getModule().getEnseignant() : "N/A"));
        moduleTeacherLabel.setLayoutX(20);
        moduleTeacherLabel.setLayoutY(300);
        moduleTeacherLabel.setPrefWidth(380);
        moduleTeacherLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        moduleTeacherLabel.setWrapText(true);

        RatingService ratingService = new RatingService();
        double avgRating = ratingService.getAverageRating(course);
        Label ratingLabel = new Label(String.format("Rating: %.1f ⭐", avgRating));
        ratingLabel.setLayoutX(20);
        ratingLabel.setLayoutY(340);
        ratingLabel.setPrefWidth(380);
        ratingLabel.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #181D38;");
        ratingLabel.setWrapText(true);

        HBox starBox = new HBox();
        starBox.setLayoutX(20);
        starBox.setLayoutY(380);
        starBox.setSpacing(10);
        List<Button> starButtons = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Button starButton = new Button("⭐");
            final int ratingValue = i;
            starButton.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-text-fill: #C0C0C0;");
            starButton.setOnAction(e -> {
                RatingService rs = new RatingService();
                rs.addRating(new Rating(course, ratingValue));
                double newAvgRating = rs.getAverageRating(course);
                ratingLabel.setText(String.format("Rating: %.1f ⭐", newAvgRating));
                for (int j = 0; j < 5; j++) {
                    if (j < ratingValue) {
                        starButtons.get(j).setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-text-fill: #000000;");
                    } else {
                        starButtons.get(j).setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-text-fill: #C0C0C0;");
                    }
                }
            });
            starButtons.add(starButton);
            starBox.getChildren().add(starButton);
        }

        FeedbackService feedbackService = new FeedbackService();
        List<entities.Feedback> feedbacks = feedbackService.getFeedbacksByCourse(course);
        VBox feedbackBox = new VBox();
        feedbackBox.setLayoutX(20);
        feedbackBox.setLayoutY(420);
        feedbackBox.setSpacing(10);
        Label feedbackTitle = new Label("Feedbacks:");
        feedbackTitle.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #181D38;");
        feedbackBox.getChildren().add(feedbackTitle);
        for (entities.Feedback feedback : feedbacks) {
            HBox feedbackRow = new HBox();
            feedbackRow.setSpacing(10);
            Label feedbackContent = new Label(feedback.getContent());
            feedbackContent.setPrefWidth(220);
            feedbackContent.setWrapText(true);
            feedbackContent.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-text-fill: #181D38;");
            Button editButton = new Button("✏");
            editButton.setOnAction(e -> {
                TextInputDialog dialog = new TextInputDialog(feedback.getContent());
                dialog.setTitle("Edit Feedback");
                dialog.setHeaderText("Edit Feedback Content");
                dialog.setContentText("New Content:");
                dialog.showAndWait().ifPresent(newContent -> {
                    feedback.setContent(newContent);
                    feedbackService.updateFeedback(feedback);
                    feedbackContent.setText(newContent);
                });
            });
            Button deleteButton = new Button("🗑");
            deleteButton.setOnAction(e -> {
                feedbackService.deleteFeedback(feedback.getId());
                feedbackBox.getChildren().remove(feedbackRow);
            });
            feedbackRow.getChildren().addAll(feedbackContent, editButton, deleteButton);
            feedbackBox.getChildren().add(feedbackRow);
        }
        Button addFeedbackButton = new Button("➕ Add Feedback");
        addFeedbackButton.setStyle("-fx-background-color: #34C759; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;");
        addFeedbackButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Feedback");
            dialog.setHeaderText("Write your feedback");
            dialog.setContentText("Feedback:");
            dialog.showAndWait().ifPresent(content -> {
                if (!content.trim().isEmpty()) {
                    entities.Feedback newFeedback = new entities.Feedback(course, content);
                    feedbackService.addFeedback(newFeedback);
                    HBox newFeedbackRow = new HBox();
                    newFeedbackRow.setSpacing(10);
                    Label newFeedbackContent = new Label(content);
                    newFeedbackContent.setPrefWidth(220);
                    newFeedbackContent.setWrapText(true);
                    newFeedbackContent.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-text-fill: #181D38;");
                    Button newEditButton = new Button("✏️");
                    newEditButton.setOnAction(editEvent -> {
                        TextInputDialog editDialog = new TextInputDialog(content);
                        editDialog.setTitle("Edit Feedback");
                        editDialog.setHeaderText("Edit Feedback Content");
                        editDialog.setContentText("New Content:");
                        editDialog.showAndWait().ifPresent(newContent -> {
                            newFeedback.setContent(newContent);
                            feedbackService.updateFeedback(newFeedback);
                            newFeedbackContent.setText(newContent);
                        });
                    });
                    Button newDeleteButton = new Button("🗑️");
                    newDeleteButton.setOnAction(deleteEvent -> {
                        feedbackService.deleteFeedback(newFeedback.getId());
                        feedbackBox.getChildren().remove(newFeedbackRow);
                    });
                    newFeedbackRow.getChildren().addAll(newFeedbackContent, newEditButton, newDeleteButton);
                    feedbackBox.getChildren().add(newFeedbackRow);
                }
            });
        });
        feedbackBox.getChildren().add(addFeedbackButton);

        Button updateButton = new Button("Update");
        updateButton.setLayoutX(20);
        updateButton.setLayoutY(610);
        updateButton.setPrefWidth(120);
        updateButton.setPrefHeight(30);
        updateButton.setStyle("-fx-background-color: #34C759; -fx-background-radius: 5; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-family: 'Nunito'; -fx-font-weight: 600;");
        updateButton.setOnAction(e -> handleUpdateCourse(course));

        Button deleteButton = new Button("Delete");
        deleteButton.setLayoutX(150);
        deleteButton.setLayoutY(610);
        deleteButton.setPrefWidth(120);
        deleteButton.setPrefHeight(30);
        deleteButton.setStyle("-fx-background-color: #181D38; -fx-background-radius: 5; -fx-text-fill: #FFFFFF;");
        deleteButton.setOnAction(e -> handleDeleteCourse(course));

        Button viewButton = new Button("View");
        viewButton.setLayoutX(280);
        viewButton.setLayoutY(610);
        viewButton.setPrefWidth(120);
        viewButton.setPrefHeight(30);
        viewButton.setStyle("-fx-background-color: #FFA500; -fx-background-radius: 5; -fx-text-fill: #FFFFFF;");
        viewButton.setOnAction(e -> handleViewCourse(course));

        card.getChildren().addAll(
                titleLabel, codeLabel, levelLabel, categoryLabel, descriptionLabel,
                moduleNameLabel, moduleTeacherLabel,
                ratingLabel, starBox,
                feedbackBox,
                updateButton, deleteButton, viewButton
        );
        return card;
    }

    private void handleViewCourse(Cour course) {
        ViewsService viewsService = new ViewsService();
        viewsService.incrementView(course.getModule());
        viewedCourseIds.add(course.getId());
        showRecommendationsAndAllCourses(course.getNiveau(), course.getId());
    }

    private void showRecommendationsAndAllCourses(String niveau, int excludedCourseId) {
        CourService courService = new CourService();
        List<Cour> recommendedCourses = getRecommendedCoursesByLevel(niveau, excludedCourseId);
        List<Cour> allCourses = courService.retrieveAll();
        vboxContainer.getChildren().clear();
        Label recommendedTitle = new Label("Recommended Courses (Level: " + niveau + ")");
        recommendedTitle.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #181D38;");
        vboxContainer.getChildren().add(recommendedTitle);
        if (recommendedCourses.isEmpty()) {
            Label noRecommendations = new Label("No recommendations available.");
            noRecommendations.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 16px; -fx-text-fill: #FF4D4D;");
            vboxContainer.getChildren().add(noRecommendations);
        } else {
            HBox currentRow = null;
            for (int i = 0; i < recommendedCourses.size(); i++) {
                if (i % 2 == 0) {
                    currentRow = new HBox();
                    currentRow.setSpacing(120);
                    currentRow.setPrefWidth(1080);
                    VBox.setMargin(currentRow, new Insets(20, 0, 0, 0));
                    vboxContainer.getChildren().add(currentRow);
                }
                Pane card = createCourseCard(recommendedCourses.get(i));
                currentRow.getChildren().add(card);
            }
        }
        Label allCoursesTitle = new Label("All Courses");
        allCoursesTitle.setStyle("-fx-font-family: 'Nunito'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #181D38;");
        VBox.setMargin(allCoursesTitle, new Insets(40, 0, 0, 0));
        vboxContainer.getChildren().add(allCoursesTitle);
        HBox currentRowAll = null;
        for (int i = 0; i < allCourses.size(); i++) {
            if (i % 2 == 0) {
                currentRowAll = new HBox();
                currentRowAll.setSpacing(120);
                currentRowAll.setPrefWidth(1080);
                VBox.setMargin(currentRowAll, new Insets(20, 0, 0, 0));
                vboxContainer.getChildren().add(currentRowAll);
            }
            Pane card = createCourseCard(allCourses.get(i));
            currentRowAll.getChildren().add(card);
        }
    }

    private List<Cour> getRecommendedCoursesByLevel(String niveau, int excludedCourseId) {
        CourService courService = new CourService();
        List<Cour> allCourses = courService.retrieveAll();
        return allCourses.stream()
                .filter(c -> c.getNiveau().equalsIgnoreCase(niveau))
                .filter(c -> !viewedCourseIds.contains(c.getId()))
                .filter(c -> c.getId() != excludedCourseId)
                .toList();
    }

    private void handleUpdateCourse(Cour course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/UpdateCours.fxml"));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof UpdateCours updateCoursController) {
                updateCoursController.initializeCourse(course);
            }
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error while loading UpdateCours.fxml: " + e.getMessage());
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Navigation Failed");
            errorAlert.setContentText("Unable to load the update page: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void handleDeleteCourse(Cour course) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Delete Course");
        confirmationAlert.setContentText("Are you sure you want to delete the course '" + course.getTitre() + "'?");
        DialogPane dialogPane = confirmationAlert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #F5F5F5; -fx-font-family: 'Nunito';");
        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #34C759; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
        );
        dialogPane.lookupButton(ButtonType.CANCEL).setStyle(
                "-fx-background-color: #181D38; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
        );
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    courService.delete(course.getId());
                    loadCourses();
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Deletion Failed");
                    errorAlert.setContentText("Error while deleting the course: " + e.getMessage());
                    DialogPane errorDialogPane = errorAlert.getDialogPane();
                    errorDialogPane.setStyle("-fx-background-color: #F5F5F5; -fx-font-family: 'Nunito';");
                    errorDialogPane.lookupButton(ButtonType.OK).setStyle(
                            "-fx-background-color: #34C759; -fx-text-fill: #FFFFFF; -fx-font-family: 'Nunito'; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 5;"
                    );
                    errorAlert.showAndWait();
                }
            }
        });
    }

    @FXML
    void showCoursList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/CoursList.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error while loading CoursList.fxml: " + e.getMessage());
        }
    }

    @FXML
    void showAddCours(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cours/AddCours.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error while loading AddCours.fxml: " + e.getMessage());
        }
    }

    // Sidebar navigation methods
    @FXML
    private void ouvrirListeModules() {
        openView("/Module/ModuleList.fxml", "Liste des Modules");
    }
    @FXML
    private void ouvrirAjouterModule() {
        openView("/Module/AddModule.fxml", "Ajouter un Module");
    }
    @FXML
    private void ouvrirListeCours() {
        openView("/Cours/CoursList.fxml", "Liste des Cours");
    }
    @FXML
    private void ouvrirAjouterCours() {
        openView("/Cours/CoursDetails.fxml", "Ajouter un Cours");
    }
    @FXML
    private void ouvrirListeCategories() {
        openView("/Categorie/CategorieList.fxml", "Liste des Catégories");
    }
    @FXML
    private void ouvrirAjouterCategorie() {
        openView("/Categorie/CategorieDetails.fxml", "Ajouter une Catégorie");
    }
    @FXML
    private void refreshFormations() {
        openView("/ListeFormations.fxml", "Liste des Formations");
    }
    @FXML
    private void refreshCoursList() {
        loadCourses();
    }
    private void openView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) vboxContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchCours() {
        filterCourses(searchField.getText());
    }
} 