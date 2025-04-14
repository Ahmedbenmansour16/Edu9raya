module org.example.demoz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    // Ouvrir également votre contrôleur pour le FXML (si nécessaire)
    opens model to javafx.base, javafx.fxml;

    opens org.example.demoz to javafx.fxml;
    opens controller to javafx.fxml;
    exports org.example.demoz;
    exports model;

}