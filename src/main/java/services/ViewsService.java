package services;

import entities.Module;
import entities.Views;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ViewsService {

    private Connection connection = MyDatabase.getInstance().getCnx();

    public void incrementView(Module module) {
        try {
            // Check if views record exists
            String checkQuery = "SELECT * FROM views WHERE module_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, module.getId());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Exists: increment view
                int currentViews = rs.getInt("view_count");
                String updateQuery = "UPDATE views SET view_count = ? WHERE module_id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, currentViews + 1);
                updateStmt.setInt(2, module.getId());
                updateStmt.executeUpdate();
            } else {
                // Not exists: insert new
                String insertQuery = "INSERT INTO views (module_id, view_count) VALUES (?, 1)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setInt(1, module.getId());
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error incrementing views: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getViewsByModule(Module module) {
        try {
            String query = "SELECT view_count FROM views WHERE module_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, module.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("view_count");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching views: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
} 