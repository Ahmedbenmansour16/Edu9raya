package services;

import entities.Cour;
import entities.Rating;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingService {

    private Connection connection = MyDatabase.getInstance().getCnx();

    public void addRating(Rating rating) {
        String query = "INSERT INTO rating (course_id, rating_value) VALUES (?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, rating.getCourse().getId());
            ps.setInt(2, rating.getRatingValue());
            ps.executeUpdate();
            System.out.println("✅ Rating added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding rating: " + e.getMessage());
        }
    }

    public double getAverageRating(Cour course) {
        String query = "SELECT AVG(rating_value) AS avg_rating FROM rating WHERE course_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, course.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error calculating average rating: " + e.getMessage());
        }
        return 0;
    }
} 