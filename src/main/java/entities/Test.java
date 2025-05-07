package entities;

public class Test {
    private int id;
    private int formationId;

    public Test() {
    }

    public Test(int formationId) {
        this.formationId = formationId;
    }

    public Test(int id, int formationId) {
        this.id = id;
        this.formationId = formationId;
    }

    public Test(int id, int formationId, String nom) {
        this.id = id;
        this.formationId = formationId;
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


}