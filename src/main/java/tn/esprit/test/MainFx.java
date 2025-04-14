package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file using an absolute path from the root of resources
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modules/ModuleList.fxml"));

        // Create the scene with the loaded FXML content, matching the FXML dimensions
        Scene scene = new Scene(loader.load(), 900, 700);

        // Set the stage properties
        primaryStage.setTitle("Liste des Événement");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}