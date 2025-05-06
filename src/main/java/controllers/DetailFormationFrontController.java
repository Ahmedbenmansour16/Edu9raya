package controllers;

import entities.Contenu;
import entities.Formation;
import entities.Niveau;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ContenuService;
import services.NiveauService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DetailFormationFrontController {

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
    private ImageView formationImage;

    @FXML
    private Label formationTitle;

    @FXML
    private Label formationCategory;

    @FXML
    private Label formationDate;

    @FXML
    private Label formationDescription;

    @FXML
    private VBox niveauxContainer;

    @FXML
    private Button testFinalBtn;

    private Formation formation;
    private NiveauService niveauService = new NiveauService();
    private ContenuService contenuService = new ContenuService();

    // Format de date pour l'affichage
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        setupNavigation();
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
        afficherDetailsFormation();
        chargerNiveaux();
    }

    private void setupNavigation() {
        backBtn.setOnAction(event -> retournerAuxFormations());
        homeBtn.setOnAction(event -> naviguerVersHome());
        formationBtn.setOnAction(event -> retournerAuxFormations());
        reclamationsBtn.setOnAction(event -> naviguerVersReclamations());
        profileBtn.setOnAction(event -> naviguerVersProfile());
        testFinalBtn.setOnAction(event -> ouvrirTestFinal());
    }

    private void afficherDetailsFormation() {
        formationTitle.setText(formation.getNom());
        formationCategory.setText("Catégorie: " + formation.getCategorie().getNom());
        formationDate.setText("Créé le " + formation.getDateCreation().format(formatter));
        formationDescription.setText(formation.getDescription());

        // Charger l'image
        try {
            String imagePath = "file:src/main/resources/images/" + formation.getImage();
            Image image = new Image(imagePath, 400, 250, true, true);
            formationImage.setImage(image);
        } catch (Exception e) {
            // Image par défaut si l'image n'est pas trouvée
            Image defaultImage = new Image("file:src/main/resources/images/default.png", 400, 250, true, true);
            formationImage.setImage(defaultImage);
            e.printStackTrace();
        }
    }

    private void chargerNiveaux() {
        try {
            List<Niveau> niveaux = niveauService.getNiveauxByFormationId(formation.getId());
            niveauxContainer.getChildren().clear();

            // Si la formation n'a pas encore de niveaux, afficher un message
            if (niveaux.isEmpty()) {
                Label infoLabel = new Label("Cette formation n'a pas encore de niveaux.");
                infoLabel.getStyleClass().add("info-message");
                niveauxContainer.getChildren().add(infoLabel);
                return;
            }

            // Debug
            System.out.println("Niveaux trouvés: " + niveaux.size());
            for (Niveau n : niveaux) {
                System.out.println("Niveau ID: " + n.getId() + " - Ordre: " + n.getOrdre());
            }

            // Créer un élément pour chaque niveau
            for (int i = 0; i < 5; i++) {
                final int niveauOrdre = i + 1;

                // Chercher si ce niveau existe dans la liste
                Niveau niveauExistant = niveaux.stream()
                        .filter(niveau -> niveau.getOrdre() == niveauOrdre)
                        .findFirst()
                        .orElse(null);

                if (niveauExistant != null) {
                    System.out.println("Création box pour niveau existant: " + niveauOrdre);
                } else {
                    System.out.println("Création box pour niveau non existant: " + niveauOrdre);
                }

                VBox niveauBox = createNiveauBox(niveauOrdre, niveauExistant != null);
                niveauxContainer.getChildren().add(niveauBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des niveaux: " + e.getMessage());
        }
    }
    private VBox createNiveauBox(int numeroNiveau, boolean niveauExiste) {
        Label niveauLabel = new Label("Niveau " + numeroNiveau);
        niveauLabel.getStyleClass().add("niveau-title");

        Button commencerBtn = new Button("Cliquez pour commencer");
        commencerBtn.getStyleClass().add("niveau-button");

        if (!niveauExiste) {
            commencerBtn.setDisable(true);
            commencerBtn.setText("Niveau non disponible");
        }

        // Événement pour ouvrir le niveau
        final int niveauOrdre = numeroNiveau;
        commencerBtn.setOnAction(event -> ouvrirNiveau(niveauOrdre));

        HBox buttonBox = new HBox(commencerBtn);
        buttonBox.getStyleClass().add("niveau-button-container");

        VBox niveauBox = new VBox(10, niveauLabel, buttonBox);
        niveauBox.getStyleClass().add("niveau-box");

        return niveauBox;
    }

    private void ouvrirNiveau(int numeroNiveau) {
        try {
            System.out.println("Ouverture du niveau " + numeroNiveau);

            // D'abord, obtenir l'ID du niveau à partir de l'ordre et de l'ID de formation
            int niveauId = niveauService.getNiveauId(formation.getId(), numeroNiveau);
            System.out.println("ID du niveau " + numeroNiveau + ": " + niveauId);

            if (niveauId == -1) {
                showAlert(Alert.AlertType.WARNING, "Niveau non disponible",
                        "Ce niveau n'est pas encore disponible pour cette formation.");
                return;
            }

            // Charger la vue du niveau
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NiveauDetailFront.fxml"));
            Parent root = loader.load();

            // Configurer le contrôleur
            NiveauDetailFrontController controller = loader.getController();
            // L'ordre est important ici
            controller.setFormation(formation);
            controller.setNiveauId(niveauId);
            controller.setNumeroNiveau(numeroNiveau);

            // Afficher la vue
            Stage stage = (Stage) niveauxContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de l'ouverture du niveau: " + e.getMessage());
        }
    }
    private void ouvrirTestFinal() {
        // Vérifier si tous les niveaux ont été complétés
        // Dans une application réelle, vous auriez une logique pour vérifier la progression de l'utilisateur

        try {
            List<Niveau> niveaux = niveauService.getNiveauxByFormationId(formation.getId());

            if (niveaux.size() < 5) {
                showAlert(Alert.AlertType.WARNING, "Test Final",
                        "Vous devez compléter tous les niveaux avant de passer le test final.");
                return;
            }

            // Si tous les niveaux sont complétés, ouvrir le test final
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestFinalFront.fxml"));
            Parent root = loader.load();

            // Configurer le contrôleur du test final
            TestFinalFrontController controller = loader.getController();
            controller.setFormation(formation);

            // Afficher la vue
            Stage stage = (Stage) testFinalBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de l'ouverture du test final: " + e.getMessage());
        }
    }

    private void retournerAuxFormations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeFormationsFront.fxml"));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à la liste des formations.");
        }
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
            Parent root = FXMLLoader.load(getClass().getResource("/MesReclamations.fxml"));
            Stage stage = (Stage) reclamationsBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de naviguer vers vos réclamations.");
        }
    }
    public void naviguerVersNiveau(int numeroNiveau) {
        try {
            System.out.println("Tentative de navigation vers niveau " + numeroNiveau);

            // Récupérer l'ID du niveau correspondant à ce numéro pour cette formation
            int niveauId = niveauService.getNiveauId(formation.getId(), numeroNiveau);
            System.out.println("ID du niveau " + numeroNiveau + ": " + niveauId);

            if (niveauId == -1) {
                showAlert(Alert.AlertType.WARNING, "Niveau non disponible",
                        "Le niveau " + numeroNiveau + " n'est pas encore disponible pour cette formation.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NiveauDetailFront.fxml"));
            Parent root = loader.load();

            NiveauDetailFrontController controller = loader.getController();
            controller.setFormation(formation);
            controller.setNumeroNiveau(numeroNiveau);
            controller.setNiveauId(niveauId); // Important : cet ordre peut être crucial

            Stage stage = (Stage) niveauxContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la navigation vers le niveau " + numeroNiveau);
        }
    }

    private Stage getStage() {
        return (Stage) niveauxContainer.getScene().getWindow();
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}