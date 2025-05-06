package services;

import entities.Stage;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StageService implements ServiceCRUD<Stage> {

    private Connection cnx;

    public StageService() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Stage stage) throws SQLException {
        String query = "INSERT INTO stage (titre, description, entreprise, lieu, duree, date_debut, categorie_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, stage.getTitre());
            stmt.setString(2, stage.getDescription());
            stmt.setString(3, stage.getEntreprise());
            stmt.setString(4, stage.getLieu());
            stmt.setInt(5, stage.getDuree());
            stmt.setDate(6, new java.sql.Date(stage.getDateDebut().getTime()));
            stmt.setInt(7, stage.getCategorieId());
            stmt.executeUpdate();
            System.out.println("Stage ajouté avec succès !");
        }
    }

    @Override
    public void modifier(Stage stage) throws SQLException {
        String query = "UPDATE stage SET titre = ?, description = ?, entreprise = ?, lieu = ?, duree = ?, date_debut = ?, categorie_id = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, stage.getTitre());
            stmt.setString(2, stage.getDescription());
            stmt.setString(3, stage.getEntreprise());
            stmt.setString(4, stage.getLieu());
            stmt.setInt(5, stage.getDuree());
            stmt.setDate(6, new java.sql.Date(stage.getDateDebut().getTime()));
            stmt.setInt(7, stage.getCategorieId());
            stmt.setInt(8, stage.getId());
            stmt.executeUpdate();
            System.out.println("Stage modifié avec succès !");
        }
    }

    @Override
    public void supprimer(Stage stage) throws SQLException {
        String query = "DELETE FROM stage WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, stage.getId());
            stmt.executeUpdate();
            System.out.println("Stage supprimé avec succès !");
        }
    }

    @Override
    public List<Stage> recuperer() throws SQLException {
        List<Stage> stages = new ArrayList<>();
        String query = "SELECT * FROM stage";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Stage stage = new Stage(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("entreprise"),
                        rs.getString("lieu"),
                        rs.getInt("duree"),
                        rs.getDate("date_debut"),
                        rs.getInt("categorie_id")
                );
                stages.add(stage);
            }
        }
        return stages;
    }
}