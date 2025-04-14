package service;

import model.Categorie;
import utils.SQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService {

    // Création d'une nouvelle catégorie
    public boolean createCategorie(Categorie categorie) {
        Connection connection = SQLConnection.getInstance().getConnection();
        String query = "INSERT INTO categorie (id_cat, nom_cat) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categorie.getIdCat());
            ps.setString(2, categorie.getNomCat());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        categorie.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Récupérer toutes les catégories
    public List<Categorie> getAllCategories() {
        List<Categorie> categories = new ArrayList<>();
        Connection connection = SQLConnection.getInstance().getConnection();
        String query = "SELECT * FROM categorie";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Categorie categorie = new Categorie();
                categorie.setId(rs.getInt("id"));
                categorie.setIdCat(rs.getString("id_cat"));
                categorie.setNomCat(rs.getString("nom_cat"));
                categories.add(categorie);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    // Mise à jour d'une catégorie existante
    public boolean updateCategorie(Categorie categorie) {
        Connection connection = SQLConnection.getInstance().getConnection();
        String query = "UPDATE categorie SET id_cat = ?, nom_cat = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, categorie.getIdCat());
            ps.setString(2, categorie.getNomCat());
            ps.setInt(3, categorie.getId());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Suppression d'une catégorie par identifiant
    public boolean deleteCategorie(int id) {
        Connection connection = SQLConnection.getInstance().getConnection();
        String query = "DELETE FROM categorie WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
