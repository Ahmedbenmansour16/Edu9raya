package entities;

import java.sql.Timestamp;

public class Cour {

    private int id;
    private String codeCours;
    private String titre;
    private String niveau;
    private String categorie;
    private Module module; // Reference to Module (foreign key module_id)
    private String description;
    private String pdfPath;
    private Timestamp updatedAt;

    // Constructors
    public Cour() {
    }

    public Cour(String codeCours, String titre, String niveau, String categorie, Module module, String description, String pdfPath) {
        this.codeCours = codeCours;
        this.titre = titre;
        this.niveau = niveau;
        this.categorie = categorie;
        this.module = module;
        this.description = description;
        this.pdfPath = pdfPath;
    }

    public Cour(int id, String codeCours, String titre, String niveau, String categorie, Module module, String description, String pdfPath, Timestamp updatedAt) {
        this.id = id;
        this.codeCours = codeCours;
        this.titre = titre;
        this.niveau = niveau;
        this.categorie = categorie;
        this.module = module;
        this.description = description;
        this.pdfPath = pdfPath;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeCours() {
        return codeCours;
    }

    public void setCodeCours(String codeCours) {
        this.codeCours = codeCours;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Cour{" +
                "id=" + id +
                ", codeCours='" + codeCours + '\'' +
                ", titre='" + titre + '\'' +
                ", niveau='" + niveau + '\'' +
                ", categorie='" + categorie + '\'' +
                ", moduleId=" + (module != null ? module.getId() : null) +
                ", description='" + description + '\'' +
                ", pdfPath='" + pdfPath + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 