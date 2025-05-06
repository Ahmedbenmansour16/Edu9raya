package services;

import entities.Reclamation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {

    private Connection cnx;

    public ReclamationService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public void ajouter(Reclamation reclamation) throws SQLException {
        String sql = "INSERT INTO reclamation (sujet, justification, image_path, date_envoi, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, reclamation.getSujet());
            ps.setString(2, reclamation.getJustification());
            ps.setString(3, reclamation.getImagePath());
            ps.setTimestamp(4, Timestamp.valueOf(reclamation.getDateEnvoi()));
            ps.setString(5, reclamation.getStatut());
            ps.executeUpdate();
        }
    }

    public List<Reclamation> recuperer() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setSujet(rs.getString("sujet"));
                reclamation.setJustification(rs.getString("justification"));
                reclamation.setImagePath(rs.getString("image_path"));
                reclamation.setDateEnvoi(rs.getTimestamp("date_envoi").toLocalDateTime());
                reclamation.setStatut(rs.getString("statut"));
                reclamation.setAdminResponse(rs.getString("admin_response"));
                reclamations.add(reclamation);
            }
        }
        return reclamations;
    }

    public void modifier(Reclamation reclamation) throws SQLException {
        String sql = "UPDATE reclamation SET sujet = ?, justification = ?, image_path = ?, date_envoi = ?, statut = ?, admin_response = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, reclamation.getSujet());
            ps.setString(2, reclamation.getJustification());
            ps.setString(3, reclamation.getImagePath());
            ps.setTimestamp(4, Timestamp.valueOf(reclamation.getDateEnvoi()));
            ps.setString(5, reclamation.getStatut());
            ps.setString(6, reclamation.getAdminResponse());
            ps.setInt(7, reclamation.getId());
            ps.executeUpdate();
        }
    }

    public void respondToReclamation(int reclamationId, String adminResponse) throws SQLException {
        String sql = "UPDATE reclamation SET admin_response = ?, statut = 'Traité' WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, adminResponse);
            ps.setInt(2, reclamationId);
            ps.executeUpdate();
        }
    }
}