package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Book;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

public class BookDetailsController {

    @FXML
    private Label idBookLabel;
    @FXML
    private Label nomBookLabel;
    @FXML
    private Label catBookLabel;
    @FXML
    private Label dispoBookLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label pdfFileLabel;
    @FXML
    private ImageView bookImageView;
    // Nouvelle ImageView pour afficher le QR Code
    @FXML
    private ImageView qrCodeImageView;

    private Book book;

    /**
     * Définit l'objet Book à afficher et met à jour les contrôles de la vue.
     * @param book l'objet Book dont les détails doivent être affichés.
     */
    public void setBook(Book book) {
        this.book = book;
        idBookLabel.setText(book.getIdBook());
        nomBookLabel.setText(book.getNomBook());
        // Affiche l'ID de la catégorie (adapter pour afficher le nom si nécessaire)
        catBookLabel.setText(String.valueOf(book.getCatBook()));
        dispoBookLabel.setText(book.getDispoBook());
        descriptionLabel.setText(book.getDescription());
        pdfFileLabel.setText(book.getPdfFile());

        // Charger et afficher l'image du book
        if (book.getPicture() != null && !book.getPicture().isEmpty()) {
            File imageFile = new File(book.getPicture());
            if (imageFile.exists()) {
                try (FileInputStream fis = new FileInputStream(imageFile)) {
                    Image image = new Image(fis);
                    bookImageView.setImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
                }
            } else {
                System.err.println("Fichier image introuvable : " + book.getPicture());
            }
        }


    }

    /**
     * Génère un QR code à partir d'une chaîne et l'affiche dans l'ImageView qrCodeImageView.
     *
     * @param data  La donnée à encoder dans le QR code (par exemple, un File ID ou URL).
     * @param width  Largeur du QR code.
     * @param height Hauteur du QR code.
     */


    /**
     * Gère l'action du bouton "Télécharger PDF".
     * Si le fichier PDF existe, il est ouvert avec l'application par défaut.
     * @param event l'événement associé au clic sur le bouton.
     */
    @FXML
    private void handleDownloadPdf(ActionEvent event) {
        if (book != null && book.getPdfFile() != null && !book.getPdfFile().isEmpty()) {
            File pdf = new File(book.getPdfFile());
            if (pdf.exists()) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(pdf);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Erreur lors de l'ouverture du fichier PDF : " + e.getMessage());
                    }
                } else {
                    System.err.println("La fonctionnalité Desktop n'est pas supportée sur ce système.");
                }
            } else {
                System.err.println("Fichier PDF introuvable : " + book.getPdfFile());
            }
        } else {
            System.err.println("Aucun fichier PDF spécifié.");
        }
    }
}
