package entities;

import java.time.LocalDate;

public class Certificat {
    private int id;
    private int userId;
    private int testId;
    private LocalDate dateObtention;
    private double score;

    public Certificat() {
    }

    public Certificat(int userId, int testId, LocalDate dateObtention, double score) {
        this.userId = userId;
        this.testId = testId;
        this.dateObtention = dateObtention;
        this.score = score;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getTestId() {
        return testId;
    }

    public LocalDate getDateObtention() {
        return dateObtention;
    }

    public double getScore() {
        return score;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public void setDateObtention(LocalDate dateObtention) {
        this.dateObtention = dateObtention;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Certificat{" +
                "id=" + id +
                ", userId=" + userId +
                ", testId=" + testId +
                ", dateObtention=" + dateObtention +
                ", score=" + score +
                '}';
    }
}