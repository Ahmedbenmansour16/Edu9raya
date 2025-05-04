package test;

import services.CategorieService;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        CategorieService cs = new CategorieService();

        try {
//            cs.modifier(new Personne(1,26, "Ben Foulen", "Foulen"));
            System.out.println(cs.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}
