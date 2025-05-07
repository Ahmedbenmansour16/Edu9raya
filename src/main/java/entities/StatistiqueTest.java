package entities;

public class StatistiqueTest {
    private int testId;
    private String testNom;
    private String formationNom; // Add this field to store the formation name
    private double scoreMaximum;
    private double scoreMoyen;
    private int nombreParticipants;
    private User meilleurEtudiant;

    public StatistiqueTest() {
    }

    public StatistiqueTest(int testId, String testNom, double scoreMaximum, double scoreMoyen, int nombreParticipants) {
        this.testId = testId;
        this.testNom = testNom;
        this.scoreMaximum = scoreMaximum;
        this.scoreMoyen = scoreMoyen;
        this.nombreParticipants = nombreParticipants;
    }

    // Add getter and setter for formationNom
    public String getFormationNom() {
        return formationNom;
    }

    public void setFormationNom(String formationNom) {
        this.formationNom = formationNom;
    }

    // Existing getters and setters
    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getTestNom() {
        return testNom;
    }

    public void setTestNom(String testNom) {
        this.testNom = testNom;
    }

    public double getScoreMaximum() {
        return scoreMaximum;
    }

    public void setScoreMaximum(double scoreMaximum) {
        this.scoreMaximum = scoreMaximum;
    }

    public double getScoreMoyen() {
        return scoreMoyen;
    }

    public void setScoreMoyen(double scoreMoyen) {
        this.scoreMoyen = scoreMoyen;
    }

    public int getNombreParticipants() {
        return nombreParticipants;
    }

    public void setNombreParticipants(int nombreParticipants) {
        this.nombreParticipants = nombreParticipants;
    }

    public User getMeilleurEtudiant() {
        return meilleurEtudiant;
    }

    public void setMeilleurEtudiant(User meilleurEtudiant) {
        this.meilleurEtudiant = meilleurEtudiant;
    }
}