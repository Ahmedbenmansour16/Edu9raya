package controllers;

import entities.User;

/**
 * Classe singleton pour gérer la session utilisateur
 */
public class SessionController {
    private static SessionController instance;
    private User currentUser;

    private SessionController() {
        // Constructeur privé
    }

    public static SessionController getInstance() {
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}