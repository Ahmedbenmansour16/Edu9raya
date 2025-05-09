package entities;

public class CategorieStage {
    private int id;
    private String nom;
    private String description;

    public CategorieStage(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    public CategorieStage(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public CategorieStage() {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}