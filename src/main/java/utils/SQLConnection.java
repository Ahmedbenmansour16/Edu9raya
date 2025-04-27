package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    private static SQLConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/edu9raya";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private SQLConnection() {
        connect();
    }

    private void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    /**
     * Retourne une connexion valide (ouvre une nouvelle connexion si la précédente est fermée).
     */
    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection validity: " + e.getMessage());
            connect();
        }
        return connection;
    }

    public static synchronized SQLConnection getInstance() {
        if (instance == null) {
            instance = new SQLConnection();
        }
        return instance;
    }
}
