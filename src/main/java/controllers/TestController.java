package controllers;

import entities.Question;
import entities.Test;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.QuestionService;
import services.TestService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    // Main container for questions
    @FXML
    private VBox questionsContainer;

    @FXML
    private ScrollPane questionsScrollPane;

    @FXML
    private Button addQuestionButton;

    private int formationId;
    private int testId;
    private int questionCount = 5; // Start with 5 questions

    private List<QuestionBox> questionBoxes = new ArrayList<>();

    private TestService testService = new TestService();
    private QuestionService questionService = new QuestionService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize with 5 questions by default
        questionsContainer.setSpacing(20);

        // Add initial questions
        for (int i = 1; i <= questionCount; i++) {
            addQuestionBox();
        }

        // Setup the Add Question button
        addQuestionButton.setOnAction(e -> addQuestionBox());
    }

    /**
     * Méthode appelée depuis l'interface précédente pour transmettre l'ID de la formation.
     * Elle crée ou récupère le test associé à cette formation.
     */
    public void setFormationId(int formationId) {
        this.formationId = formationId;
        try {
            // Création ou récupération du test pour cette formation
            Test t = new Test(formationId);
            this.testId = testService.creerTestSiInexistant(t);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la création du test.");
        }
    }

    /**
     * Adds a new question box to the container
     */
    private void addQuestionBox() {
        int questionNumber = questionBoxes.size() + 1;
        QuestionBox questionBox = new QuestionBox(questionNumber);
        questionBoxes.add(questionBox);
        questionsContainer.getChildren().add(questionBox.getBox());
        updateQuestionNumbers();
    }

    /**
     * Removes a question box from the container
     */
    private void removeQuestionBox(QuestionBox questionBox) {
        if (questionBoxes.size() <= 1) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Vous devez avoir au moins une question.");
            return;
        }

        questionBoxes.remove(questionBox);
        questionsContainer.getChildren().remove(questionBox.getBox());
        updateQuestionNumbers();
    }

    /**
     * Updates question numbers after add/remove operations
     */
    private void updateQuestionNumbers() {
        for (int i = 0; i < questionBoxes.size(); i++) {
            questionBoxes.get(i).setQuestionNumber(i + 1);
        }
    }

    /**
     * Cette méthode est appelée lors du clic sur "Enregistrer le Test".
     * Elle crée les questions et les enregistre en BDD.
     */
    @FXML
    private void enregistrerTest() {
        try {
            if (questionBoxes.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Votre test doit contenir au moins une question.");
                return;
            }

            // Validate all questions first
            for (int i = 0; i < questionBoxes.size(); i++) {
                QuestionBox qBox = questionBoxes.get(i);
                if (!qBox.validateQuestion()) {
                    return; // Stop if validation fails
                }
            }

            // Create and save all questions
            for (QuestionBox qBox : questionBoxes) {
                Question question = qBox.createQuestion();
                questionService.ajouter(question);
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le test a été enregistré !");

            // Return to the formations list
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeFormations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) questionsContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de sauvegarder le test.");
        }
    }

    /**
     * Méthode utilitaire pour afficher une alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Inner class to encapsulate a question's UI elements
     */
    private class QuestionBox {
        private VBox box;
        private Label titleLabel;
        private TextArea enonce;
        private TextField answer1, answer2, answer3, answer4;
        private TextField correctAnswer;
        private int questionNumber;

        public QuestionBox(int number) {
            this.questionNumber = number;

            // Create the container
            box = new VBox();
            box.setSpacing(10);
            box.getStyleClass().add("modern-question-box");

            // Create the header with title and delete button
            HBox header = new HBox();
            header.setAlignment(Pos.CENTER_LEFT);
            header.setSpacing(10);

            // Title label
            titleLabel = new Label("Question " + number);
            titleLabel.getStyleClass().add("form-section-title");

            // Delete button
            Button deleteButton = new Button("✖");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            deleteButton.setOnAction(e -> removeQuestionBox(this));

            header.getChildren().addAll(titleLabel, deleteButton);

            // Question content
            enonce = new TextArea();
            enonce.setPromptText("Saisissez l'énoncé...");
            enonce.setPrefHeight(60);
            enonce.getStyleClass().add("search-input");

            // First row of answers
            HBox firstAnswerRow = new HBox();
            firstAnswerRow.setSpacing(15);

            VBox answer1Box = createAnswerBox("Réponse 1:");
            answer1 = (TextField) answer1Box.getChildren().get(1);

            VBox answer2Box = createAnswerBox("Réponse 2:");
            answer2 = (TextField) answer2Box.getChildren().get(1);

            firstAnswerRow.getChildren().addAll(answer1Box, answer2Box);
            HBox.setHgrow(answer1Box, javafx.scene.layout.Priority.ALWAYS);
            HBox.setHgrow(answer2Box, javafx.scene.layout.Priority.ALWAYS);

            // Second row of answers
            HBox secondAnswerRow = new HBox();
            secondAnswerRow.setSpacing(15);

            VBox answer3Box = createAnswerBox("Réponse 3:");
            answer3 = (TextField) answer3Box.getChildren().get(1);

            VBox answer4Box = createAnswerBox("Réponse 4:");
            answer4 = (TextField) answer4Box.getChildren().get(1);

            secondAnswerRow.getChildren().addAll(answer3Box, answer4Box);
            HBox.setHgrow(answer3Box, javafx.scene.layout.Priority.ALWAYS);
            HBox.setHgrow(answer4Box, javafx.scene.layout.Priority.ALWAYS);

            // Correct answer row
            HBox correctAnswerRow = new HBox();
            correctAnswerRow.setSpacing(10);
            correctAnswerRow.setAlignment(Pos.CENTER_LEFT);

            Label correctLabel = new Label("Réponse correcte (1-4):");
            correctLabel.getStyleClass().add("info-label");

            correctAnswer = new TextField();
            correctAnswer.setPrefWidth(60);
            correctAnswer.setPromptText("?");
            correctAnswer.getStyleClass().add("search-input");

            correctAnswerRow.getChildren().addAll(correctLabel, correctAnswer);

            // Add all elements to the main box
            box.getChildren().addAll(header, enonce, firstAnswerRow, secondAnswerRow, correctAnswerRow);
        }

        private VBox createAnswerBox(String labelText) {
            VBox answerBox = new VBox();
            answerBox.setSpacing(5);

            Label label = new Label(labelText);
            label.getStyleClass().add("info-label");

            TextField field = new TextField();
            field.getStyleClass().add("search-input");
            field.setPromptText(labelText.replace(":", ""));

            answerBox.getChildren().addAll(label, field);
            return answerBox;
        }

        public VBox getBox() {
            return box;
        }

        public void setQuestionNumber(int number) {
            this.questionNumber = number;
            titleLabel.setText("Question " + number);
        }

        public boolean validateQuestion() {
            // Validation de l'énoncé
            if (enonce.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "L'énoncé de la question " + questionNumber + " est obligatoire.");
                enonce.requestFocus();
                return false;
            }

            if (enonce.getText().trim().length() < 5) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "L'énoncé de la question " + questionNumber + " est trop court.");
                enonce.requestFocus();
                return false;
            }

            // Validation des réponses
            if (answer1.getText().trim().isEmpty() ||
                    answer2.getText().trim().isEmpty() ||
                    answer3.getText().trim().isEmpty() ||
                    answer4.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Toutes les réponses de la question " + questionNumber + " sont obligatoires.");
                return false;
            }

            // Validation de la réponse correcte
            try {
                int correctAnswerValue = Integer.parseInt(correctAnswer.getText().trim());
                if (correctAnswerValue < 1 || correctAnswerValue > 4) {
                    showAlert(Alert.AlertType.WARNING, "Erreur",
                            "La réponse correcte de la question " + questionNumber + " doit être un chiffre entre 1 et 4.");
                    correctAnswer.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Erreur",
                        "La réponse correcte de la question " + questionNumber + " doit être un chiffre entre 1 et 4.");
                correctAnswer.requestFocus();
                return false;
            }

            return true;
        }

        public Question createQuestion() {
            return new Question(
                    testId,
                    enonce.getText().trim(),
                    answer1.getText().trim(),
                    answer2.getText().trim(),
                    answer3.getText().trim(),
                    answer4.getText().trim(),
                    Integer.parseInt(correctAnswer.getText().trim())
            );
        }
    }
}