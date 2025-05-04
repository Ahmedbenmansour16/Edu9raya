package controllers;

import entities.Question;
import entities.Test;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.QuestionService;
import services.TestService;

import java.io.IOException;
import java.sql.SQLException;

public class TestController {

    // Champs pour la Question 1
    @FXML
    private TextArea enonce1;
    @FXML
    private TextField answer1Q1;
    @FXML
    private TextField answer2Q1;
    @FXML
    private TextField answer3Q1;
    @FXML
    private TextField answer4Q1;
    @FXML
    private TextField correctQ1;

    // Champs pour la Question 2
    @FXML
    private TextArea enonce2;
    @FXML
    private TextField answer1Q2;
    @FXML
    private TextField answer2Q2;
    @FXML
    private TextField answer3Q2;
    @FXML
    private TextField answer4Q2;
    @FXML
    private TextField correctQ2;

    // Champs pour la Question 3
    @FXML
    private TextArea enonce3;
    @FXML
    private TextField answer1Q3;
    @FXML
    private TextField answer2Q3;
    @FXML
    private TextField answer3Q3;
    @FXML
    private TextField answer4Q3;
    @FXML
    private TextField correctQ3;

    // Champs pour la Question 4
    @FXML
    private TextArea enonce4;
    @FXML
    private TextField answer1Q4;
    @FXML
    private TextField answer2Q4;
    @FXML
    private TextField answer3Q4;
    @FXML
    private TextField answer4Q4;
    @FXML
    private TextField correctQ4;

    // Champs pour la Question 5
    @FXML
    private TextArea enonce5;
    @FXML
    private TextField answer1Q5;
    @FXML
    private TextField answer2Q5;
    @FXML
    private TextField answer3Q5;
    @FXML
    private TextField answer4Q5;
    @FXML
    private TextField correctQ5;

    private int formationId;
    private int testId;

    private TestService testService = new TestService();
    private QuestionService questionService = new QuestionService();

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
     * Cette méthode est appelée lors du clic sur "Enregistrer le Test".
     * Elle crée 5 questions et les enregistre en BDD.
     */
    @FXML
    private void enregistrerTest() {
        try {
            // Validation de chaque question
            if (!validateQuestion(enonce1, answer1Q1, answer2Q1, answer3Q1, answer4Q1, correctQ1, 1)) return;
            if (!validateQuestion(enonce2, answer1Q2, answer2Q2, answer3Q2, answer4Q2, correctQ2, 2)) return;
            if (!validateQuestion(enonce3, answer1Q3, answer2Q3, answer3Q3, answer4Q3, correctQ3, 3)) return;
            if (!validateQuestion(enonce4, answer1Q4, answer2Q4, answer3Q4, answer4Q4, correctQ4, 4)) return;
            if (!validateQuestion(enonce5, answer1Q5, answer2Q5, answer3Q5, answer4Q5, correctQ5, 5)) return;

            // Création des questions
            Question q1 = createQuestion(enonce1, answer1Q1, answer2Q1, answer3Q1, answer4Q1, correctQ1);
            Question q2 = createQuestion(enonce2, answer1Q2, answer2Q2, answer3Q2, answer4Q2, correctQ2);
            Question q3 = createQuestion(enonce3, answer1Q3, answer2Q3, answer3Q3, answer4Q3, correctQ3);
            Question q4 = createQuestion(enonce4, answer1Q4, answer2Q4, answer3Q4, answer4Q4, correctQ4);
            Question q5 = createQuestion(enonce5, answer1Q5, answer2Q5, answer3Q5, answer4Q5, correctQ5);

            // Enregistrement des 5 questions en base
            questionService.ajouter(q1);
            questionService.ajouter(q2);
            questionService.ajouter(q3);
            questionService.ajouter(q4);
            questionService.ajouter(q5);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le test a été enregistré !");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeFormations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) enonce1.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la liste des formations.");
        }
    }
    private boolean validateQuestion(TextArea enonce, TextField answer1, TextField answer2,
                                     TextField answer3, TextField answer4, TextField correct, int questionNum) {
        // Validation de l'énoncé
        if (enonce.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "L'énoncé de la question " + questionNum + " est obligatoire.");
            enonce.requestFocus();
            return false;
        }

        if (enonce.getText().trim().length() < 5) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "L'énoncé de la question " + questionNum + " est trop court.");
            enonce.requestFocus();
            return false;
        }

        // Validation des réponses
        if (answer1.getText().trim().isEmpty() ||
                answer2.getText().trim().isEmpty() ||
                answer3.getText().trim().isEmpty() ||
                answer4.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Toutes les réponses de la question " + questionNum + " sont obligatoires.");
            return false;
        }

        // Validation de la réponse correcte
        try {
            int correctAnswer = Integer.parseInt(correct.getText().trim());
            if (correctAnswer < 1 || correctAnswer > 4) {
                showAlert(Alert.AlertType.WARNING, "Erreur",
                        "La réponse correcte de la question " + questionNum + " doit être un chiffre entre 1 et 4.");
                correct.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Erreur",
                    "La réponse correcte de la question " + questionNum + " doit être un chiffre entre 1 et 4.");
            correct.requestFocus();
            return false;
        }

        return true;
    }

    private Question createQuestion(TextArea enonce, TextField answer1, TextField answer2,
                                    TextField answer3, TextField answer4, TextField correct) {
        return new Question(
                testId,
                enonce.getText().trim(),
                answer1.getText().trim(),
                answer2.getText().trim(),
                answer3.getText().trim(),
                answer4.getText().trim(),
                Integer.parseInt(correct.getText().trim())
        );
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
}
