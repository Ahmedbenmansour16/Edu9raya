package services;

import entities.Question;
import utils.MyDatabase;

import java.sql.*;

public class QuestionService {

    private Connection cnx;

    public QuestionService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajoute une question pour un test donné
    public void ajouter(Question question) throws SQLException {
        String sql = "INSERT INTO question (test_id, enonce, answer1, answer2, answer3, answer4, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, question.getTestId());
        ps.setString(2, question.getEnonce());
        ps.setString(3, question.getAnswer1());
        ps.setString(4, question.getAnswer2());
        ps.setString(5, question.getAnswer3());
        ps.setString(6, question.getAnswer4());
        ps.setInt(7, question.getCorrectAnswer());
        ps.executeUpdate();
        System.out.println("Question ajoutée !");
    }
}
