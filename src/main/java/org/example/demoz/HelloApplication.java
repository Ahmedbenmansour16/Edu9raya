package org.example.demoz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.SQLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.sql.Connection;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Connection conn = SQLConnection.getInstance().getConnection();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        Logger.getLogger("org.apache.pdfbox").setLevel(Level.SEVERE);
        System.out.println("Chemin CSS : " + getClass().getResource("/css/style.css"));

        // Vérification du chargement du CSS
        String cssUrl = getClass().getResource("/css/style.css") != null
                ? getClass().getResource("/css/style.css").toExternalForm()
                : "CSS not found";
        System.out.println("Chemin du CSS : " + cssUrl);

        // Ajoutez le CSS par code pour contourner la référence dans le FXML si besoin
        if (!cssUrl.equals("CSS not found")) {
            scene.getStylesheets().add(cssUrl);
        }

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}

