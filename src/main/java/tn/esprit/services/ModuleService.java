package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Module;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleService implements IService<Module> {

    private Connection connection = MyDataBase.getInstance().getCnx();

    @Override
    public void add(Module module) {
        String req = "INSERT INTO module (nom, enseigant, duree, coefficient, image) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, module.getNom());
            ps.setString(2, module.getEnseignant());
            ps.setInt(3, module.getDuree());
            ps.setString(4, module.getCoefficient());
            ps.setString(5, module.getImage()); // Handle image field

            ps.executeUpdate();
            System.out.println("Module added successfully!");
        } catch (SQLException e) {
            System.out.println("Error while adding the module: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Module module) {
        String req = "UPDATE module SET nom = ?, enseigant = ?, duree = ?, coefficient = ?, image = ? WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, module.getNom());
            ps.setString(2, module.getEnseignant());
            ps.setInt(3, module.getDuree());
            ps.setString(4, module.getCoefficient());
            ps.setString(5, module.getImage()); // Handle image field
            ps.setInt(6, module.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Module updated successfully!");
            } else {
                System.out.println("No module found with this ID!");
            }
        } catch (SQLException e) {
            System.out.println("Error while updating the module: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM module WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Module deleted successfully!");
            } else {
                System.out.println("No module found with this ID!");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting the module: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Module> retrieveAll() {
        String req = "SELECT id, nom, enseigant, duree, coefficient, image FROM module";
        List<Module> moduleList = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Module module = new Module();
                module.setId(rs.getInt("id"));
                module.setNom(rs.getString("nom"));
                module.setEnseignant(rs.getString("enseigant"));
                module.setDuree(rs.getInt("duree"));
                module.setCoefficient(rs.getString("coefficient"));
                module.setImage(rs.getString("image")); // Retrieve image field

                moduleList.add(module);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving modules: " + e.getMessage());
            e.printStackTrace();
        }

        return moduleList;
    }
}