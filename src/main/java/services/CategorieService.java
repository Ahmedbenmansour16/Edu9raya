package services;

import entities.Categorie;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements Service<Categorie> {

    private Connection cnx;

    public CategorieService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public int ajouter(Categorie categorie) throws SQLException {
        // Utilisation d'un PreparedStatement pour éviter les injections SQL
        String sql = "INSERT INTO categorie(nom) VALUES(?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, categorie.getNom());
        ps.executeUpdate();
        System.out.println("Catégorie ajoutée en base !");
        return 0;
    }

    @Override
    public void modifier(Categorie categorie) throws SQLException {
        String sql = "UPDATE categorie SET nom = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, categorie.getNom());
        ps.setInt(2, categorie.getId());
        ps.executeUpdate();
        System.out.println("Catégorie modifiée !");
    }

    @Override
    public void supprimer(Categorie categorie) throws SQLException {
        String sql = "DELETE FROM categorie WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, categorie.getId());
        ps.executeUpdate();
        System.out.println("Catégorie supprimée !");
    }

    @Override
    public List<Categorie> recuperer() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorie";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id");
            String nom = rs.getString("nom");
            Categorie categorie = new Categorie(id, nom);
            categories.add(categorie);
        }
        return categories;
    }

    /**
     * Vérifie si une catégorie avec le nom spécifié existe déjà dans la base de données.
     *
     * @param nom Le nom de la catégorie à vérifier
     * @return true si une catégorie avec ce nom existe, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean existeParNom(String nom) throws SQLException {
        String query = "SELECT COUNT(*) FROM categorie WHERE nom = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, nom);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}