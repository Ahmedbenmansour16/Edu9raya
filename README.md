Edu9raya

Une plateforme éducative complète développée pour la gestion des formations, des réclamations, des catégories, des cours, des modules, d'une bibliothèque, des événements et des stages, dans le cadre du cours PIDEV 3A à Esprit School of Engineering.

Description

Edu9raya est une application web construite avec Symfony 6.4 et JavaFX, conçue pour simplifier les processus de gestion éducative. Développé dans le cadre du cours PIDEV 3A à Esprit School of Engineering, ce projet vise à offrir une plateforme réactive et collaborative pour les administrateurs, les éducateurs et les étudiants. Les principales fonctionnalités incluent :


- Gestion des formations : Création, modification et gestion des formations éducatives avec catégories.

- Système de réclamations : Gestion efficace des plaintes des utilisateurs via une interface administrateur.

- Authentification des utilisateurs : Système de connexion sécurisé avec gestion des rôles.

- Gestion des cours et modules : Ajout et organisation des cours et modules éducatifs.

- Gestion de bibliothèque : Ajout de livres, lecture en ligne et numérisation de QR codes.

- Gestion des événements : Organisation et gestion des événements éducatifs.

- Gestion des stages : Administration des opportunités de stages pour les étudiants.


Table des Matières

- Installation

- Utilisation

- Fonctionnalités

- Technologies Utilisées

- Structure des Répertoires

- Contributions

- Licence

- Remerciements


Installation

Suivez ces étapes pour configurer le projet Edu9raya en local :

1) Cloner le dépôt :
  git clone https://github.com/Ahmedbenmansour16/edu9raya.git
   cd edu9raya

2) Configurer le backend (Symfony) :

- Assurez-vous d’avoir PHP 8.1+ et Composer installés.

- Si vous utilisez WAMP ou XAMPP :

   + Placez le projet dans le dossier www (WAMP) ou htdocs (XAMPP).

   + Démarrez Apache et MySQL depuis l’interface de WAMP/XAMPP.

- Installez les dépendances :
  composer install

  - Configurez la base de données dans le fichier .env :
  DATABASE_URL="mysql://utilisateur:motdepasse@127.0.0.1:3306/piedu9raya"

- Exécutez les migrations pour configurer la base de données :
 php bin/console doctrine:migrations:migrate
- Démarrez le serveur Symfony :
 symfony server:start
- Accédez au backend via http://localhost:8000.

3) Configurer le frontend (JavaFX) :

- Assurez-vous d’avoir Java 17+ et Maven installés.

- Naviguez vers le module JavaFX (si séparé dans la structure du projet) :
  cd C:\Users\monta\OneDrive\Bureau\Edu9raya-java>

- Compilez et exécutez l’application JavaFX :

mvn clean install
mvn javafx:run

Utilisation

Pour les Administrateurs

- Connexion : Utilisez vos identifiants administrateur pour accéder au tableau de bord.

- Gestion des formations : Rendez-vous dans la section "Formations" pour ajouter, modifier ou supprimer des formations et catégories.

- Gestion des réclamations : Consultez la section "Réclamations" pour voir et répondre aux plaintes des utilisateurs.

- Gestion des cours et modules : Ajoutez des cours et modules via les sections "Cours" et "Modules".

- Gestion de la bibliothèque : Ajoutez des livres, activez la lecture en ligne ou générez des QR codes pour un accès rapide.

- Gestion des événements et stages : Organisez des événements et stages via leurs sections respectives.

Pour les Utilisateurs

- Authentification : Inscrivez-vous ou connectez-vous pour accéder aux fonctionnalités personnalisées.

- Navigation : Explorez les formations, cours, livres, événements et opportunités de stages.

- Soumission de réclamations : Signalez des problèmes via le système de réclamations.

Fonctionnalités

- Gestion des formations : Création et catégorisation des formations éducatives avec descriptions détaillées et images.

- Gestion des réclamations : Système permettant aux utilisateurs de soumettre des plaintes, avec une interface admin pour les gérer.

- Authentification des utilisateurs : Système de connexion sécurisé avec gestion des rôles (admin, utilisateur).

- Gestion des cours et modules : Organisation du contenu éducatif en cours et modules.

- Système de bibliothèque : Ajout de livres, lecture en ligne et numérisation de QR codes pour un accès rapide.

- Gestion des événements : Planification et gestion des événements éducatifs.

- Gestion des stages : Facilitation des opportunités de stages pour les étudiants.

- Design réactif : Assure une bonne expérience utilisateur sur différents appareils.


Technologies Utilisées

Frontend

- JavaFX : Pour la construction de l’interface de l’application desktop.

- CSS : Styles personnalisés pour les composants JavaFX.

Backend

- Symfony 6.4 : Framework PHP pour l’API backend et l’interface web.

- MySQL : Base de données pour stocker les formations, utilisateurs, livres, événements, etc.

Autres Outils

- Maven : Gestion des dépendances pour le module JavaFX.

- Composer : Gestion des dépendances pour Symfony.

- Git : Contrôle de version.

  Structure des Répertoires

  edu9raya/
├── public/                 # Répertoire public de Symfony (ressources web)
├── src/                    # Code source de Symfony
│   ├── Controller/         # Contrôleurs Symfony
│   ├── Entity/             # Entités Doctrine (Formation, Categorie, etc.)
│   ├── Service/            # Services de logique métier
│   └── Repository/         # Répertoires Doctrine
├── javafx-module/          # Module frontend JavaFX
│   ├── src/main/java/      # Code source JavaFX
│   └── src/main/resources/ # Fichiers FXML et styles
├── migrations/             # Migrations de la base de données
├── .env                    # Configuration de l’environnement
└── README.md               # Documentation du projet


Contributions

Nous accueillons les contributions pour améliorer Edu9raya ! Suivez ces étapes pour contribuer :

1) Fork le projet : Cliquez sur le bouton "Fork" sur la page du dépôt GitHub.

2) Clonez votre fork :

git clone https://github.com/Ahmedbenmansour16/edu9raya.git
cd edu9raya

3) Créez une nouvelle branche :
   git checkout -b fonctionnalite/java

4) Effectuez vos modifications et validez :
   git add .
   git commit -m "Ajout de votre fonctionnalité"

5) Poussez vers votre fork :
   git push origin fonctionnalite/java

6) Soumettez une pull request : Rendez-vous sur le dépôt original et créez une pull request.

Contributeurs


Montassar kaabi - Développement initial, gestion des formations et système de réclamations, authentification des utilisateurs.

khalil haouari - Gestion des cours, modules.

ahmed ben mansour - Gestion des bibliothèques.

rania regai - gestion des stages

yacine chehata - Gestion des événements


Remerciements

Ce projet a été développé dans le cadre du cours PIDEV 3A à Esprit School of Engineering. Un grand merci aux professeurs d’Esprit School of Engineering pour leur soutien et leurs ressources.
