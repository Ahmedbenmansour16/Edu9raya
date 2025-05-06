package entities;

public class Niveau {
    private int id;
    private int formationId;
    private int ordre; // ex: 1, 2, 3, 4, 5

    public Niveau() {
    }

    public Niveau(int formationId, int ordre) {
        this.formationId = formationId;
        this.ordre = ordre;
    }

    public Niveau(int id, int formationId, int ordre) {
        this.id = id;
        this.formationId = formationId;
        this.ordre = ordre;
    }

    public int getId() {
        return id;
    }

    public int getFormationId() {
        return formationId;
    }

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }
}
