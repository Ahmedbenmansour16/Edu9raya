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
        Connection connection = SQLConnection.getInstance().getConnection();
        String query = "INSERT INTO book(id_book, nom_book, cat_book, dispo_book, description, pdf_file, file_id, picture) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getIdBook());
            ps.setString(2, book.getNomBook());
            ps.setInt(3, book.getCatBook());
            ps.setString(4, book.getDispoBook());
            ps.setString(5, book.getDescription());
            ps.setString(6, book.getPdfFile());
            ps.setString(7, book.getFileId());
            ps.setString(8, book.getPicture());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if (generatedKeys.next()){
                        book.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch(SQLException ex) {
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
        Connection connection = SQLConnection.getInstance().getConnection();
        String query = "SELECT * FROM book";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while(rs.next()){
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setIdBook(rs.getString("id_book"));
                book.setNomBook(rs.getString("nom_book"));
                book.setCatBook(rs.getInt("cat_book"));
                book.setDispoBook(rs.getString("dispo_book"));
                book.setDescription(rs.getString("description"));
                book.setPdfFile(rs.getString("pdf_file"));
                book.setFileId(rs.getString("file_id"));
                book.setPicture(rs.getString("picture"));
                books.add(book);
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return books;
    }

    /**
     * Met à jour un book existant dans la table "book".
     * @param book L'objet Book à mettre à jour (doit contenir l'id).
     * @return true si la mise à jour est réussie.
     */
    public boolean updateBook(Book book) {
        Connection connection = SQLConnection.getInstance().getConnection();
        String query = "UPDATE book SET id_book = ?, nom_book = ?, cat_book = ?, dispo_book = ?, description = ?, pdf_file = ?, file_id = ?, picture = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, book.getIdBook());
            ps.setString(2, book.getNomBook());
            ps.setInt(3, book.getCatBook());
            ps.setString(4, book.getDispoBook());
            ps.setString(5, book.getDescription());
            ps.setString(6, book.getPdfFile());
            ps.setString(7, book.getFileId());
            ps.setString(8, book.getPicture());
            ps.setInt(9, book.getId());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Supprime un book de la table "book" en fonction de son id.
     * @param id L'id du book à supprimer.
     * @return true si la suppression est réussie.
     */
    public boolean deleteBook(int id) {
        Connection connection = SQLConnection.getInstance().getConnection();
        String query = "DELETE FROM book WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
