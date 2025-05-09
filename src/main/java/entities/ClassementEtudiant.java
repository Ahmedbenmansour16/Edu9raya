package entities;

public class ClassementEtudiant {
    private int rank;
    private User etudiant;
    private double score;
    private int testId;

    public ClassementEtudiant() {
    }

    public ClassementEtudiant(int rank, User etudiant, double score, int testId) {
        this.rank = rank;
        this.etudiant = etudiant;
        this.score = score;
        this.testId = testId;
    }

    // Getters et Setters
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public User getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(User etudiant) {
        this.etudiant = etudiant;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }
}