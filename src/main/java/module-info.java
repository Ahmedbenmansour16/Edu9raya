module org.example.demoz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;


    opens controller to javafx.fxml;
    opens model to javafx.base, javafx.fxml;
    opens org.example.demoz to javafx.fxml;

    exports org.example.demoz;
    exports model;
}
