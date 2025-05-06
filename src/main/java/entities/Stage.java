package entities;

import java.util.Date;

public class Stage {
    private int id;
    private String titre;
    private String description;
    private String entreprise;
    private String lieu;
    private int duree;
    private Date dateDebut;
    private int categorieId;

    public Stage(int id, String titre, String description, String entreprise, String lieu, int duree, Date dateDebut, int categorieId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.entreprise = entreprise;
        this.lieu = lieu;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.categorieId = categorieId;
    }

    public Stage(String titre, String description, String entreprise, String lieu, int duree, Date dateDebut, int categorieId) {
        this.titre = titre;
        this.description = description;
        this.entreprise = entreprise;
        this.lieu = lieu;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.categorieId = categorieId;
    }

    public Stage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }
    @Override
    public String toString() {
        return "Stage{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", entreprise='" + entreprise + '\'' +
                ", lieu='" + lieu + '\'' +
                ", duree=" + duree +
                ", dateDebut=" + dateDebut +
                ", categorieId=" + categorieId +
                '}';

    }
}