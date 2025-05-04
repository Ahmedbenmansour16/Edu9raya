package services;

import entities.Test;
import utils.MyDatabase;

import java.sql.*;

public class TestService {

    private Connection cnx;

    public TestService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Crée un test si inexistant pour une formation et retourne son ID
    public int creerTestSiInexistant(Test test) throws SQLException {
        String checkSql = "SELECT id FROM test WHERE formation_id = ?";
        PreparedStatement psCheck = cnx.prepareStatement(checkSql);
        psCheck.setInt(1, test.getFormationId());
        ResultSet rs = psCheck.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("id");
            System.out.println("Test déjà existant avec ID : " + id);
            return id;
        }
        String insertSql = "INSERT INTO test (formation_id) VALUES(?)";
        PreparedStatement psInsert = cnx.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
        psInsert.setInt(1, test.getFormationId());
        psInsert.executeUpdate();
        ResultSet rsInsert = psInsert.getGeneratedKeys();
        if (rsInsert.next()) {
            int id = rsInsert.getInt(1);
            System.out.println("Test créé avec ID : " + id);
            return id;
        }
        return -1;
    }
}
