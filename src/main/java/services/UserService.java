package services;

import entities.Role;
import entities.User;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {

    private Connection connection;
    private BCryptPasswordEncoder passwordEncoder;

    public UserService() {
        connection = MyDatabase.getInstance().getCnx();
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public Connection getConnection() {
        return connection;
    }

    public void ajouter(User user) throws SQLException {
        // Hacher le mot de passe avec BCryptPasswordEncoder
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        // 1. Insérer l'utilisateur
        String query = "INSERT INTO user (email, password, nom, prenom) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getEmail());
            pst.setString(2, hashedPassword);
            pst.setString(3, user.getNom());
            pst.setString(4, user.getPrenom());
            pst.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        }

        // 2. Insérer les rôles de l'utilisateur
        for (Role role : user.getRoles()) {
            ajouterRole(user.getId(), role);
        }
    }

    private void ajouterRole(int userId, Role role) throws SQLException {
        String query = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, userId);
            pst.setInt(2, role.getId());
            pst.executeUpdate();
        }
    }

    public List<User> recuperer() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                User user = new User(
                        id,
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        recupererRoles(id)
                );
                users.add(user);
            }
        }
        return users;
    }

    public User recupererParId(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            id,
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            recupererRoles(id)
                    );
                }
            }
        }
        return null;
    }

    public User authentifier(String email, String password) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    // Vérifier le mot de passe avec BCryptPasswordEncoder
                    if (passwordEncoder.matches(password, storedHash)) {
                        int id = rs.getInt("id");
                        return new User(
                                id,
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("nom"),
                                rs.getString("prenom"),
                                recupererRoles(id)
                        );
                    }
                }
            }
        }
        return null;
    }

    private List<Role> recupererRoles(int userId) throws SQLException {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT role_id FROM user_role WHERE user_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int roleId = rs.getInt("role_id");
                    roles.add(Role.fromId(roleId));
                }
            }
        }
        return roles;
    }

    public void modifier(User user) throws SQLException {
        // Hacher le mot de passe
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        // 1. Mettre à jour les informations de l'utilisateur
        String query = "UPDATE user SET email = ?, password = ?, nom = ?, prenom = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, user.getEmail());
            pst.setString(2, hashedPassword);
            pst.setString(3, user.getNom());
            pst.setString(4, user.getPrenom());
            pst.setInt(5, user.getId());
            pst.executeUpdate();
        }

        // 2. Supprimer tous les rôles existants
        String deleteQuery = "DELETE FROM user_role WHERE user_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(deleteQuery)) {
            pst.setInt(1, user.getId());
            pst.executeUpdate();
        }

        // 3. Ajouter les nouveaux rôles
        for (Role role : user.getRoles()) {
            ajouterRole(user.getId(), role);
        }
    }

    public void supprimer(int id) throws SQLException {
        // 1. Supprimer les rôles associés
        String deleteRolesQuery = "DELETE FROM user_role WHERE user_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(deleteRolesQuery)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }

        // 2. Supprimer l'utilisateur
        String deleteUserQuery = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(deleteUserQuery)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}