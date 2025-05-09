package controllers;

import entities.Resume;
import entities.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import services.CategorieStageService;
import services.ResumeService;
import services.StageService;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


public class StageController {

    private AdminConsulterStageController mainController;
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(AdminConsulterStageController mainController) {
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

    @FXML
    private Label Cvs;

    CategorieStageService Cs = new CategorieStageService();
    ResumeService Rs = new ResumeService();

    @FXML
    void modifStage(ActionEvent event) throws Exception{
        if (mainController != null) {
            mainController.setStageForModification(getStage());
            mainController.setLocalStage(getStage());
        }
    }

    @FXML
    void SuppStage(ActionEvent event) {
        if (mainController != null) {
            try {
                StageService stageService = new StageService();
                stageService.supprimer(getStage());
                mainController.loadStages();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void voirCV(int resumeId, VBox parent) {
        try {
            Resume resume = Rs.recupererParId(resumeId);
            if (resume != null && resume.getFileContent() != null) {
                // Add a styled label for the filename
                HBox hBox = new HBox(); // Use HBox to group the filename label and download button
                hBox.setSpacing(10);

                Label fileNameLabel = new Label("File: " + resume.getFilename());
                fileNameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-padding: 10;");
                hBox.getChildren().add(fileNameLabel);

                // Add a download button
                Button downloadButton = new Button("Telecharger");
                downloadButton.setStyle("-fx-padding: 3; -fx-font-size: 12;");
                downloadButton.setOnAction(event -> {
                    try {
                        downloadPDF(resume.getFilename(), resume.getFileContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to download the PDF.");
                        alert.showAndWait();
                    }
                });
                hBox.getChildren().add(downloadButton);

                parent.getChildren().add(hBox);

                // Render all pages of the PDF
                PDDocument document = PDDocument.load(new ByteArrayInputStream(resume.getFileContent()));
                PDFRenderer renderer = new PDFRenderer(document);

                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    BufferedImage pageImage = renderer.renderImageWithDPI(i, 300); // Render each page
                    int width = pageImage.getWidth();
                    int height = pageImage.getHeight();
                    javafx.scene.image.WritableImage fxImage = new javafx.scene.image.WritableImage(width, height);
                    fxImage.getPixelWriter().setPixels(
                            0, 0, width, height,
                            javafx.scene.image.PixelFormat.getIntArgbInstance(),
                            pageImage.getRGB(0, 0, width, height, null, 0, width),
                            0, width
                    );

                    ImageView imageView = new ImageView(fxImage);
                    imageView.setFitWidth(800);
                    imageView.setPreserveRatio(true);

                    // Add a label to indicate the page number
                    Label pageNumberLabel = new Label("Page " + (i + 1));
                    pageNumberLabel.setStyle("-fx-font-size: 14; -fx-padding: 5;");
                    parent.getChildren().addAll(pageNumberLabel, imageView);
                }

                document.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadPDF(String filename, byte[] fileContent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf"));

        // Set the default file name
        File initialFile = new File(filename);
        fileChooser.setInitialFileName(initialFile.getName());

        // Show the save dialog
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            Files.write(selectedFile.toPath(), fileContent);
            System.out.println("PDF downloaded successfully: " + selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void viewUploadedCV(ActionEvent event) {
        int stageId = getStage().getId(); // Get the Stage ID
        try {
            List<Resume> resumes = Rs.recupererResumesParStageId(stageId); // Fetch all resumes for the stage

            if (!resumes.isEmpty()) {
                // Create a VBox to hold all PDF viewers
                VBox vbox = new VBox();
                vbox.setSpacing(20); // Add spacing between resumes
                vbox.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;"); // Add padding and background color

                for (Resume resume : resumes) {
                    voirCV(resume.getId(), vbox); // Call voirCV for each resume and add it to the VBox
                }

                // Wrap the VBox in a ScrollPane
                ScrollPane scrollPane = new ScrollPane(vbox);
                scrollPane.setStyle("-fx-border-radius: 10; -fx-background-radius: 10;"); // Rounded corners

                javafx.stage.Stage pdfStage = new javafx.stage.Stage();
                pdfStage.setTitle("CV Viewer - " + getStage().getTitre());
                pdfStage.setScene(new Scene(scrollPane, 850, 600));
                pdfStage.show();
            } else {
                System.out.println("No CVs uploaded for this stage.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        int numCVs = Rs.compterCVsParStageId(s.getId());
        Cvs.setText("Nombre De CVs Envoye :" + String.valueOf(numCVs));
    }

}
