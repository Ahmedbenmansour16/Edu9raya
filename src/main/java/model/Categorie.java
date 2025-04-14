package model;

public class Categorie {
    private int id;
    private String idCat;
    private String nomCat;

    public Categorie() {}

    public Categorie(int id, String idCat, String nomCat) {
        this.id = id;
        this.idCat = idCat;
        this.nomCat = nomCat;
    }

    // Getters et setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getIdCat() {
        return idCat;
    }
    public void setIdCat(String idCat) {
        this.idCat = idCat;
    }

    public String getNomCat() {
        return nomCat;
    }
    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", idCat='" + idCat + '\'' +
                ", nomCat='" + nomCat + '\'' +
                '}';
    }
}
