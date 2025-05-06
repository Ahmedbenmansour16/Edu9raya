package entities;

public class Module {

    private int id;
    private String nom;
    private String enseignant;
    private int duree; // Duration in hours
    private String coefficient;
    private String image; // New attribute for image path

    // Constructors
    public Module() {
    }

    public Module(String nom, String enseignant, int duree, String coefficient, String image) {
        this.nom = nom;
        this.enseignant = enseignant;
        this.duree = duree;
        this.coefficient = coefficient;
        this.image = image;
    }

    public Module(int id, String nom, String enseignant, int duree, String coefficient, String image) {
        this.id = id;
        this.nom = nom;
        this.enseignant = enseignant;
        this.duree = duree;
        this.coefficient = coefficient;
        this.image = image;
    }

    public Module(int id) {
        this.id = id;
    }

    // Getters and Setters
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

    public String getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(String enseignant) {
        this.enseignant = enseignant;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(String coefficient) {
        this.coefficient = coefficient;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", enseignant='" + enseignant + '\'' +
                ", duree=" + duree + " hours" +
                ", coefficient='" + coefficient + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
} 