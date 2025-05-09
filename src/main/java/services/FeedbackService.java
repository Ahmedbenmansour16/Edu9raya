package services;

import entities.Cour;
import entities.Feedback;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackService {

    private Connection connection = MyDatabase.getInstance().getCnx();

    public void addFeedback(Feedback feedback) {
        String query = "INSERT INTO feedback (course_id, content) VALUES (?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, feedback.getCourse().getId());
            ps.setString(2, feedback.getContent());
            ps.executeUpdate();
            System.out.println("✅ Feedback added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding feedback: " + e.getMessage());
        }
    }

    public void updateFeedback(Feedback feedback) {
        String query = "UPDATE feedback SET content = ? WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, feedback.getContent());
            ps.setInt(2, feedback.getId());
            ps.executeUpdate();
            System.out.println("✅ Feedback updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating feedback: " + e.getMessage());
        }
    }

    public void deleteFeedback(int id) {
        String query = "DELETE FROM feedback WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Feedback deleted successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting feedback: " + e.getMessage());
        }
    }

    public List<Feedback> getFeedbacksByCourse(Cour course) {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE course_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, course.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Feedback feedback = new Feedback(
                        rs.getInt("id"),
                        course,
                        rs.getString("content")
                );
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching feedbacks: " + e.getMessage());
        }

        return feedbackList;
    }
} 