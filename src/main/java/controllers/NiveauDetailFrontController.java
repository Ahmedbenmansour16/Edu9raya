package controllers;

import entities.Contenu;
import entities.Formation;
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
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import services.ContenuService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class NiveauDetailFrontController {

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
    private Label niveauTitleLabel;

    @FXML
    private VBox contenuContainer;

    @FXML
    private Button previousNiveauBtn;

    @FXML
    private Button nextNiveauBtn;

    private Formation formation;
    private int niveauId;
    private int numeroNiveau;
    private ContenuService contenuService = new ContenuService();

    @FXML
    private void initialize() {
        setupNavigation();
    }


    public void setFormation(Formation formation) {
        this.formation = formation;
    }



    private void configureNavigationButtons() {
        // Configurer le bouton "Précédent"
        previousNiveauBtn.setDisable(numeroNiveau <= 1);
        previousNiveauBtn.setOnAction(event -> naviguerVersNiveau(numeroNiveau - 1));

        // Configurer le bouton "Suivant"
        nextNiveauBtn.setDisable(numeroNiveau >= 5);
        nextNiveauBtn.setOnAction(event -> naviguerVersNiveau(numeroNiveau + 1));
    }

    private void naviguerVersNiveau(int numeroNiveau) {
        try {
            // Récupérer l'ID du niveau cible
            services.NiveauService niveauService = new services.NiveauService();
            int targetNiveauId = niveauService.getNiveauId(formation.getId(), numeroNiveau);

            if (targetNiveauId == -1) {
                showAlert(Alert.AlertType.WARNING, "Niveau non disponible",
                        "Le niveau " + numeroNiveau + " n'est pas encore disponible pour cette formation.");
                return;
            }

            // Charger la vue du niveau
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NiveauDetailFront.fxml"));
            Parent root = loader.load();

            // Configurer le contrôleur
            NiveauDetailFrontController controller = loader.getController();
            controller.setNiveauId(targetNiveauId);
            controller.setFormation(formation);
            controller.setNumeroNiveau(numeroNiveau);

            // Afficher la vue
            Stage stage = (Stage) contenuContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la navigation vers le niveau " + numeroNiveau + ": " + e.getMessage());
        }
    }

    private void setupNavigation() {
        backBtn.setOnAction(event -> retournerAFormation());
        homeBtn.setOnAction(event -> naviguerVersHome());
        formationBtn.setOnAction(event -> retournerAuxFormations());
        reclamationsBtn.setOnAction(event -> naviguerVersReclamations());
        profileBtn.setOnAction(event -> naviguerVersProfile());
    }

    private void chargerContenuNiveau() {
        try {
            // Ajoutons des logs pour mieux comprendre ce qui se passe
            System.out.println("Chargement des contenus pour le niveau ID: " + niveauId);

            List<Contenu> contenus = contenuService.getContenusParNiveau(niveauId);
            System.out.println("Nombre de contenus trouvés: " + contenus.size());

            contenuContainer.getChildren().clear();

            if (contenus.isEmpty()) {
                Label infoLabel = new Label("Ce niveau n'a pas encore de contenu.");
                infoLabel.getStyleClass().add("info-message");
                contenuContainer.getChildren().add(infoLabel);
                return;
            }

            // Afficher chaque contenu selon son type
            for (Contenu contenu : contenus) {
                System.out.println("Ajout du contenu type: " + contenu.getType() + " au niveau " + numeroNiveau);
                VBox elementBox = createContenuElement(contenu);
                contenuContainer.getChildren().add(elementBox);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement du contenu: " + e.getMessage());
        }
    }

    private VBox createContenuElement(Contenu contenu) {
        VBox elementBox = new VBox(10);
        elementBox.getStyleClass().add("contenu-element");

        Label typeLabel = new Label(contenu.getType());
        typeLabel.getStyleClass().add("contenu-type-label");
        elementBox.getChildren().add(typeLabel);

        switch (contenu.getType()) {
            case "Description":
                Label descriptionLabel = new Label(contenu.getDescription());
                descriptionLabel.setWrapText(true);
                descriptionLabel.getStyleClass().add("contenu-description");
                elementBox.getChildren().add(descriptionLabel);
                break;

            case "Image":
                try {
                    File file = new File(contenu.getFichier());
                    String imagePath = file.toURI().toString();
                    Image image = new Image(imagePath, 600, 400, true, true);
                    ImageView imageView = new ImageView(image);
                    imageView.getStyleClass().add("contenu-image");
                    elementBox.getChildren().add(imageView);
                } catch (Exception e) {
                    Label errorLabel = new Label("Impossible de charger l'image: " + e.getMessage());
                    errorLabel.getStyleClass().add("error-message");
                    elementBox.getChildren().add(errorLabel);
                }
                break;

            case "Vidéo YouTube":
                WebView webView = new WebView();
                webView.setPrefSize(600, 400);
                webView.getEngine().load("https://www.youtube.com/embed/" + contenu.getYoutubeId());
                elementBox.getChildren().add(webView);
                break;

            case "PDF":
                Label pdfLabel = new Label("Fichier PDF: " + contenu.getFichier());
                Button openPdfBtn = new Button("Ouvrir le PDF");
                openPdfBtn.setOnAction(e -> {
                    try {
                        File file = new File(contenu.getFichier());
                        java.awt.Desktop.getDesktop().open(file);
                    } catch (IOException ioEx) {
                        showAlert(Alert.AlertType.ERROR, "Erreur",
                                "Impossible d'ouvrir le PDF: " + ioEx.getMessage());
                    }
                });
                VBox pdfBox = new VBox(5, pdfLabel, openPdfBtn);
                elementBox.getChildren().add(pdfBox);
                break;
        }

        return elementBox;
    }

    private void retournerAFormation() {
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
    public void setNiveauId(int niveauId) {
        this.niveauId = niveauId;
        // Ne chargez pas encore les contenus ici
        // Le chargement sera fait après que setNumeroNiveau est appelé
    }
    public void setNumeroNiveau(int numeroNiveau) {
        this.numeroNiveau = numeroNiveau;
        niveauTitleLabel.setText("Niveau " + numeroNiveau + " : " + formation.getNom());
        configureNavigationButtons();

        // Maintenant que nous avons à la fois niveauId et numeroNiveau, chargez les contenus
        if (niveauId > 0) {
            chargerContenuNiveau();
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}