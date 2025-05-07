package services;

import entities.Certificat;
import entities.ClassementEtudiant;
import entities.StatistiqueTest;
import entities.User;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatistiqueService {
    private Connection cnx;
    private UserService userService;

    public StatistiqueService() {
        cnx = MyDatabase.getInstance().getCnx();
        userService = new UserService();
    }

    public StatistiqueTest getStatistiquesByTestId(int testId) throws SQLException {
        String sql = "SELECT AVG(score) as score_moyen, MAX(score) as score_max, COUNT(*) as nombre_participants " +
                "FROM certificat WHERE test_id = ?";

        StatistiqueTest stats = new StatistiqueTest();
        stats.setTestId(testId);

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, testId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.setScoreMoyen(rs.getDouble("score_moyen"));
                    stats.setScoreMaximum(rs.getDouble("score_max"));
                    stats.setNombreParticipants(rs.getInt("nombre_participants"));
                }
            }
        }

        // Récupérer le nom du test
        String sqlNomTest = "SELECT nom FROM test WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sqlNomTest)) {
            ps.setInt(1, testId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.setTestNom(rs.getString("nom"));
                }
            }
        }

        // Récupérer le formationId à partir de la table Test
        String sqlFormationId = "SELECT formation_id FROM test WHERE id = ?";
        int formationId = -1;
        try (PreparedStatement ps = cnx.prepareStatement(sqlFormationId)) {
            ps.setInt(1, testId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    formationId = rs.getInt("formation_id");
                }
            }
        }

        // Récupérer le nom de la formation à partir de la table Formation
        if (formationId != -1) {
            String sqlFormationNom = "SELECT nom FROM formation WHERE id = ?";
            try (PreparedStatement ps = cnx.prepareStatement(sqlFormationNom)) {
                ps.setInt(1, formationId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        stats.setFormationNom(rs.getString("nom"));
                    }
                }
            }
        }

        // Récupérer le meilleur étudiant
        String sqlMeilleur = "SELECT user_id FROM certificat WHERE test_id = ? AND score = ? LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(sqlMeilleur)) {
            ps.setInt(1, testId);
            ps.setDouble(2, stats.getScoreMaximum());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    User meilleurEtudiant = userService.recupererParId(userId);
                    stats.setMeilleurEtudiant(meilleurEtudiant);
                }
            }
        }

        return stats;
    }

    public List<StatistiqueTest> getAllStatistiques() throws SQLException {
        List<StatistiqueTest> allStats = new ArrayList<>();
        String sqlTestIds = "SELECT DISTINCT test_id FROM certificat";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sqlTestIds)) {
            while (rs.next()) {
                int testId = rs.getInt("test_id");
                StatistiqueTest stats = getStatistiquesByTestId(testId);
                allStats.add(stats);
            }
        }

        return allStats;
    }

    public List<ClassementEtudiant> getClassementByTestId(int testId) throws SQLException {
        List<ClassementEtudiant> classement = new ArrayList<>();
        String sql = "SELECT user_id, score FROM certificat WHERE test_id = ? ORDER BY score DESC";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, testId);
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    double score = rs.getDouble("score");
                    User etudiant = userService.recupererParId(userId);

                    ClassementEtudiant entry = new ClassementEtudiant(rank, etudiant, score, testId);
                    classement.add(entry);
                    rank++;
                }
            }
        }

        return classement;
    }

    public List<ClassementEtudiant> getTopPerformers(int limit) throws SQLException {
        List<ClassementEtudiant> topPerformers = new ArrayList<>();
        String sql = "SELECT c.user_id, c.test_id, c.score, " +
                "(SELECT COUNT(*) + 1 FROM certificat c2 WHERE c2.test_id = c.test_id AND c2.score > c.score) as rank " +
                "FROM certificat c ORDER BY c.score DESC LIMIT ?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    int testId = rs.getInt("test_id");
                    double score = rs.getDouble("score");
                    int rank = rs.getInt("rank");

                    User etudiant = userService.recupererParId(userId);
                    ClassementEtudiant entry = new ClassementEtudiant(rank, etudiant, score, testId);
                    topPerformers.add(entry);
                }
            }
        }

        return topPerformers;
    }

    public double getScoreMoyenGlobal() throws SQLException {
        String sql = "SELECT AVG(score) as score_moyen_global FROM certificat";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("score_moyen_global");
            }
        }
        return 0;
    }

    public int getNombreTotalCertificats() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM certificat";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
}