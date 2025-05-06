package controllers.Cours;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CoursDetails implements Initializable {
    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField dureeField;
    @FXML
    private TextField niveauField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private AnchorPane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize any necessary data or UI elements
    }

    @FXML
    private void handleSave() {
        // Implement save functionality
    }

    @FXML
    private void handleCancel() {
        // Implement cancel functionality
    }
} 