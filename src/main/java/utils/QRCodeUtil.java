package utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeUtil {

    /**
     * Décode le contenu d'un QR code présent dans le fichier image.
     *
     * @param qrCodeImage Le fichier image contenant le QR code.
     * @return Le texte décodé ou null si aucun QR code n'est trouvé.
     * @throws IOException En cas d'erreur de lecture.
     */
    public static String decodeQRCode(File qrCodeImage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeImage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("Aucun QR code trouvé dans l'image");
            return null;
        }
    }
}
