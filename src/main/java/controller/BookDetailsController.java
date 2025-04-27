package controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;
import utils.DriveDownloader;

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Controller for BookDetails.fxml.
 */
public class BookDetailsController {

    @FXML private Label idBookLabel;
    @FXML private Label nomBookLabel;
    @FXML private Label catBookLabel;
    @FXML private Label dispoBookLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label pdfFileLabel;
    @FXML private ImageView bookImageView;
    @FXML private ImageView qrCodeImageView;

    private Book book;

    /**
     * Initialise la vue avec les données du Book,
     * charge la couverture et génère le QR code.
     */
    public void setBook(Book book) {
        this.book = book;

        // Texte
        idBookLabel.setText(book.getIdBook());
        nomBookLabel.setText(book.getNomBook());
        catBookLabel.setText(String.valueOf(book.getCatBook()));
        dispoBookLabel.setText(book.getDispoBook());
        descriptionLabel.setText(book.getDescription());
        pdfFileLabel.setText(book.getPdfFile());

        // Image de couverture
        if (book.getPicture() != null && !book.getPicture().isEmpty()) {
            File imgFile = new File(book.getPicture());
            if (imgFile.exists()) {
                try (InputStream in = new FileInputStream(imgFile)) {
                    bookImageView.setImage(new Image(in));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Génération du QR code pour Drive
        if (book.getFileId() != null && !book.getFileId().isEmpty()) {
            String driveLink = "https://drive.google.com/uc?export=download&id=" + book.getFileId();
            try {
                qrCodeImageView.setImage(generateQRCode(driveLink, 150, 150));
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Bouton « Télécharger PDF » :
     * ouvre localement si existant, sinon télécharge depuis Drive.
     */
    @FXML
    private void handleDownloadPdf(ActionEvent event) {
        if (book == null || book.getFileId() == null) return;

        File localPdf = new File(book.getPdfFile());
        if (localPdf.exists()) {
            openWithDesktop(localPdf);
        } else {
            // Téléchargement depuis Drive
            String tmp = System.getProperty("java.io.tmpdir")
                    + File.separator + book.getFileId() + ".pdf";
            DriveDownloader.downloadPDF(book.getFileId(), "YOUR_API_KEY", tmp);
            openWithDesktop(new File(tmp));
        }
    }

    /**
     * Bouton « Lire le PDF » :
     * ouvre une fenêtre intégrée (PdfView.fxml) avec WebView.
     */
    @FXML
    private void handleReadPdf(ActionEvent event) {
        if (book == null || book.getPdfFile() == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PdfView.fxml"));
            Parent root = loader.load();
            PdfViewController pdfCtrl = loader.getController();
            pdfCtrl.loadPdf(book.getPdfFile());

            Stage stage = new Stage();
            stage.setTitle("Lecture PDF – " + book.getNomBook());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> {
                try { pdfCtrl.close(); }
                catch (IOException ex) { ex.printStackTrace(); }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Ouvre un fichier avec l’application par défaut du système. */
    private void openWithDesktop(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Génère un QR Code en Image JavaFX via ZXing.
     */
    private Image generateQRCode(String text, int width, int height)
            throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);

        BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bImg.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return SwingFXUtils.toFXImage(bImg, null);
    }
}
