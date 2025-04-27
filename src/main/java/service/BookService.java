package service;

import model.Book;
import utils.SQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    /**
     * Insère un nouveau book dans la table "book".
     * @param book L'objet Book à insérer.
     * @return true si l'insertion est réussie.
     */
    public boolean createBook(Book book) {
        String sql = "INSERT INTO book(id_book, nom_book, cat_book, dispo_book, description, pdf_file, file_id, picture) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = SQLConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getIdBook());
            ps.setString(2, book.getNomBook());
            ps.setInt(3, book.getCatBook());
            ps.setString(4, book.getDispoBook());
            ps.setString(5, book.getDescription());
            ps.setString(6, book.getPdfFile());
            ps.setString(7, book.getFileId());
            ps.setString(8, book.getPicture());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        book.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Récupère tous les books de la table "book".
     * @return Une liste d'objets Book.
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book";
        try (Connection conn = SQLConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book b = new Book();
                b.setId(rs.getInt("id"));
                b.setIdBook(rs.getString("id_book"));
                b.setNomBook(rs.getString("nom_book"));
                b.setCatBook(rs.getInt("cat_book"));
                b.setDispoBook(rs.getString("dispo_book"));
                b.setDescription(rs.getString("description"));
                b.setPdfFile(rs.getString("pdf_file"));
                b.setFileId(rs.getString("file_id"));
                b.setPicture(rs.getString("picture"));
                // si vous avez ajouté last_page / notes :
                // b.setLastPage(rs.getInt("last_page"));
                // b.setNotes(rs.getString("notes"));
                books.add(b);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return books;
    }

    /**
     * Met à jour un book existant.
     * @param book L'objet Book à mettre à jour.
     * @return true si la mise à jour a réussi.
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE book SET id_book=?, nom_book=?, cat_book=?, dispo_book=?, "
                + "description=?, pdf_file=?, file_id=?, picture=? WHERE id=?";
        try (Connection conn = SQLConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getIdBook());
            ps.setString(2, book.getNomBook());
            ps.setInt(3, book.getCatBook());
            ps.setString(4, book.getDispoBook());
            ps.setString(5, book.getDescription());
            ps.setString(6, book.getPdfFile());
            ps.setString(7, book.getFileId());
            ps.setString(8, book.getPicture());
            ps.setInt(9, book.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un book par son id.
     * @param id L'id du book à supprimer.
     * @return true si la suppression a réussi.
     */
    public boolean deleteBook(int id) {
        String sql = "DELETE FROM book WHERE id=?";
        try (Connection conn = SQLConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Recherche les books dont l'id ou le nom commencent par le mot‑clé.
     * @param keyword Le mot‑clé de recherche.
     * @return Liste des livres correspondants.
     */
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE id_book LIKE ? OR nom_book LIKE ?";
        try (Connection conn = SQLConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book b = new Book();
                    b.setId(rs.getInt("id"));
                    b.setIdBook(rs.getString("id_book"));
                    b.setNomBook(rs.getString("nom_book"));
                    b.setCatBook(rs.getInt("cat_book"));
                    b.setDispoBook(rs.getString("dispo_book"));
                    b.setDescription(rs.getString("description"));
                    b.setPdfFile(rs.getString("pdf_file"));
                    b.setFileId(rs.getString("file_id"));
                    b.setPicture(rs.getString("picture"));
                    // b.setLastPage(rs.getInt("last_page"));
                    // b.setNotes(rs.getString("notes"));
                    books.add(b);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return books;
    }

    /**
     * (Optionnel) Met à jour la progression de lecture et les notes.
     */
    public boolean updateProgressAndNotes(int bookId, int lastPage, String notes) {
        String sql = "UPDATE book SET last_page=?, notes=? WHERE id=?";
        try (Connection conn = SQLConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lastPage);
            ps.setString(2, notes);
            ps.setInt(3, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
