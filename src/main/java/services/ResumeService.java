package services;

import entities.Resume;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResumeService implements ServiceCRUD<Resume> {

    private Connection cnx;

    public ResumeService() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Resume resume) throws SQLException {
        String query = "INSERT INTO resume (filename, stage_id) VALUES (?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, resume.getFilename());
            stmt.setInt(2, resume.getStageId());
            stmt.executeUpdate();
            System.out.println("CV ajouté avec succès !");
        }
    }

    public void ajouterPdf(Resume resume, byte[] fileContent) throws SQLException {
        String query = "INSERT INTO resume (filename, stage_id, file_content) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, resume.getFilename());
            stmt.setInt(2, resume.getStageId());
            stmt.setBytes(3, fileContent);
            stmt.executeUpdate();
            System.out.println("CV ajouté avec succès !");
        }
    }

    @Override
    public void modifier(Resume resume) throws SQLException {
        String query = "UPDATE resume SET filename = ?, stage_id = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, resume.getFilename());
            stmt.setInt(2, resume.getStageId());
            stmt.setInt(3, resume.getId());
            stmt.executeUpdate();
            System.out.println("CV modifié avec succès !");
        }
    }

    @Override
    public void supprimer(Resume resume) throws SQLException {
        String query = "DELETE FROM resume WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, resume.getId());
            stmt.executeUpdate();
            System.out.println("CV supprimé avec succès !");
        }
    }

    @Override
    public List<Resume> recuperer() throws SQLException {
        List<Resume> resumes = new ArrayList<>();
        String query = "SELECT * FROM resume";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Resume resume = new Resume(
                        rs.getInt("id"),
                        rs.getString("filename"),
                        rs.getInt("stage_id")
                );
                resumes.add(resume);
            }
        }
        return resumes;
    }

    public Resume recupererParId(int id) throws SQLException {
        String query = "SELECT id, filename, stage_id, file_content FROM resume WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Resume resume = new Resume(
                            rs.getInt("id"),
                            rs.getString("filename"),
                            rs.getInt("stage_id")
                    );
                    resume.setFileContent(rs.getBytes("file_content"));
                    return resume;
                }
            }
        }
        return null; // Return null if no resume is found with the given ID
    }

    public List<Resume> recupererResumesParStageId(int stageId) throws SQLException {
        String query = "SELECT id, filename, stage_id, file_content FROM resume WHERE stage_id = ?";
        List<Resume> resumes = new ArrayList<>();
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, stageId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Resume resume = new Resume(
                            rs.getInt("id"),
                            rs.getString("filename"),
                            rs.getInt("stage_id")
                    );
                    resume.setFileContent(rs.getBytes("file_content"));
                    resumes.add(resume);
                }
            }
        }
        return resumes;
    }

    public List<Resume> recupererPDF() throws SQLException {
        List<Resume> resumes = new ArrayList<>();
        String query = "SELECT id, filename, stage_id, file_content FROM resume";
        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Resume resume = new Resume(
                        rs.getInt("id"),
                        rs.getString("filename"),
                        rs.getInt("stage_id")
                );
                resume.setFileContent(rs.getBytes("file_content"));
                resumes.add(resume);
            }
        }
        return resumes;
    }

    public int compterCVsParStageId(int stageId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM resume WHERE stage_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, stageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return 0;
    }


    public List<String> recupererFilenamesParStageId(int stageId) throws SQLException {
        List<String> filenames = new ArrayList<>();
        String query = "SELECT filename FROM resume WHERE stage_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, stageId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    filenames.add(rs.getString("filename"));
                }
            }
        }
        return filenames;
    }

}