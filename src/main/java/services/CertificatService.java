package services;

import entities.Certificat;
import utils.MyDatabase;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class CertificatService {
    private Connection cnx;

    public CertificatService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public void ajouterCertificat(Certificat certificat) throws SQLException {
        String sql = "INSERT INTO certificat (user_id, test_id, date_obtention, score) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, certificat.getUserId());
            ps.setInt(2, certificat.getTestId());
            ps.setDate(3, Date.valueOf(certificat.getDateObtention()));
            ps.setDouble(4, certificat.getScore());
            ps.executeUpdate();
        }
    }
}
