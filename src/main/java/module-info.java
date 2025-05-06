module PI3A64 {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.graphics;
    requires org.apache.pdfbox;
    requires jakarta.mail;
    requires java.naming;

    // Exportez tous vos packages
    exports test;
    exports controllers;
    exports controllers.Cours;
    exports controllers.Modules;
    exports entities;
    exports services;
    exports utils;

    // Ouvrez les packages pour la réflexion FXML
    opens controllers to javafx.fxml;
    opens controllers.Cours to javafx.fxml;
    opens controllers.Modules to javafx.fxml;
    opens test to javafx.graphics, javafx.fxml;
    opens services to jakarta.mail;
}