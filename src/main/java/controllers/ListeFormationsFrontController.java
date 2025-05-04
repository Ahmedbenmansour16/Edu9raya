package controllers;

import entities.Categorie;
import entities.Formation;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import services.CategorieService;
import services.FormationService;
import entities.ClassementEtudiant;
import entities.StatistiqueTest;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import services.StatistiqueService;
import java.text.DecimalFormat;
import javafx.util.StringConverter;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListeFormationsFrontController {
    @FXML
    private Label lblTotalCertificats;

    @FXML
    private Label lblScoreMoyen;

    @FXML
    private Label lblMeilleurScore;

    @FXML
    private AnchorPane topStudentsContainer;

    @FXML
    private AnchorPane classementContainer;

    @FXML
    private ComboBox<StatistiqueTest> comboTests;

    @FXML
    private Button btnAfficherClassement;

    private StatistiqueService statistiqueService = new StatistiqueService();
    private DecimalFormat df = new DecimalFormat("#0.00");

    @FXML
    private VBox categoriesContainer;

    @FXML
    private Button homeBtn;

    @FXML
    private Button formationBtn;

    @FXML
    private Button reclamationsBtn;

    @FXML
    private Button profileBtn;

    @FXML
    private Label userNameLabel;

    @FXML
    private Button logoutBtn;

    private FormationService formationService = new FormationService();
    private CategorieService categorieService = new CategorieService();

    // Format de date pour l'affichage
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

        chargerFormations();
        setupNavigation();

        // Configurer le bouton de déconnexion
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
            Stage stage = (Stage) categoriesContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers la page de connexion.");
        }
    }

    private void setupNavigation() {
        homeBtn.setOnAction(event -> naviguerVersHome());
        reclamationsBtn.setOnAction(event -> naviguerVersReclamations());
        profileBtn.setOnAction(event -> naviguerVersProfile());
    }

    private void naviguerVersHome() {
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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Reclamation.fxml"));
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

    private void chargerFormations() {
        try {
            List<Formation> formations = formationService.recuperer();
            // Grouper les formations par catégorie
            Map<String, List<Formation>> formationsParNomCategorie = formations.stream()
                    .collect(Collectors.groupingBy(f -> f.getCategorie().getNom()));

            // Vider le conteneur pour éviter les doublons
            categoriesContainer.getChildren().clear();

            // Créer une section pour chaque catégorie
            for (Map.Entry<String, List<Formation>> entry : formationsParNomCategorie.entrySet()) {
                String nomCategorie = entry.getKey();
                List<Formation> formationsCategorie = entry.getValue();

                // Créer un label pour le nom de la catégorie
                Label categorieLabel = new Label(nomCategorie);
                categorieLabel.getStyleClass().add("category-title");

                // Créer un FlowPane pour les cartes de formation
                FlowPane formationsPane = new FlowPane();
                formationsPane.setHgap(25);
                formationsPane.setVgap(25);
                formationsPane.getStyleClass().add("formations-container");

                // Ajouter chaque formation à la catégorie
                for (Formation formation : formationsCategorie) {
                    VBox formationCard = creerCarteFormation(formation);
                    formationsPane.getChildren().add(formationCard);
                }

                // Ajouter la catégorie et ses formations au conteneur principal
                VBox categorieSection = new VBox(15, categorieLabel, formationsPane);
                categorieSection.getStyleClass().add("category-section");
                categoriesContainer.getChildren().add(categorieSection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des formations.");
        }
    }

    private VBox creerCarteFormation(Formation formation) {
        // Création de l'image
        ImageView imageView = new ImageView();
        try {
            // Chemin vers les images
            String imagePath = "file:src/main/resources/images/" + formation.getImage();
            Image image = new Image(imagePath, 280, 180, true, true);
            imageView.setImage(image);
        } catch (Exception e) {
            // Image par défaut si l'image n'est pas trouvée
            Image defaultImage = new Image("file:src/main/resources/images/default.png", 280, 180, true, true);
            imageView.setImage(defaultImage);
        }
        imageView.getStyleClass().add("formation-image");

        // Titre de la formation
        Label titleLabel = new Label(formation.getNom());
        titleLabel.getStyleClass().add("formation-title");

        // Description de la formation (limité à 100 caractères avec "...")
        String description = formation.getDescription();
        if (description.length() > 100) {
            description = description.substring(0, 97) + "...";
        }
        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("formation-description");

        // Date de création
        Label dateLabel = new Label("Créé le " + formation.getDateCreation().format(formatter));
        dateLabel.getStyleClass().add("formation-date");

        // Bouton pour voir la formation
        Button voirButton = new Button("Voir la formation");
        voirButton.getStyleClass().add("voir-formation-btn");
        voirButton.setOnAction(event -> ouvrirDetailFormation(formation));

        // Assembler tous les éléments dans une carte
        VBox formationCard = new VBox(10, imageView, titleLabel, descriptionLabel, dateLabel, voirButton);
        formationCard.getStyleClass().add("formation-card");

        return formationCard;
    }

    private void ouvrirDetailFormation(Formation formation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailFormationFront.fxml"));
            Parent root = loader.load();

            DetailFormationFrontController controller = loader.getController();
            controller.setFormation(formation);

            Stage stage = (Stage) categoriesContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir les détails de la formation.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}