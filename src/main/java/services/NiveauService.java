package services;

import entities.Niveau;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NiveauService {

    private Connection cnx;

    public NiveauService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Crée un niveau si inexistant et retourne son ID

    public int creerNiveauSiInexistant(Niveau niveau) throws SQLException {
        String checkSql = "SELECT id FROM niveau WHERE formation_id = ? AND ordre = ?";
        PreparedStatement psCheck = cnx.prepareStatement(checkSql);
        psCheck.setInt(1, niveau.getFormationId());
        psCheck.setInt(2, niveau.getOrdre());
        ResultSet rs = psCheck.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("id");
            System.out.println("Niveau déjà existant avec ID : " + id + ", ordre : " + niveau.getOrdre());
            return id;
        }

        String insertSql = "INSERT INTO niveau (formation_id, ordre) VALUES(?, ?)";
        PreparedStatement psInsert = cnx.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
        psInsert.setInt(1, niveau.getFormationId());
        psInsert.setInt(2, niveau.getOrdre());  // Assurez-vous que cette valeur est correcte
        psInsert.executeUpdate();

        ResultSet rsInsert = psInsert.getGeneratedKeys();
        if (rsInsert.next()) {
            int id = rsInsert.getInt(1);
            System.out.println("Niveau créé avec ID : " + id + ", ordre : " + niveau.getOrdre());
            return id;
        }
        return -1;
    }
    public List<Niveau> getNiveauxByFormationId(int formationId) throws SQLException {
        List<Niveau> niveaux = new ArrayList<>();
        String sql = "SELECT id, formation_id, ordre FROM niveau WHERE formation_id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, formationId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int ordre = rs.getInt("ordre");
            Niveau niveau = new Niveau(id, formationId, ordre);
            niveaux.add(niveau);
        }
        return niveaux;
    }
    // Récupère l'ID d'un niveau par formation et numéro de niveau
    public int getNiveauId(int formationId, int numeroNiveau) throws SQLException {
        String sql = "SELECT id FROM niveau WHERE formation_id = ? AND ordre = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, formationId);
        ps.setInt(2, numeroNiveau);  // ici numeroNiveau correspond à ordre

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }

        return -1; // Retourne -1 si le niveau n'existe pas
    }
}
