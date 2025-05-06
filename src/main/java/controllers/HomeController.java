package controllers;

import entities.ClassementEtudiant;
import entities.StatistiqueTest;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import services.StatistiqueService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class HomeController {
    @FXML
    private Label lblTotalCertificats;

    @FXML
    private Label lblScoreMoyen;

    @FXML
    private Label lblMeilleurScore;

    @FXML
    private AnchorPane topStudentsContainer;

    @FXML
    private BarChart<String, Number> topStudentsChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private VBox classementContainer;

    @FXML
    private ComboBox<StatistiqueTest> comboTests;

    @FXML
    private Button btnAfficherClassement;

    @FXML
    private Button btnVoirFormations;

    @FXML
    private Button btnMesCertificats;

    @FXML
    private Button homeBtn;

    @FXML
    private Button StageBtn;

    @FXML
    private Button formationBtn;

    @FXML
    private Button reclamationsBtn;

    @FXML
    private Button profileBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label userNameLabel;

    private StatistiqueService statistiqueService = new StatistiqueService();
    private DecimalFormat df = new DecimalFormat("#0.00");

    @FXML
    private void initialize() {
        // Afficher le nom de l'utilisateur connecté
        User currentUser = SessionController.getInstance().getCurrentUser();
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getPrenom() + " " + currentUser.getNom());
        } else {
            // Rediriger vers la page de connexion si aucun utilisateur n'est connecté
            redirectToLogin();
            return;
        }

        chargerStatistiques();
        setupNavigation();
        setupButtons();
    }

    private void setupButtons() {
        btnVoirFormations.setOnAction(event -> naviguerVersFormations());
        btnMesCertificats.setOnAction(event -> naviguerVersCertificats());
        btnAfficherClassement.setOnAction(event -> afficherClassementTest());
    }

    private void naviguerVersCertificats() {
        // À implémenter: navigation vers la page des certificats
        showAlert(Alert.AlertType.INFORMATION, "Information", "La fonctionnalité 'Mes certificats' sera disponible prochainement.");
    }

    private void setupNavigation() {
        formationBtn.setOnAction(event -> naviguerVersFormations());
        reclamationsBtn.setOnAction(event -> naviguerVersReclamations());
        profileBtn.setOnAction(event -> naviguerVersProfile());
        StageBtn.setOnAction(event -> naviguerVersStage());
        logoutBtn.setOnAction(event -> handleLogout());
    }

    private void handleLogout() {
        // Déconnexion de l'utilisateur
        SessionController.getInstance().logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page de connexion.");
        }
    }

    private void naviguerVersFormations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeFormationsFront.fxml"));
            Stage stage = (Stage) formationBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page des formations.");
        }
    }

    private void naviguerVersStage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserConsulterStage.fxml"));
            Stage stage = (Stage) formationBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page des formations.");
        }
    }

    private void naviguerVersReclamations() {
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

    private void chargerStatistiques() {
        try {
            // Récupérer les statistiques générales
            int totalCertificats = statistiqueService.getNombreTotalCertificats();
            double scoreMoyenGlobal = statistiqueService.getScoreMoyenGlobal();

            // Mettre à jour les labels
            lblTotalCertificats.setText(String.valueOf(totalCertificats));
            lblScoreMoyen.setText(df.format(scoreMoyenGlobal));

            // Récupérer tous les tests pour le ComboBox
            List<StatistiqueTest> allStats = statistiqueService.getAllStatistiques();
            comboTests.setItems(FXCollections.observableArrayList(allStats));

            // Configurer l'affichage des items dans le ComboBox
            comboTests.setConverter(new StringConverter<StatistiqueTest>() {
                @Override
                public String toString(StatistiqueTest stat) {
                    if (stat == null) {
                        return null;
                    }
                    return stat.getTestNom() + " (ID: " + stat.getTestId() + ")";
                }

                @Override
                public StatistiqueTest fromString(String string) {
                    return null;
                }
            });

            if (!allStats.isEmpty()) {
                comboTests.getSelectionModel().selectFirst();
                lblMeilleurScore.setText(df.format(allStats.stream().mapToDouble(StatistiqueTest::getScoreMaximum).max().orElse(0)));
            }

            // Afficher le top 3 des étudiants avec graphique
            List<ClassementEtudiant> topPerformers = statistiqueService.getTopPerformers(3);
            afficherTopEtudiants(topPerformers);
            creerBarChartTopEtudiants(topPerformers);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des statistiques.");
        }
    }

    private void creerBarChartTopEtudiants(List<ClassementEtudiant> topPerformers) {
        // Vider le graphique précédent
        topStudentsChart.getData().clear();

        if (topPerformers.isEmpty()) {
            return;
        }

        // Créer une série pour le graphique
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Score");

        // Ajouter les données pour chaque étudiant
        for (ClassementEtudiant etudiant : topPerformers) {
            String nom = etudiant.getEtudiant().getPrenom() + " " + etudiant.getEtudiant().getNom();
            XYChart.Data<String, Number> data = new XYChart.Data<>(nom, etudiant.getScore());
            series.getData().add(data);
        }

        topStudentsChart.getData().add(series);

        // Personnaliser l'apparence des barres
        for (int i = 0; i < series.getData().size(); i++) {
            XYChart.Data<String, Number> data = series.getData().get(i);

            // Appliquer des couleurs différentes selon le rang
            String color;
            if (i == 0) {
                color = "gold";
            } else if (i == 1) {
                color = "silver";
            } else {
                color = "#CD7F32"; // Bronze
            }

            data.getNode().setStyle("-fx-bar-fill: " + color + ";");

            // Afficher la valeur au-dessus de la barre -
            // Utilisez le StackPane (nœud de donnée) pour ajouter le label
            Label scoreLabel = new Label(df.format(data.getYValue()));
            scoreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

            // Positionnez le label au-dessus de la barre
            StackPane.setAlignment(scoreLabel, Pos.TOP_CENTER);
            StackPane.setMargin(scoreLabel, new Insets(-25, 0, 0, 0));

            // Ajoutez le label au nœud de la barre (c'est généralement un StackPane)
            Node node = data.getNode();
            if (node instanceof StackPane) {
                ((StackPane) node).getChildren().add(scoreLabel);
            }
        }
    }
    private void afficherTopEtudiants(List<ClassementEtudiant> topPerformers) {
        topStudentsContainer.getChildren().clear();

        if (topPerformers.isEmpty()) {
            Label noDataLabel = new Label("Aucune donnée disponible");
            noDataLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");
            topStudentsContainer.getChildren().add(noDataLabel);
            AnchorPane.setTopAnchor(noDataLabel, 20.0);
            AnchorPane.setLeftAnchor(noDataLabel, 20.0);
            return;
        }

        // Créer un HBox pour contenir les podiums
        HBox podiumContainer = new HBox(50);
        podiumContainer.setAlignment(Pos.CENTER);
        podiumContainer.setPadding(new Insets(10));

        // Calculer la taille du podium en fonction du nombre d'étudiants
        int size = Math.min(topPerformers.size(), 3);

        // Définir l'ordre d'affichage : 2ème (gauche), 1er (milieu), 3ème (droite)
        int[] order = {1, 0, 2};

        for (int i = 0; i < size; i++) {
            int position = order[i];
            if (position >= size) continue;

            ClassementEtudiant etudiant = topPerformers.get(position);

            // Créer un VBox pour chaque étudiant
            VBox studentBox = new VBox(10);
            studentBox.setAlignment(Pos.TOP_CENTER);
            studentBox.setMinWidth(120);
            studentBox.setMaxWidth(120);

            // Icône du trophée
            ImageView trophyIcon = new ImageView(new Image("file:src/main/resources/images/trophy.jpg"));
            trophyIcon.setFitHeight(24);
            trophyIcon.setFitWidth(24);
            trophyIcon.setPreserveRatio(true);

            // Photo de profil dans un cadre coloré
            StackPane photoContainer = new StackPane();

            // Cadre coloré
            Rectangle frame = new Rectangle(100, 100);
            String borderColor;
            String rankText;

            // Appliquer des couleurs et textes spécifiques selon le rang
            if (position == 0) {
                borderColor = "#FFD700"; // Or
                rankText = "#1";
            } else if (position == 1) {
                borderColor = "#C0C0C0"; // Argent
                rankText = "#2";
            } else {
                borderColor = "#CD7F32"; // Bronze
                rankText = "#3";
            }

            frame.setFill(Color.WHITE);
            frame.setStroke(Color.web(borderColor));
            frame.setStrokeWidth(4);
            frame.setArcHeight(10);
            frame.setArcWidth(10);

            // Photo de profil
            ImageView profilePic = new ImageView(new Image("file:src/main/resources/images/profile.jpg"));
            profilePic.setFitHeight(90);
            profilePic.setFitWidth(90);
            profilePic.setPreserveRatio(true);

            photoContainer.getChildren().addAll(frame, profilePic);

            // Nom de l'étudiant
            Label nameLabel = new Label(etudiant.getEtudiant().getPrenom() + " " + etudiant.getEtudiant().getNom());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-alignment: center; -fx-wrap-text: true;");
            nameLabel.setWrapText(true);
            nameLabel.setTextAlignment(TextAlignment.CENTER);
            nameLabel.setMaxWidth(110);

            // Score
            Label scoreLabel = new Label(df.format(etudiant.getScore()) + " pts");
            scoreLabel.setStyle("-fx-font-size: 14px;");

            // Badge de rang
            Label rankLabel = new Label(rankText);
            rankLabel.setStyle("-fx-background-color: " + borderColor + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-padding: 3 10; -fx-background-radius: 12;");

            studentBox.getChildren().addAll(trophyIcon, photoContainer, nameLabel, scoreLabel, rankLabel);
            podiumContainer.getChildren().add(studentBox);
        }

        topStudentsContainer.getChildren().add(podiumContainer);
        AnchorPane.setTopAnchor(podiumContainer, 0.0);
        AnchorPane.setLeftAnchor(podiumContainer, 0.0);
        AnchorPane.setRightAnchor(podiumContainer, 0.0);
        AnchorPane.setBottomAnchor(podiumContainer, 0.0);
    }

    private void afficherClassementTest() {
        StatistiqueTest selectedTest = comboTests.getValue();
        if (selectedTest == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un test.");
            return;
        }

        try {
            List<ClassementEtudiant> classement = statistiqueService.getClassementByTestId(selectedTest.getTestId());
            afficherClassement(classement);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement du classement.");
        }
    }

    private void afficherClassement(List<ClassementEtudiant> classement) {
        classementContainer.getChildren().clear();

        if (classement.isEmpty()) {
            Label noDataLabel = new Label("Aucune donnée disponible");
            noDataLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");
            classementContainer.getChildren().add(noDataLabel);
            return;
        }

        // Ajouter chaque étudiant dans le classement
        for (ClassementEtudiant etudiant : classement) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(10));
            item.getStyleClass().add("classement-item");

            // Rang
            Label rankLabel = new Label("#" + etudiant.getRank());
            rankLabel.setMinWidth(40);
            rankLabel.getStyleClass().add("rank-label");

            // Appliquer un style spécial aux 3 premiers
            if (etudiant.getRank() == 1) {
                rankLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: gold;");
            } else if (etudiant.getRank() == 2) {
                rankLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: silver;");
            } else if (etudiant.getRank() == 3) {
                rankLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #CD7F32;"); // Bronze
            }

            // Image de profil
            ImageView profileImg = new ImageView(new Image("file:src/main/resources/images/profile.jpg"));
            profileImg.setFitHeight(30);
            profileImg.setFitWidth(30);
            profileImg.setPreserveRatio(true);

            // Nom de l'étudiant
            Label nameLabel = new Label(etudiant.getEtudiant().getPrenom() + " " + etudiant.getEtudiant().getNom());
            HBox.setHgrow(nameLabel, Priority.ALWAYS);
            nameLabel.setStyle("-fx-font-weight: bold;");

            // Score
            Label scoreLabel = new Label(df.format(etudiant.getScore()) + " pts");
            scoreLabel.setStyle("-fx-font-weight: bold;");

            item.getChildren().addAll(rankLabel, profileImg, nameLabel, scoreLabel);

            // Ajouter un effet hover
            item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #f0f0f0;"));
            item.setOnMouseExited(e -> item.setStyle("-fx-background-color: transparent;"));

            classementContainer.getChildren().add(item);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}