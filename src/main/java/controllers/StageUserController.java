package controllers;

import entities.Resume;
import entities.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import services.CategorieStageService;
import services.ResumeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class StageUserController {

    private UserConsulterStageController mainController;
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(UserConsulterStageController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private Label entreprise;

    @FXML
    private Label Duree;

    @FXML
    private Label dateDeb;

    @FXML
    private Label titre;

    @FXML
    private Label categore;

    @FXML
    private Label desc;

    @FXML
    private Label lieu;

    CategorieStageService Cs = new CategorieStageService();
    ResumeService Rs = new ResumeService();

    @FXML
    void uploadCV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier pdf", "*.pdf"));
        File CvFile = fileChooser.showOpenDialog(null);

        if (CvFile != null) {
            try {
                byte[] fileContent = Files.readAllBytes(CvFile.toPath());
                Resume r = new Resume(CvFile.getName(), getStage().getId());
                Rs.ajouterPdf(r, fileContent);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Le CV a été téléchargé avec succès !");
                successAlert.showAndWait();

            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur de fichier");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Une erreur s'est produite lors de la lecture du fichier : " + e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();

            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur de base de données");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Une erreur s'est produite lors de l'enregistrement du CV dans la base de données : " + e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        } else {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Aucun fichier sélectionné");
            warningAlert.setHeaderText(null);
            warningAlert.setContentText("Veuillez sélectionner un fichier PDF à télécharger.");
            warningAlert.showAndWait();
        }
    }
    void setData(Stage s) throws Exception{
        setStage(s);
        titre.setText(s.getTitre());
        desc.setText(s.getDescription());
        categore.setText(Cs.recupererParId(s.getCategorieId()).getNom());
        entreprise.setText(s.getEntreprise());
        lieu.setText(s.getLieu());
        dateDeb.setText(s.getDateDebut().toString());
        Duree.setText(String.valueOf(s.getDuree()));
    }

}
