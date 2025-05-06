package entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String email;
    private String password;
    private String nom;
    private String prenom;
    private List<Role> roles;

    // Constructeur par défaut
    public User() {
        this.roles = new ArrayList<>();
        // Par défaut, ajouter le rôle étudiant
        this.roles.add(Role.ETUDIANT);
    }

    // Constructeur pour la création
    public User(String email, String password, String nom, String prenom) {
        this();
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
    }

    // Constructeur complet
    public User(int id, String email, String password, String nom, String prenom, List<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.roles = roles != null ? roles : new ArrayList<>();
        // Si aucun rôle n'est fourni, ajouter le rôle étudiant par défaut
        if (this.roles.isEmpty()) {
            this.roles.add(Role.ETUDIANT);
        }
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    // Méthodes utilitaires pour les rôles
    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public Role getHighestRole() {
        if (hasRole(Role.ADMIN)) {
            return Role.ADMIN;
        } else if (hasRole(Role.ENSEIGNANT)) {
            return Role.ENSEIGNANT;
        } else {
            return Role.ETUDIANT;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", roles=" + roles +
                '}';
    }
}