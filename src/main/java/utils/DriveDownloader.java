package utils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DriveDownloader {

    /**
     * Télécharge un PDF depuis Google Drive en utilisant l'ID du fichier et une clé API.
     *
     * @param fileId  L’ID du fichier sur Google Drive.
     * @param apiKey  Votre clé API Google.
     * @param destPath Chemin local de destination pour enregistrer le fichier.
     */
    public static void downloadPDF(String fileId, String apiKey, String destPath) {
        try {
            String urlStr = "https://www.googleapis.com/drive/v3/files/" + fileId + "?alt=media&key=" + apiKey;
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            OutputStream out = new FileOutputStream(destPath);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            in.close();
            System.out.println("Téléchargement terminé : " + destPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
