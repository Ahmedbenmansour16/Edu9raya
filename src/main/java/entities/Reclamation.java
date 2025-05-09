package entities;

import java.time.LocalDateTime;

public class Reclamation {
    private int id;
    private int userId;
    private String sujet;
    private String justification;
    private String imagePath;
    private LocalDateTime dateEnvoi;
    private String statut;
    private String adminResponse;

    public Reclamation() {
    }

    public Reclamation(int userId, String sujet, String justification, String imagePath, LocalDateTime dateEnvoi, String statut) {
        this.userId = userId;
        this.sujet = sujet;
        this.justification = justification;
        this.imagePath = imagePath;
        this.dateEnvoi = dateEnvoi;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", userId=" + userId +
                ", sujet='" + sujet + '\'' +
                ", justification='" + justification + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", statut='" + statut + '\'' +
                ", adminResponse='" + adminResponse + '\'' +
                '}';
    }
}