package controllers;

import entities.Certificat;
import entities.Formation;
import entities.Question;
import entities.User;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.CertificatService;
import services.QuestionService;
import services.TestService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.MyDatabase;

public class TestFinalFrontController {

    @FXML
    private Button backBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private Button formationBtn;

    @FXML
    private Button reclamationsBtn;

    @FXML
    private Button profileBtn;

    @FXML
    private Label formationTitleLabel;

    @FXML
    private VBox questionsContainer;

    @FXML
    private Button submitBtn;

    @FXML
    private Label timerLabel;

    private Formation formation;
    private List<Question> questions = new ArrayList<>();
    private Map<Integer, ToggleGroup> questionGroups = new HashMap<>();
    private Map<Integer, Integer> userAnswers = new HashMap<>();

    private TestService testService = new TestService();
    private Connection cnx = MyDatabase.getInstance().getCnx();

    // Variables pour le chronomètre
    private Timeline timeline;
    private int timeSeconds = 15; // Durée de 15 secondes

    @FXML
    private void initialize() {
        setupNavigation();
        submitBtn.setOnAction(event -> soumettreReponses());
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
        formationTitleLabel.setText("Test Final: " + formation.getNom());
        chargerQuestions();
        demarrerChronometre();
    }

    private void demarrerChronometre() {
        // Initialiser le texte du chronomètre
        timerLabel.setText(timeSeconds + " sec");

        // Créer le timeline pour le chronomètre
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds--;

            // Mettre à jour le texte du chronomètre
            Platform.runLater(() -> timerLabel.setText(timeSeconds + " sec"));

            // Vérifier si le temps est écoulé
            if (timeSeconds <= 0) {
                timeline.stop();
                // Soumettre automatiquement le test
                Platform.runLater(() -> soumettreReponses());
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void setupNavigation() {
        backBtn.setOnAction(event -> retournerAFormation());
        homeBtn.setOnAction(event -> naviguerVersHome());
        formationBtn.setOnAction(event -> retournerAuxFormations());
        reclamationsBtn.setOnAction(event -> naviguerVersReclamations());
        profileBtn.setOnAction(event -> naviguerVersProfile());
    }

    private void chargerQuestions() {
        try {
            // Récupérer l'ID du test pour cette formation
            int testId = getTestIdForFormation(formation.getId());
            if (testId == -1) {
                showAlert(Alert.AlertType.WARNING, "Test non disponible",
                        "Le test final n'est pas encore disponible pour cette formation.");
                return;
            }

            // Récupérer les questions du test
            questions = getQuestionsForTest(testId);
            if (questions.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Questions non disponibles",
                        "Aucune question n'est disponible pour ce test.");
                return;
            }

            // Afficher les questions
            afficherQuestions();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des questions: " + e.getMessage());
        }
    }

    private int getTestIdForFormation(int formationId) throws SQLException {
        String sql = "SELECT id FROM test WHERE formation_id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, formationId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
        return -1;
    }

    private List<Question> getQuestionsForTest(int testId) throws SQLException {
        List<Question> result = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE test_id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, testId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Question q = new Question(
                    rs.getInt("id"),
                    rs.getInt("test_id"),
                    rs.getString("enonce"),
                    rs.getString("answer1"),
                    rs.getString("answer2"),
                    rs.getString("answer3"),
                    rs.getString("answer4"),
                    rs.getInt("correct_answer")
            );
            result.add(q);
        }
        return result;
    }

    private void afficherQuestions() {
        questionsContainer.getChildren().clear();

        int questionNum = 1;
        for (Question question : questions) {
            VBox questionBox = createQuestionBox(question, questionNum);
            questionsContainer.getChildren().add(questionBox);
            questionNum++;
        }
    }

    private VBox createQuestionBox(Question question, int questionNum) {
        VBox questionBox = new VBox(10);
        questionBox.getStyleClass().add("question-box");

        Label questionLabel = new Label("Question " + questionNum + ": " + question.getEnonce());
        questionLabel.getStyleClass().add("question-label");
        questionLabel.setWrapText(true);

        ToggleGroup group = new ToggleGroup();
        questionGroups.put(question.getId(), group);

        RadioButton rb1 = new RadioButton(question.getAnswer1());
        rb1.setToggleGroup(group);
        rb1.setUserData(1);
        rb1.getStyleClass().add("answer-option");
        rb1.setWrapText(true);

        RadioButton rb2 = new RadioButton(question.getAnswer2());
        rb2.setToggleGroup(group);
        rb2.setUserData(2);
        rb2.getStyleClass().add("answer-option");
        rb2.setWrapText(true);

        RadioButton rb3 = new RadioButton(question.getAnswer3());
        rb3.setToggleGroup(group);
        rb3.setUserData(3);
        rb3.getStyleClass().add("answer-option");
        rb3.setWrapText(true);

        RadioButton rb4 = new RadioButton(question.getAnswer4());
        rb4.setToggleGroup(group);
        rb4.setUserData(4);
        rb4.getStyleClass().add("answer-option");
        rb4.setWrapText(true);

        // Listener pour enregistrer la réponse de l'utilisateur
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userAnswers.put(question.getId(), (Integer) newValue.getUserData());
            }
        });

        questionBox.getChildren().addAll(questionLabel, rb1, rb2, rb3, rb4);
        return questionBox;
    }

    private void soumettreReponses() {
        // Arrêter le chronomètre
        if (timeline != null) {
            timeline.stop();
        }

        // Calculer le score même si toutes les questions n'ont pas été répondues
        int correctCount = 0;
        int totalQuestions = questions.size();

        for (Question question : questions) {
            Integer userAnswer = userAnswers.get(question.getId());
            if (userAnswer != null && userAnswer == question.getCorrectAnswer()) {
                correctCount++;
            }
        }

        double scorePercentage = (double) correctCount / totalQuestions * 100;
        boolean passed = scorePercentage >= 50; // Considère comme réussi si >= 50%

        // Afficher le résultat
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Résultat du test");
        alert.setHeaderText(passed ? "Félicitations !" : "Test non réussi");

        String message = String.format("Vous avez obtenu %d/%d réponses correctes (%.1f%%).\n\n",
                correctCount, totalQuestions, scorePercentage);

        if (passed) {
            message += "Vous avez réussi le test final ! Vous pouvez maintenant accéder à d'autres formations.";
        } else {
            message += "Vous n'avez pas obtenu le score minimum requis (50%). Vous pouvez revoir le contenu des niveaux et réessayer.";
        }

        alert.setContentText(message);
        alert.showAndWait();

        // Enregistrer le résultat (à implémenter si nécessaire)
        // saveTestResult(formation.getId(), passed, scorePercentage);

        if (passed) {
            try {
                genererCertificat(scorePercentage);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
            }
        } else {
            retournerAuxFormations();
        }
    }

    private void retournerAFormation() {
        // Arrêter le chronomètre avant de quitter la page
        if (timeline != null) {
            timeline.stop();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailFormationFront.fxml"));
            Parent root = loader.load();

            DetailFormationFrontController controller = loader.getController();
            controller.setFormation(formation);

            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner aux détails de la formation.");
        }
    }

    private void retournerAuxFormations() {
        // Arrêter le chronomètre avant de quitter la page
        if (timeline != null) {
            timeline.stop();
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeFormationsFront.fxml"));
            Stage stage = (Stage) formationBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à la liste des formations.");
        }
    }

    private void naviguerVersHome() {
        // Arrêter le chronomètre avant de quitter la page
        if (timeline != null) {
            timeline.stop();
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page d'accueil.");
        }
    }

    private void naviguerVersReclamations() {
        // Arrêter le chronomètre avant de quitter la page
        if (timeline != null) {
            timeline.stop();
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MesReclamations.fxml"));
            Stage stage = (Stage) reclamationsBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers vos réclamations.");
        }
    }

    private void naviguerVersProfile() {
        // Arrêter le chronomètre avant de quitter la page
        if (timeline != null) {
            timeline.stop();
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
            Stage stage = (Stage) profileBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers votre profil.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void genererCertificat(double score) {
        try {
            // 1. Vérifier qu'on a un utilisateur connecté
            User user = SessionController.getInstance().getCurrentUser();
            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun utilisateur connecté");
                return;
            }

            // 2. Récupérer l'ID du test associé à la formation
            int testId = getTestIdForFormation(formation.getId());
            if (testId == -1) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Aucun test trouvé pour cette formation. Contactez l'administrateur.");
                return;
            }

            // 3. Créer le certificat
            Certificat certificat = new Certificat(
                    user.getId(),
                    testId,
                    LocalDate.now(),
                    score
            );

            // 4. Sauvegarder en base
            CertificatService certificatService = new CertificatService();
            certificatService.ajouterCertificat(certificat);

            // 5. Afficher la vue du certificat
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CertificateView.fxml"));
                Parent root = loader.load();

                CertificatViewController controller = loader.getController();
                controller.initData(certificat, formation.getNom());

                // 6. Fermer la fenêtre actuelle et afficher le certificat
                Stage currentStage = (Stage) submitBtn.getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.setTitle("Certificat de Réussite");

                // Fermer la fenêtre actuelle après un léger délai
                PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                delay.setOnFinished(e -> currentStage.close());
                delay.play();

                newStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Impossible d'afficher le certificat : " + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Erreur lors de la génération du certificat :\n";

            if (e.getMessage().contains("foreign key constraint fails")) {
                errorMessage += "Données invalides. Contactez l'administrateur.";
            } else {
                errorMessage += e.getMessage();
            }

            showAlert(Alert.AlertType.ERROR, "Erreur", errorMessage);
        }
    }
}