-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : lun. 03 mars 2025 à 00:53
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `edukraya`
--

-- --------------------------------------------------------

--
-- Structure de la table `doctrine_migration_versions`
--

CREATE TABLE `doctrine_migration_versions` (
  `version` varchar(191) NOT NULL,
  `executed_at` datetime DEFAULT NULL,
  `execution_time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `doctrine_migration_versions`
--

INSERT INTO `doctrine_migration_versions` (`version`, `executed_at`, `execution_time`) VALUES
('DoctrineMigrations\\Version20250228011047', '2025-02-28 02:11:10', 1137);

-- --------------------------------------------------------

--
-- Structure de la table `etudiant`
--

CREATE TABLE `etudiant` (
  `user_id` int(11) NOT NULL,
  `nom` varchar(10) NOT NULL,
  `prenom` varchar(10) NOT NULL,
  `date_naissance` date NOT NULL,
  `annee` int(11) NOT NULL,
  `specialisation` varchar(255) NOT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `date_inscription` date NOT NULL,
  `statut` varchar(255) DEFAULT NULL,
  `moyenne` double NOT NULL,
  `cours` varchar(255) DEFAULT NULL,
  `sexe` varchar(255) NOT NULL,
  `cin` varchar(255) NOT NULL,
  `etat_paiement` tinyint(1) DEFAULT NULL,
  `linkdin` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `etudiant`
--

INSERT INTO `etudiant` (`user_id`, `nom`, `prenom`, `date_naissance`, `annee`, `specialisation`, `telephone`, `adresse`, `photo`, `date_inscription`, `statut`, `moyenne`, `cours`, `sexe`, `cin`, `etat_paiement`, `linkdin`) VALUES
(19, 'aaaaaaaaaa', 'youcef', '2025-03-27', 12, 'adeaz', '29295371', 'tunis,tunis', 'azdza', '2025-03-21', 'azd', 12.1, 'AZDAZ', 'Homme', '&é\"&é', 0, 'DFZEDD'),
(20, 'zfhjh', 'theth', '2025-03-14', 2, 'rfef', '32323', 'refg', 'QQDD', '2025-03-14', 'fef', 23.2, 'eff', 'aed', '1331332', 1, 'ZZS'),
(21, 'hhhhhhhhhh', 'theth', '2025-03-05', 2, 'rfef', '32323', 'refg', 'QQDD', '2025-03-12', 'fef', 23.2, 'eff', 'aed', '1331332', 0, 'ZZS'),
(23, '123', 'youcef', '2025-03-14', 1221, 'SDVSV', '29295371', 'tunis,tunis', 'SDVSD', '2025-03-19', 'AZAZ', 1123, 'EAFE', 'H', 'ZEFZEFZ', 1, 'ZEFZEF');

-- --------------------------------------------------------

--
-- Structure de la table `messenger_messages`
--

CREATE TABLE `messenger_messages` (
  `id` bigint(20) NOT NULL,
  `body` longtext NOT NULL,
  `headers` longtext NOT NULL,
  `queue_name` varchar(190) NOT NULL,
  `created_at` datetime NOT NULL COMMENT '(DC2Type:datetime_immutable)',
  `available_at` datetime NOT NULL COMMENT '(DC2Type:datetime_immutable)',
  `delivered_at` datetime DEFAULT NULL COMMENT '(DC2Type:datetime_immutable)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `prof`
--

CREATE TABLE `prof` (
  `user_id` int(11) NOT NULL,
  `nom` varchar(20) NOT NULL,
  `prenom` varchar(20) NOT NULL,
  `cin` varchar(20) NOT NULL,
  `sexe` varchar(10) NOT NULL,
  `matiere` varchar(255) DEFAULT NULL,
  `grade` varchar(255) DEFAULT NULL,
  `date_recrutement` date DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `date_naissance` date NOT NULL,
  `nbr_annees` int(11) NOT NULL,
  `linkdin` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `prof`
--

INSERT INTO `prof` (`user_id`, `nom`, `prenom`, `cin`, `sexe`, `matiere`, `grade`, `date_recrutement`, `telephone`, `photo`, `date_naissance`, `nbr_annees`, `linkdin`) VALUES
(9, 'kharroubi', 'youcefccc', '1222211', 'h', 'zze', 'zefez', '2025-03-12', '29295371', NULL, '2025-03-11', 12, 'dz'),
(24, 'EFZ', 'ZED', '12', 'E', 'FF', 'FEFE', '2025-03-11', 'EFEF', NULL, '2025-03-10', 112, 'DZ');

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(180) NOT NULL,
  `roles` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`roles`)),
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `email`, `roles`, `password`) VALUES
(1, 'user@example.com', '[\"ROLE_USER\"]', 'password123'),
(3, 'kharroubi.youcef@esprit.tn', '[\"ROLE_ETUDIANT\"]', 'aaaa'),
(6, 'a@ede.com', '[\"ROLE_ADMIN\"]', 'AAZERazz1'),
(9, 'zzzzz@efe.com', '[\"ROLE_PROF\"]', 'AAZZTG265aa'),
(19, 'kharroubi.youvvcef@esprit.tn', '[\"ROLE_ETUDIANT\"]', 'sdvsdQQF12'),
(20, 'khaffrroubi.youcef@esprit.tn', '[\"ROLE_ETUDIANT\"]', 'QSDFZE12az'),
(21, 'aa@aa.aaa', '[\"ROLE_ETUDIANT\"]', 'Azerty1'),
(23, 'kharrEFEoubi.youcef@esprit.tn', '[\"ROLE_ETUDIANT\"]', 'AZZEaee123'),
(24, 'kharrqqaaoubi.youcef@esprit.tn', '[\"ROLE_PROF\"]', 'sdfdAZAZ11');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `doctrine_migration_versions`
--
ALTER TABLE `doctrine_migration_versions`
  ADD PRIMARY KEY (`version`);

--
-- Index pour la table `etudiant`
--
ALTER TABLE `etudiant`
  ADD PRIMARY KEY (`user_id`);

--
-- Index pour la table `messenger_messages`
--
ALTER TABLE `messenger_messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_75EA56E0FB7336F0` (`queue_name`),
  ADD KEY `IDX_75EA56E0E3BD61CE` (`available_at`),
  ADD KEY `IDX_75EA56E016BA31DB` (`delivered_at`);

--
-- Index pour la table `prof`
--
ALTER TABLE `prof`
  ADD PRIMARY KEY (`user_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UNIQ_IDENTIFIER_EMAIL` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `messenger_messages`
--
ALTER TABLE `messenger_messages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `etudiant`
--
ALTER TABLE `etudiant`
  ADD CONSTRAINT `FK_717E22E3A76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `prof`
--
ALTER TABLE `prof`
  ADD CONSTRAINT `FK_5BBA70BBA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
