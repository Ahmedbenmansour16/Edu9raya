package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Cour;
import tn.esprit.models.Module;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourService implements IService<Cour> {

    private Connection connection = MyDataBase.getInstance().getCnx();
    private ModuleService moduleService = new ModuleService();

    @Override
    public void add(Cour cour) {
        String req = "INSERT INTO cour (code_cours, titre, niveau, categorie, module_id, description, pdf_path, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, cour.getCodeCours());
            ps.setString(2, cour.getTitre());
            ps.setString(3, cour.getNiveau());
            ps.setString(4, cour.getCategorie());
            ps.setInt(5, cour.getModule().getId());
            ps.setString(6, cour.getDescription());
            ps.setString(7, cour.getPdfPath());
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();
            System.out.println("Course added successfully!");
        } catch (SQLException e) {
            System.out.println("Error while adding the course: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Cour cour) {
        String req = "UPDATE cour SET code_cours = ?, titre = ?, niveau = ?, categorie = ?, module_id = ?, description = ?, pdf_path = ?, updated_at = ? WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, cour.getCodeCours());
            ps.setString(2, cour.getTitre());
            ps.setString(3, cour.getNiveau());
            ps.setString(4, cour.getCategorie());
            ps.setInt(5, cour.getModule().getId());
            ps.setString(6, cour.getDescription());
            ps.setString(7, cour.getPdfPath());
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            ps.setInt(9, cour.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Course updated successfully!");
            } else {
                System.out.println("No course found with this ID!");
            }
        } catch (SQLException e) {
            System.out.println("Error while updating the course: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM cour WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Course deleted successfully!");
            } else {
                System.out.println("No course found with this ID!");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting the course: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Cour> retrieveAll() {
        String req = "SELECT c.id, c.code_cours, c.titre, c.niveau, c.categorie, c.module_id, c.description, c.pdf_path, c.updated_at " +
                "FROM cour c JOIN module m ON c.module_id = m.id";
        List<Cour> courList = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Cour cour = new Cour();
                cour.setId(rs.getInt("id"));
                cour.setCodeCours(rs.getString("code_cours"));
                cour.setTitre(rs.getString("titre"));
                cour.setNiveau(rs.getString("niveau"));
                cour.setCategorie(rs.getString("categorie"));
                Module module = moduleService.retrieveById(rs.getInt("module_id"));
                cour.setModule(module != null ? module : new Module(rs.getInt("module_id")));
                cour.setDescription(rs.getString("description"));
                cour.setPdfPath(rs.getString("pdf_path"));
                cour.setUpdatedAt(rs.getTimestamp("updated_at"));

                courList.add(cour);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving courses: " + e.getMessage());
            e.printStackTrace();
        }

        return courList;
    }

    public List<Cour> retrieveByModuleId(int moduleId) {
        String req = "SELECT id, code_cours, titre, niveau, categorie, module_id, description, pdf_path, updated_at " +
                "FROM cour WHERE module_id = ?";
        List<Cour> courList = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, moduleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cour cour = new Cour();
                cour.setId(rs.getInt("id"));
                cour.setCodeCours(rs.getString("code_cours"));
                cour.setTitre(rs.getString("titre"));
                cour.setNiveau(rs.getString("niveau"));
                cour.setCategorie(rs.getString("categorie"));
                Module module = moduleService.retrieveById(rs.getInt("module_id"));
                cour.setModule(module != null ? module : new Module(rs.getInt("module_id")));
                cour.setDescription(rs.getString("description"));
                cour.setPdfPath(rs.getString("pdf_path"));
                cour.setUpdatedAt(rs.getTimestamp("updated_at"));

                courList.add(cour);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving courses for module ID " + moduleId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return courList;
    }
}