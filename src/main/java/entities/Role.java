package entities;

public enum Role {
    ADMIN(1, "Administrateur"),
    ENSEIGNANT(2, "Enseignant"),
    ETUDIANT(3, "Étudiant");

    private final int id;
    private final String libelle;

    Role(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public int getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public static Role fromId(int id) {
        for (Role role : values()) {
            if (role.id == id) {
                return role;
            }
        }
        return ETUDIANT; // Par défaut
    }

    @Override
    public String toString() {
        return libelle;
    }
}