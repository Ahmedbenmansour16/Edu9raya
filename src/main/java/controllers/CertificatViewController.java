package controllers;

import entities.Certificat;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

public class CertificatViewController {
    @FXML private Label studentNameLabel;
    @FXML private Label courseNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label scoreLabel;
    @FXML private Button downloadBtn;
    @FXML private Button returnBtn;

    private Certificat certificat;
    private String formationNom;

    public void initData(Certificat certificat, String formationNom) {
        this.certificat = certificat;
        this.formationNom = formationNom;

        User user = SessionController.getInstance().getCurrentUser();
        studentNameLabel.setText(user.getNom() + " " + user.getPrenom());
        courseNameLabel.setText(formationNom);
        dateLabel.setText("Date d'obtention : " + certificat.getDateObtention().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        scoreLabel.setText("Score : " + String.format("%.0f%%", certificat.getScore()));
    }

    @FXML
    private void handleReturn() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeFormationsFront.fxml"));
            Stage stage = (Stage) returnBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à la liste des formations.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDownload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le certificat");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("certificat_" +
                formationNom.replaceAll("\\s+", "_") + "_" +
                System.currentTimeMillis() + ".pdf");

        Stage stage = (Stage) downloadBtn.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Afficher une indication de chargement
            downloadBtn.setDisable(true);
            downloadBtn.setText("Génération en cours...");

            // Générer le PDF de manière asynchrone
            CompletableFuture.runAsync(() -> {
                try {
                    generatePDF(file);

                    // Mettre à jour l'UI sur le thread JavaFX
                    javafx.application.Platform.runLater(() -> {
                        showAlert(Alert.AlertType.INFORMATION, "Succès",
                                "Le certificat a été téléchargé avec succès.");
                        downloadBtn.setDisable(false);
                        downloadBtn.setText("Télécharger en PDF");
                    });
                } catch (Exception e) {
                    // Mettre à jour l'UI sur le thread JavaFX
                    javafx.application.Platform.runLater(() -> {
                        showAlert(Alert.AlertType.ERROR, "Erreur",
                                "Erreur lors de la génération du PDF: " + e.getMessage());
                        downloadBtn.setDisable(false);
                        downloadBtn.setText("Télécharger en PDF");
                    });
                    e.printStackTrace();
                }
            });
        }
    }

    private void generatePDF(File file) throws IOException {
        try (PDDocument document = new PDDocument()) {
            // Créer une page A4
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Calculer les dimensions de la page
            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();
            float margin = 50;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Ajouter un fond
                contentStream.setNonStrokingColor(245, 247, 250);
                contentStream.addRect(0, 0, pageWidth, pageHeight);
                contentStream.fill();

                // Dessiner un cadre
                contentStream.setStrokingColor(38, 70, 83); // #264653
                contentStream.setLineWidth(4);
                contentStream.addRect(margin, margin, pageWidth - 2 * margin, pageHeight - 2 * margin);
                contentStream.stroke();

                // Fond blanc pour le contenu principal
                contentStream.setNonStrokingColor(255, 255, 255);
                contentStream.addRect(margin + 5, margin + 5, pageWidth - 2 * margin - 10, pageHeight - 2 * margin - 10);
                contentStream.fill();

                // Essayer de charger le logo
                try {
                    PDImageXObject logo = PDImageXObject.createFromFile(
                            getClass().getResource("/images/certificate_badge.png").getPath(), document);
                    float logoWidth = 100;
                    float logoHeight = 100;
                    contentStream.drawImage(logo,
                            (pageWidth - logoWidth) / 2,
                            pageHeight - margin - 120,
                            logoWidth, logoHeight);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement du logo: " + e.getMessage());
                    // Continuer sans logo si on ne peut pas le charger
                }

                // Titre du certificat
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_BOLD, 28);
                contentStream.setNonStrokingColor(38, 70, 83); // #264653
                float titleWidth = PDType1Font.TIMES_BOLD.getStringWidth("Certificat de Réussite") / 1000 * 28;
                contentStream.newLineAtOffset((pageWidth - titleWidth) / 2, pageHeight - margin - 170);
                contentStream.showText("Certificat de Réussite");
                contentStream.endText();

                // Félicitations
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 22);
                contentStream.setNonStrokingColor(42, 157, 143); // #2a9d8f
                float congratsWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Félicitations !") / 1000 * 22;
                contentStream.newLineAtOffset((pageWidth - congratsWidth) / 2, pageHeight - margin - 220);
                contentStream.showText("Félicitations !");
                contentStream.endText();

                // Formation
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 16);
                contentStream.setNonStrokingColor(100, 116, 139); // #64748b
                contentStream.newLineAtOffset(pageWidth / 2 - 100, pageHeight - margin - 270);
                contentStream.showText("Formation validée :");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.setNonStrokingColor(30, 41, 59); // #1e293b
                contentStream.newLineAtOffset(pageWidth / 2 - 100, pageHeight - margin - 300);
                contentStream.showText(formationNom);
                contentStream.endText();

                // Score
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 26);
                contentStream.setNonStrokingColor(37, 99, 235); // #2563eb
                String scoreText = String.format("Score : %.0f%%", certificat.getScore());
                float scoreWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(scoreText) / 1000 * 26;
                contentStream.newLineAtOffset((pageWidth - scoreWidth) / 2, pageHeight - margin - 350);
                contentStream.showText(scoreText);
                contentStream.endText();

                // Rectangle pour informations de l'étudiant
                contentStream.setNonStrokingColor(248, 250, 252); // #f8fafc
                contentStream.addRect(pageWidth / 2 - 150, pageHeight - margin - 470, 300, 100);
                contentStream.fill();

                contentStream.setStrokingColor(226, 232, 240); // #e2e8f0
                contentStream.setLineWidth(1);
                contentStream.addRect(pageWidth / 2 - 150, pageHeight - margin - 470, 300, 100);
                contentStream.stroke();

                // Informations de l'étudiant
                User user = SessionController.getInstance().getCurrentUser();

                // Attribué à
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 16);
                contentStream.setNonStrokingColor(100, 116, 139); // #64748b
                contentStream.newLineAtOffset(pageWidth / 2 - 130, pageHeight - margin - 400);
                contentStream.showText("Attribué à :");
                contentStream.endText();

                // Nom de l'étudiant
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.setNonStrokingColor(30, 41, 59); // #1e293b
                contentStream.newLineAtOffset(pageWidth / 2 - 130, pageHeight - margin - 430);
                contentStream.showText(user.getNom() + " " + user.getPrenom());
                contentStream.endText();

                // Date
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 14);
                contentStream.setNonStrokingColor(100, 116, 139); // #64748b
                contentStream.newLineAtOffset(pageWidth / 2 - 130, pageHeight - margin - 460);
                contentStream.showText("Date d'obtention : " + certificat.getDateObtention().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                contentStream.endText();
            }

            document.save(file);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}