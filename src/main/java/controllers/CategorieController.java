package controllers;

import entities.CategorieStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.CategorieStageService;

public class CategorieController {

    private AdminConsulterStageController mainController;
    private CategorieStage categorieStage;

    public CategorieStage getCategorie() {
        return categorieStage;
    }

    public void setCategorie(CategorieStage categorieStage) {
        this.categorieStage = categorieStage;
    }

    public void setMainController(AdminConsulterStageController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private Label nom;

    @FXML
    private Label desc;

    @FXML
    void modifCategorie(ActionEvent event) {
        if (mainController != null) {
            mainController.setCategoryForModification(getCategorie());
            mainController.setLocalCategory(getCategorie());
        }
    }

    @FXML
    void SuppCategorie(ActionEvent event) {
        if (mainController != null) {
            try {
                CategorieStageService categorieStageService = new CategorieStageService();
                categorieStageService.supprimer(getCategorie());
                mainController.loadCategories();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void setData(CategorieStage c) {
        setCategorie(c);
        nom.setText(c.getNom());
        desc.setText(c.getDescription());
    }

}
