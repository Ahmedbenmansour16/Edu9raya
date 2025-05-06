package services;

import entities.CategorieStage;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieStageService implements ServiceCRUD<CategorieStage> {

    private Connection cnx;

    public CategorieStageService() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void ajouter(CategorieStage categorieStage) throws SQLException {
        String query = "INSERT INTO categorie_stage (nom, description) VALUES (?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, categorieStage.getNom());
            stmt.setString(2, categorieStage.getDescription());
            stmt.executeUpdate();
            System.out.println("Catégorie ajoutée avec succès !");
        }
    }

    @Override
    public void modifier(CategorieStage categorieStage) throws SQLException {
        String query = "UPDATE categorie_stage SET nom = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, categorieStage.getNom());
            stmt.setString(2, categorieStage.getDescription());
            stmt.setInt(3, categorieStage.getId());
            stmt.executeUpdate();
            System.out.println("Catégorie modifiée avec succès !");
        }
    }

    @Override
    public void supprimer(CategorieStage categorieStage) throws SQLException {
        String query = "DELETE FROM categorie_stage WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, categorieStage.getId());
            stmt.executeUpdate();
            System.out.println("Catégorie supprimée avec succès !");
        }
    }

    @Override
    public List<CategorieStage> recuperer() throws SQLException {
        List<CategorieStage> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie_stage";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                CategorieStage categorieStage = new CategorieStage(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description")
                );
                categories.add(categorieStage);
            }
        }
        return categories;
    }

    public CategorieStage recupererParId(int id) throws SQLException {
        String query = "SELECT * FROM categorie_stage WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Return the category if found
                    return new CategorieStage(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("description")
                    );
                } else {
                    return null;
                }
            }
        }
    }
}