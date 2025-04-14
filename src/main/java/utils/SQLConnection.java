package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    // Static instance of the singleton
    private static SQLConnection instance;

    // The Connection object
    private Connection connection;

    // Constants for database connection
    private static final String URL = "jdbc:mysql://localhost:3306/edu9raya";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Private constructor to prevent external instantiation
    private SQLConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    // Provides global access to the single instance
    public static synchronized SQLConnection getInstance() {
        if (instance == null) {
            instance = new SQLConnection();
        }
        return instance;
    }

    // Returns the established connection
    public Connection getConnection() {
        return connection;
    }
}
