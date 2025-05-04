package services;

import entities.Reclamation;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {
    private Connection cnx;

    public ReclamationService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public int ajouter(Reclamation reclamation) throws SQLException {
        String sql = "INSERT INTO reclamation(sujet, justification, image_path, date_envoi, statut) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, reclamation.getSujet());
        ps.setString(2, reclamation.getJustification());
        ps.setString(3, reclamation.getImagePath());
        ps.setTimestamp(4, Timestamp.valueOf(reclamation.getDateEnvoi()));
        ps.setString(5, reclamation.getStatut());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int id = rs.getInt(1);
            System.out.println("Réclamation ajoutée avec l'ID : " + id);
            return id;
        }
        return -1;
    }

    public void modifier(Reclamation reclamation) throws SQLException {
        String sql = "UPDATE reclamation SET sujet = ?, justification = ?, image_path = ?, statut = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, reclamation.getSujet());
        ps.setString(2, reclamation.getJustification());
        ps.setString(3, reclamation.getImagePath());
        ps.setString(4, reclamation.getStatut());
        ps.setInt(5, reclamation.getId());
        ps.executeUpdate();
        System.out.println("Réclamation modifiée !");
    }

    public void supprimer(Reclamation reclamation) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, reclamation.getId());
        ps.executeUpdate();
        System.out.println("Réclamation supprimée !");
    }

    public List<Reclamation> recuperer() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String sujet = rs.getString("sujet");
            String justification = rs.getString("justification");
            String imagePath = rs.getString("image_path");
            LocalDateTime dateEnvoi = rs.getTimestamp("date_envoi").toLocalDateTime();
            String statut = rs.getString("statut");
            Reclamation reclamation = new Reclamation(id, sujet, justification, imagePath, dateEnvoi, statut);
            reclamations.add(reclamation);
        }
        return reclamations;
    }
}