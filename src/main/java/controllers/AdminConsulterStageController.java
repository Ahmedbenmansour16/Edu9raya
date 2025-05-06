package controllers;

import entities.CategorieStage;
import entities.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import services.CategorieStageService;
import services.StageService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AdminConsulterStageController implements Initializable {
    @FXML
    private TextField LieuStage;

    @FXML
    private TextField LieuStage1;

    @FXML
    private TextField nomCategorie;

    @FXML
    private VBox ModifStage;

    @FXML
    private TextField titreStage;

    @FXML
    private TextField EntrepriseStage1;

    @FXML
    private Label erreurAjoutStage;

    @FXML
    private VBox AjoutStage;

    @FXML
    private TextField nomCategorie1;

    @FXML
    private TextArea descriptionCategorie1;

    @FXML
    private TextArea descriptionStage1;

    @FXML
    private Label erreurAjouotCategorie;

    @FXML
    private VBox categorieContainer;

    @FXML
    private Label erreurAjoutStage1;

    @FXML
    private ComboBox<CategorieStage> categorieStage1;

    @FXML
    private Label erreurModifCategorie;

    @FXML
    private VBox stageContainer;

    @FXML
    private TextField EntrepriseStage;

    @FXML
    private TextField DebStage1;

    @FXML
    private TextArea descriptionStage;

    @FXML
    private VBox stagesList;

    @FXML
    private TextField DebStage;

    @FXML
    private ComboBox<CategorieStage> categorieStage;

    @FXML
    private TextField titreStage1;

    @FXML
    private VBox categoriesList;

    @FXML
    private TextField DureeStage;

    @FXML
    private VBox AjoutCategorie;

    @FXML
    private VBox ModifCategorie;

    @FXML
    private TextArea descriptionCategorie;

    @FXML
    private TextField DureeStage1;

    @FXML
    private TextField recherche;

    @FXML
    private ComboBox<String> sortCriteria;


    //Declaration des services
    StageService Ss = new StageService();
    CategorieStageService Cs = new CategorieStageService();
    private CategorieStage localCategory;
    private Stage localStage;

    public CategorieStage getLocalCategory() {
        return localCategory;
    }

    public void setLocalCategory(CategorieStage localCategory) {
        this.localCategory = localCategory;
    }

    public Stage getLocalStage() {
        return localStage;
    }

    public void setLocalStage(Stage localStage) {
        this.localStage = localStage;
    }

    void filtrerRecherche() {
        String searchText = recherche.getText().trim(); // Get the search text
        if (stagesList.isVisible()) {
            filtrerStages(searchText); // Filter stages if the stage list is visible
        } else if (categoriesList.isVisible()) {
            filtrerCategories(searchText); // Filter categories if the category list is visible
        }
    }


    private void filtrerStages(String searchText) {
        try {
            List<Stage> stages = Ss.recuperer(); // Fetch all stages from the database
            stageContainer.getChildren().clear(); // Clear the current stage list

            for (Stage s : stages) {
                // Check if the search text matches any relevant fields (e.g., title, enterprise, etc.)
                if (s.getTitre().toLowerCase().contains(searchText.toLowerCase()) ||
                        s.getEntreprise().toLowerCase().contains(searchText.toLowerCase()) ||
                        s.getDescription().toLowerCase().contains(searchText.toLowerCase())) {

                    // Load the matching stage into the UI
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Stage.fxml"));
                    VBox stageItem = loader.load();
                    StageController stageController = loader.getController();
                    stageController.setData(s);
                    stageController.setMainController(this);
                    stageContainer.getChildren().add(stageItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrerCategories(String searchText) {
        try {
            List<CategorieStage> categories = Cs.recuperer(); // Fetch all categories from the database
            categorieContainer.getChildren().clear(); // Clear the current category list

            for (CategorieStage c : categories) {
                // Check if the search text matches any relevant fields (e.g., name, description)
                if (c.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        c.getDescription().toLowerCase().contains(searchText.toLowerCase())) {

                    // Load the matching category into the UI
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie.fxml"));
                    VBox categorieItem = loader.load();
                    CategorieController categorieController = loader.getController();
                    categorieController.setData(c);
                    categorieController.setMainController(this);
                    categorieContainer.getChildren().add(categorieItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Inizialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBox();
        naviguation(1);
        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerRecherche();
        });
        sortCriteria.setOnAction(event -> {
            String selectedOption = sortCriteria.getValue();
            if (stagesList.isVisible()) {
                try {
                    sortStages(selectedOption); // Sort stages if the stage list is visible
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (categoriesList.isVisible()) {
                sortCategories(selectedOption); // Sort categories if the category list is visible
            }
        });

    }

    private void sortStages(String criteria) throws Exception {
        try {
            List<Stage> stages = Ss.recuperer(); // Fetch all stages from the database
            stageContainer.getChildren().clear(); // Clear the current stage list

            switch (criteria) {
                case "Title (A-Z)":
                    stages.sort(Comparator.comparing(Stage::getTitre)); // Ascending order
                    break;
                case "Title (Z-A)":
                    stages.sort(Comparator.comparing(Stage::getTitre).reversed()); // Descending order
                    break;
                case "Date (Newest First)":
                    stages.sort(Comparator.comparing(Stage::getDateDebut).reversed()); // Newest first
                    break;
                case "Date (Oldest First)":
                    stages.sort(Comparator.comparing(Stage::getDateDebut)); // Oldest first
                    break;
                case "Enterprise (A-Z)":
                    stages.sort(Comparator.comparing(Stage::getEntreprise)); // Ascending order
                    break;
                case "Enterprise (Z-A)":
                    stages.sort(Comparator.comparing(Stage::getEntreprise).reversed()); // Descending order
                    break;
                case "Duration (Shortest First)":
                    stages.sort(Comparator.comparingInt(Stage::getDuree)); // Shortest first
                    break;
                case "Duration (Longest First)":
                    stages.sort(Comparator.comparingInt(Stage::getDuree).reversed()); // Longest first
                    break;
                case "Category Name (A-Z)":
                    stages.sort(Comparator.comparing(stage -> {
                        try {
                            CategorieStage categorieStage = Cs.recupererParId(stage.getCategorieId());
                            return categorieStage != null ? categorieStage.getNom() : "Unknown"; // Handle null categories
                        } catch (SQLException e) {
                            throw new RuntimeException("Error fetching category name", e);
                        }
                    }));
                    break;
              /*  case "Category Name (Z-A)":
                    stages.sort(Comparator.comparing(stage -> {
                        try {
                            Categorie categorie = Cs.recupererParId(stage.getCategorieId());
                            return categorie != null ? categorie.getNom() : "Unknown";
                        } catch (SQLException e) {
                            throw new RuntimeException("Error fetching category name", e);
                        }
                    }).reversed());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting criteria: " + criteria);*/
            }
            for (Stage s : stages) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Stage.fxml"));
                VBox stageItem = loader.load();
                StageController stageController = loader.getController();
                stageController.setData(s);
                stageController.setMainController(this);
                stageContainer.getChildren().add(stageItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error sorting stages", e); // Rethrow with a meaningful message
        }
    }

    private void sortCategories(String criteria) {
        try {
            List<CategorieStage> categories = Cs.recuperer(); // Fetch all categories from the database
            categorieContainer.getChildren().clear(); // Clear the current category list

            switch (criteria) {
                case "Name (A-Z)":
                    categories.sort(Comparator.comparing(CategorieStage::getNom)); // Ascending order
                    break;
                case "Name (Z-A)":
                    categories.sort(Comparator.comparing(CategorieStage::getNom).reversed()); // Descending order
                    break;
                case "Description Length (Shortest First)":
                    categories.sort(Comparator.comparingInt(c -> {
                        String description = c.getDescription(); // Ensure description is not null
                        return description == null ? 0 : description.length();
                    }));
                    break;
                /*case "Description Length (Longest First)":
                    categories.sort(Comparator.comparingInt(c -> {
                        String description = c.getDescription(); // Ensure description is not null
                        return description == null ? 0 : description.length();
                    }).reversed());
                    break;*/
                default:
                    throw new IllegalArgumentException("Invalid sorting criteria: " + criteria);
            }

            // Reload the sorted categories into the UI
            for (CategorieStage c : categories) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie.fxml"));
                VBox categorieItem = loader.load();
                CategorieController categorieController = loader.getController();
                categorieController.setData(c);
                categorieController.setMainController(this);
                categorieContainer.getChildren().add(categorieItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error sorting categories", e); // Rethrow with a meaningful message
        }
    }

    public void refreshData() {
        try {
            List<CategorieStage> categories = Cs.recuperer();
            categorieStage.getItems().clear();
            categorieStage.getItems().addAll(categories);
            categorieStage1.getItems().clear();
            categorieStage1.getItems().addAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des catégories : " + e.getMessage());
        }
    }
//Combo Box Setup
    private void setupComboBox() {
        categorieStage.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(CategorieStage categorieStage, boolean empty) {
                super.updateItem(categorieStage, empty);
                if (empty || categorieStage == null) {
                    setText(null);
                } else {
                    setText(categorieStage.getNom());
                }
            }
        });
        categorieStage1.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(CategorieStage categorieStage, boolean empty) {
                super.updateItem(categorieStage, empty);
                if (empty || categorieStage == null) {
                    setText(null);
                } else {
                    setText(categorieStage.getNom());
                }
            }
        });
        categorieStage.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(CategorieStage categorieStage, boolean empty) {
                super.updateItem(categorieStage, empty);
                if (empty || categorieStage == null) {
                    setText(null);
                } else {
                    setText(categorieStage.getNom());
                }
            }
        });
        categorieStage1.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(CategorieStage categorieStage, boolean empty) {
                super.updateItem(categorieStage, empty);
                if (empty || categorieStage == null) {
                    setText(null);
                } else {
                    setText(categorieStage.getNom());
                }
            }
        });
    }

    public CategorieStage getSelectedCategorie() {
        return categorieStage.getValue();
    }

    public CategorieStage getSelectedCategorie1() {
        return categorieStage1.getValue();
    }

    public static boolean DateValide(String dateStr) {
        String dateFormat = "yyyy-MM-dd"; //Format sql
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false); //Accepter que le format sql
        try {
            Date parsedDate = sdf.parse(dateStr); //convertion de la date au format sql
            Date today = new Date(); //Date actuel
            return !parsedDate.before(today); //Voit si la date saisie est avant la date actuel
        } catch (ParseException e) { // Si erreur pendant conversion cela veut dire que la date est incorrect
            return false;
        }
    }

    //Fonction Verif Formulaire Stage
    boolean VerifStageForm(int type) { //Si type 1 alors verif formulaire ajout sinon verif formulaire modif
        if (type == 1) {
            if (titreStage.getText().trim().isEmpty()) {
                erreurAjoutStage.setText("Veuillez Saisir le titre du stage !");
                return false;
            }
            if (descriptionStage.getText().trim().isEmpty()) {
                erreurAjoutStage.setText("Veuillez Saisir la description du stage ! ");
                return false;
            }
            if (EntrepriseStage.getText().trim().isEmpty()) {
                erreurAjoutStage.setText("Veuillez Saisir l'entreprise qui organise ce stage !");
                return false;
            }
            if (LieuStage.getText().trim().isEmpty()) {
                erreurAjoutStage.setText("Veuillez Saisir le lieu du stage !");
                return false;
            }
            if (!DureeStage.getText().trim().isEmpty()) {
                try {
                    int duration = Integer.parseInt(DureeStage.getText().trim());
                    if (duration <= 0) {
                        erreurAjoutStage.setText("Veuillez saisir une durée de stage positive !");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    erreurAjoutStage.setText("Veuillez saisir une durée de stage correcte !");
                    return false;
                }
            } else {
                erreurAjoutStage.setText("Veuillez saisir une durée de stage !");
                return false;
            }
            if (DebStage.getText().trim().isEmpty() || !DateValide(DebStage.getText())) {
                erreurAjoutStage.setText("Veuillez Saisir une date correcte !");
                return false;
            }
            if (getSelectedCategorie() == null) {
                erreurAjoutStage.setText("Veuillez Ajouter une catégorie !");
                return false;
            }

            erreurAjoutStage.setText("");
            return true;
        } else {
            if (titreStage1.getText().trim().isEmpty()) {
                erreurAjoutStage1.setText("Veuillez Saisir le titre du stage !");
                return false;
            }
            if (descriptionStage1.getText().trim().isEmpty()) {
                erreurAjoutStage1.setText("Veuillez Saisir la description du stage ! ");
                return false;
            }
            if (EntrepriseStage1.getText().trim().isEmpty()) {
                erreurAjoutStage1.setText("Veuillez Saisir l'entreprise qui organise ce stage !");
                return false;
            }
            if (LieuStage1.getText().trim().isEmpty()) {
                erreurAjoutStage1.setText("Veuillez Saisir le lieu du stage !");
                return false;
            }
            if (!DureeStage1.getText().trim().isEmpty()) {
                try {
                    int duration = Integer.parseInt(DureeStage1.getText().trim());
                    if (duration <= 0) {
                        erreurAjoutStage1.setText("Veuillez saisir une durée de stage positive !");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    erreurAjoutStage1.setText("Veuillez saisir une durée de stage correcte !");
                    return false;
                }
            } else {
                erreurAjoutStage1.setText("Veuillez saisir une durée de stage !");
                return false;
            }
            if (DebStage1.getText().trim().isEmpty() || !DateValide(DebStage1.getText())) {
                erreurAjoutStage1.setText("Veuillez Saisir une date correcte !");
                return false;
            }
            if (getSelectedCategorie1() == null) {
                erreurAjoutStage1.setText("Veuillez Ajouter une catégorie !");
                return false;
            }
            erreurAjoutStage1.setText("");
            return true;
        }
    }

    //Fonction Verif Formulaire Categorie
    boolean VerifCategorieForm(int type) {  //Si type 1 alors verif formulaire ajout sinon verif formulaire modif
        if (type == 1) {
            if (nomCategorie.getText().trim().isEmpty()) {
                erreurAjouotCategorie.setText("Veuillez Saisir le nom de la categorie !");
                return false;
            }
            if (descriptionCategorie.getText().trim().isEmpty()) {
                erreurAjouotCategorie.setText("Veuillez Saisir la description categorie !");
                return false;
            }
            erreurAjouotCategorie.setText("");
            return true;

        } else {
            if (nomCategorie1.getText().trim().isEmpty()) {
                erreurModifCategorie.setText("Veuillez Saisir le nom de la categorie !");
                return false;
            }
            if (descriptionCategorie1.getText().trim().isEmpty()) {
                erreurModifCategorie.setText("Veuillez Saisir la description categorie !");
                return false;
            }
            erreurModifCategorie.setText("");
            return true;
        }
    }


    @FXML
    void AjoutStage(ActionEvent event) throws Exception {
        if (VerifStageForm(1)) {
            String dateFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);
            Stage s = new Stage(
                    titreStage.getText().trim(),
                    descriptionStage.getText().trim(),
                    EntrepriseStage.getText().trim(),
                    LieuStage.getText().trim(),
                    Integer.parseInt(DureeStage.getText().trim()),
                    sdf.parse(DebStage.getText().trim()),
                    getSelectedCategorie().getId()
            );
            Ss.ajouter(s);
            naviguation(1);
            titreStage.setText("");
            descriptionStage.setText("");
            EntrepriseStage.setText("");
            LieuStage.setText("");
            DureeStage.setText("");
            DebStage.setText("");
            categorieStage.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void ModifStage(ActionEvent event) throws Exception {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        if (VerifStageForm(2)) {
            Stage s = getLocalStage();
            s.setTitre(titreStage1.getText().trim());
            s.setDescription(descriptionStage1.getText().trim());
            s.setEntreprise(EntrepriseStage1.getText().trim());
            s.setLieu(LieuStage1.getText().trim());
            s.setDuree(Integer.parseInt(DureeStage1.getText().trim()));
            s.setDateDebut(sdf.parse(DebStage1.getText().trim()));
            if(getSelectedCategorie1()!=null)  s.setCategorieId(getSelectedCategorie1().getId());
            Ss.modifier(s);
            naviguation(1);
        }
    }

    @FXML
    void AjoutCategorie(ActionEvent event) throws Exception {
        if (VerifCategorieForm(1)) {
            CategorieStage c = new CategorieStage(
                    nomCategorie.getText().trim(),
                    descriptionCategorie.getText().trim()
            );
            System.out.println(c);
            Cs.ajouter(c);
            naviguation(2);
            nomCategorie.setText("");
            descriptionCategorie.setText("");
        }
    }

    @FXML
    void ModifCategorie(ActionEvent event) throws Exception {
        if (VerifCategorieForm(2)) {
            CategorieStage c = getLocalCategory();
            c.setNom(nomCategorie1.getText().trim());
            c.setDescription(descriptionCategorie1.getText().trim());
            System.out.println(c);
            Cs.modifier(c);
            naviguation(2);
            nomCategorie.setText("");
            descriptionCategorie.setText("");
        }
    }

    @FXML
    void AnnulerAjoutStage(ActionEvent event) {
        titreStage.setText("");
        descriptionStage.setText("");
        EntrepriseStage.setText("");
        LieuStage.setText("");
        DureeStage.setText("");
        DebStage.setText("");
        categorieStage.getSelectionModel().clearSelection();
    }

    @FXML
    void AnnulerModifStage(ActionEvent event) {
        titreStage1.setText("");
        descriptionStage1.setText("");
        EntrepriseStage1.setText("");
        LieuStage1.setText("");
        DureeStage1.setText("");
        DebStage1.setText("");
        categorieStage1.getSelectionModel().clearSelection();
        naviguation(1);
    }

    @FXML
    void AnuulerAjoutCategorie(ActionEvent event) {
        nomCategorie.setText("");
        descriptionCategorie.setText("");
    }

    @FXML
    void AnnulerModifCategorie(ActionEvent event) {
        nomCategorie.setText("");
        descriptionCategorie.setText("");
        naviguation(2);
    }

    public void setStageForModification(Stage s) throws Exception {
        titreStage1.setText(s.getTitre());
        descriptionStage1.setText(s.getDescription());
        EntrepriseStage1.setText(s.getEntreprise());
        LieuStage1.setText(s.getLieu());
        DebStage1.setText(s.getDateDebut().toString());
        DureeStage1.setText(String.valueOf(s.getDuree()));
        refreshData();
        CategorieStage selectedCategory = Cs.recupererParId(s.getCategorieId());
        System.out.println(selectedCategory);
        if (selectedCategory != null) {
            categorieStage1.getSelectionModel().select(selectedCategory);
        } else {
        }
        naviguation(4);
    }

    void loadStages() throws Exception {
        List<Stage> stages = Ss.recuperer();
        System.out.println(stages);
        stageContainer.getChildren().clear();
        for (Stage s : stages) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Stage.fxml"));
                VBox stageItem = loader.load();
                StageController stageController = loader.getController();
                stageController.setData(s);
                stageController.setMainController(this);
                stageContainer.getChildren().add(stageItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCategoryForModification(CategorieStage c) {
        nomCategorie1.setText(c.getNom());
        descriptionCategorie1.setText(c.getDescription());
        naviguation(6);
    }

    void loadCategories() throws Exception {
        List<CategorieStage> categories = Cs.recuperer();
        System.out.println(categories);
        categorieContainer.getChildren().clear();
        for (CategorieStage c : categories) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie.fxml"));
                VBox categorieItem = loader.load();
                CategorieController categorieController = loader.getController();
                categorieController.setData(c);
                categorieController.setMainController(this);
                categorieContainer.getChildren().add(categorieItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /*Partie pour la naviguation*/

    void naviguation(int page) {
        AjoutStage.setVisible(false);
        ModifStage.setVisible(false);
        stagesList.setVisible(false);
        AjoutCategorie.setVisible(false);
        ModifCategorie.setVisible(false);
        categoriesList.setVisible(false);
        sortCriteria.getItems().clear();
        switch (page) {
            case 1:
                try {
                    loadStages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sortCriteria.getItems().addAll(
                        "Title (A-Z)", "Title (Z-A)",
                        "Date (Newest First)", "Date (Oldest First)",
                        "Enterprise (A-Z)", "Enterprise (Z-A)",
                        "Duration (Shortest First)", "Duration (Longest First)",
                        "Category Name (A-Z)", "Category Name (Z-A)"
                );
                stagesList.setVisible(true);
                break;
            case 2:
                try {
                    loadCategories();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sortCriteria.getItems().addAll(
                        "Name (A-Z)", "Name (Z-A)",
                        "Description Length (Shortest First)", "Description Length (Longest First)"
                );
                categoriesList.setVisible(true);
                break;
            case 3:
                refreshData();
                AjoutStage.setVisible(true);
                break;
            case 4:
                refreshData();
                ModifStage.setVisible(true);
                break;
            case 5:
                AjoutCategorie.setVisible(true);
                break;
            case 6:
                ModifCategorie.setVisible(true);
                break;
        }
    }

    @FXML
    void GoToStageList(ActionEvent event) {
        naviguation(1);
    }

    @FXML
    void GoToAjoutStage(ActionEvent event) {
        naviguation(3);
    }

    @FXML
    void GoToCategorieList(ActionEvent event) {
        naviguation(2);
    }

    @FXML
    void GoToAjoutCategorie(ActionEvent event) {
        naviguation(5);
    }


    @FXML
    void GoToMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeFormations.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) stageContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
