package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiConfig {
    private static final String CONFIG_FILE = "config.properties";
    private static String baseUrl;
    private static String apiKey;

    static {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(CONFIG_FILE));

            baseUrl = props.getProperty("api.baseUrl", "https://api.edu9raya.com/v1");
            apiKey = props.getProperty("api.key", "");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier de configuration: " + e.getMessage());
            // Valeurs par défaut
            baseUrl = "https://api.edu9raya.com/v1";
            apiKey = "";
        }
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getApiKey() {
        return apiKey;
    }
}