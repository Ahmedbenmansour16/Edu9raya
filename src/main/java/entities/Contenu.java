package entities;

public class Contenu {
    private int id;
    private int niveauId;
    private String type;        // (vidéo upload, vidéo YouTube, PDF, image, description)
    private String fichier;     // fichier local ou PDF
    private String description; // texte libre
    private String youtubeId;   // si c’est une vidéo YouTube

    public Contenu() {
    }

    public Contenu(int niveauId, String type, String fichier, String description, String youtubeId) {
        this.niveauId = niveauId;
        this.type = type;
        this.fichier = fichier;
        this.description = description;
        this.youtubeId = youtubeId;
    }

    public Contenu(int id, int niveauId, String type, String fichier, String description, String youtubeId) {
        this.id = id;
        this.niveauId = niveauId;
        this.type = type;
        this.fichier = fichier;
        this.description = description;
        this.youtubeId = youtubeId;
    }

    public int getId() {
        return id;
    }

    public int getNiveauId() {
        return niveauId;
    }

    public void setNiveauId(int niveauId) {
        this.niveauId = niveauId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }
}
