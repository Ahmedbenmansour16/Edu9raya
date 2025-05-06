package services;

import java.sql.SQLException;
import java.util.List;

public interface Service<T> {
    // On précise que l'ajout renvoie un int (ID généré ou -1 en cas d'erreur)
    int ajouter(T t) throws SQLException;

    void modifier(T t) throws SQLException;

    void supprimer(T t) throws SQLException;

    List<T> recuperer() throws SQLException;
}
