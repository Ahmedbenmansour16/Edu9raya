module PI3A64 {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.graphics;
    requires org.apache.pdfbox;

    // Exportez tous vos packages
    exports test;
    exports controllers;
    exports entities;
    exports services;
    exports utils;

    // Ouvrez les packages pour la réflexion FXML
    opens controllers to javafx.fxml;
    opens test to javafx.graphics, javafx.fxml;
}