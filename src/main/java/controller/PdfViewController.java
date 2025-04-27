package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfViewController {

    @FXML private Button prevPageBtn;
    @FXML private Button nextPageBtn;
    @FXML private Label pageNumberLabel;
    @FXML private ImageView pdfImageView;

    private PDDocument document;
    private PDFRenderer renderer;
    private int currentPage;
    private int pageCount;

    /**
     * Call this right after loading the FXML to show your PDF.
     */
    public void loadPdf(String filePath) throws IOException {
        document = PDDocument.load(new File(filePath));
        renderer = new PDFRenderer(document);
        pageCount = document.getNumberOfPages();
        showPage(0);
    }

    private void showPage(int index) throws IOException {
        BufferedImage bim = renderer.renderImageWithDPI(index, 150);
        pdfImageView.setImage(SwingFXUtils.toFXImage(bim, null));
        currentPage = index;
        pageNumberLabel.setText((index + 1) + " / " + pageCount);
        prevPageBtn.setDisable(index == 0);
        nextPageBtn.setDisable(index == pageCount - 1);
    }

    @FXML
    private void handlePrev() throws IOException {
        if (currentPage > 0) showPage(currentPage - 1);
    }

    @FXML
    private void handleNext() throws IOException {
        if (currentPage < pageCount - 1) showPage(currentPage + 1);
    }

    /**
     * Remember to close the document when done.
     */
    public void close() throws IOException {
        if (document != null) document.close();
    }
}
