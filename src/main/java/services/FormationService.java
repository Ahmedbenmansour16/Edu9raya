package services;

import entities.Formation;
import entities.Categorie;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormationService implements Service<Formation> {

    private Connection cnx;

    public FormationService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public int ajouter(Formation formation) throws SQLException {
        String sql = "INSERT INTO formation(categorie_id, nom, description, image, date_creation) VALUES(?, ?, ?, ?, ?)";
        // On utilise RETURN_GENERATED_KEYS pour récupérer l'ID généré
        PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        // On récupère l'id de la catégorie sélectionnée
        ps.setInt(1, formation.getCategorie().getId());
        ps.setString(2, formation.getNom());
        ps.setString(3, formation.getDescription());
        ps.setString(4, formation.getImage());
        // Vérifiez que formation.getDateCreation() renvoie bien un LocalDateTime
        ps.setTimestamp(5, Timestamp.valueOf(formation.getDateCreation()));
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()){
            int id = rs.getInt(1);
            System.out.println("Formation ajoutée avec l'ID : " + id);
            return id;
        }
        return -1;
    }

    @Override
    public void modifier(Formation formation) throws SQLException {
        String sql = "UPDATE formation SET categorie_id = ?, nom = ?, description = ?, image = ?, date_creation = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, formation.getCategorie().getId());
        ps.setString(2, formation.getNom());
        ps.setString(3, formation.getDescription());
        ps.setString(4, formation.getImage());
        ps.setTimestamp(5, Timestamp.valueOf(formation.getDateCreation()));
        ps.setInt(6, formation.getId());
        ps.executeUpdate();
        System.out.println("Formation modifiée !");
    }

    @Override
    public void supprimer(Formation formation) throws SQLException {
        String sql = "DELETE FROM formation WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, formation.getId());
        ps.executeUpdate();
        System.out.println("Formation supprimée !");
    }

    @Override
    public List<Formation> recuperer() throws SQLException {
        List<Formation> formations = new ArrayList<>();
        // Utiliser une jointure pour récupérer le nom de la catégorie
        String sql = "SELECT f.id, f.categorie_id, c.nom as categorie_nom, f.nom, f.description, f.image, f.date_creation " +
                "FROM formation f INNER JOIN categorie c ON f.categorie_id = c.id";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()){
            int id = rs.getInt("id");
            int categorieId = rs.getInt("categorie_id");
            String categorieNom = rs.getString("categorie_nom");
            String nom = rs.getString("nom");
            String description = rs.getString("description");
            String image = rs.getString("image");
            Timestamp ts = rs.getTimestamp("date_creation");
            java.time.LocalDateTime dateCreation = ts.toLocalDateTime();

            // Création d'un objet Categorie complet
            entities.Categorie categorie = new entities.Categorie();
            categorie.setId(categorieId);
            categorie.setNom(categorieNom);

            Formation formation = new Formation(id, categorie, nom, description, image, dateCreation);
            formations.add(formation);
        }
        return formations;
    }
}