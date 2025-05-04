package entities;

import java.time.LocalDateTime;

public class Formation {
    private int id;
    private Categorie categorie;
    private String nom;
    private String description;
    private String image;
    private LocalDateTime dateCreation;

    public Formation() {
        this.dateCreation = LocalDateTime.now();
    }

    public Formation(Categorie categorie, String nom, String description, String image) {
        this.categorie = categorie;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.dateCreation = LocalDateTime.now();
    }

    public Formation(int id, Categorie categorie, String nom, String description, String image, LocalDateTime dateCreation) {
        this.id = id;
        this.categorie = categorie;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.dateCreation = dateCreation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", categorie=" + categorie +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }
}