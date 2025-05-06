package services;

import entities.Contenu;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContenuService {

    private Connection cnx;

    public ContenuService() {
        cnx = MyDatabase.getInstance().getCnx();
    }
    public List<Contenu> getContenusParNiveau(int niveauId) throws SQLException {
        System.out.println("Fetching content for niveau ID: " + niveauId);
        List<Contenu> contenus = new ArrayList<>();

        String sql = "SELECT * FROM contenu WHERE niveau_id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, niveauId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Contenu contenu = new Contenu(
                    rs.getInt("id"),
                    rs.getInt("niveau_id"),
                    rs.getString("type"),
                    rs.getString("fichier"),
                    rs.getString("description"),
                    rs.getString("youtube_id")
            );
            contenus.add(contenu);
        }

        return contenus;
    }
    // Ajoute un contenu pour un niveau donné
    public void ajouter(Contenu contenu) throws SQLException {
        String sql = "INSERT INTO contenu (niveau_id, type, fichier, description, youtube_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, contenu.getNiveauId());
        ps.setString(2, contenu.getType());
        ps.setString(3, contenu.getFichier());
        ps.setString(4, contenu.getDescription());
        ps.setString(5, contenu.getYoutubeId());
        ps.executeUpdate();
        System.out.println("Contenu ajouté !");
    }
}
