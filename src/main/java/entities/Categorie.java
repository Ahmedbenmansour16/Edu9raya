package entities;

public class Categorie {
    private int id;
    private String nom;

    // Constructeur sans paramètre (requis par certains frameworks ou pour la sérialisation)
    public Categorie() {
    }

    // Constructeur avec le nom de la catégorie
    public Categorie(String nom) {
        this.nom = nom;
    }

    // Constructeur avec id et nom (si vous utilisez un id généré par exemple)
    public Categorie(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Categorie [id=" + id + ", nom=" + nom + "]";
    }
}