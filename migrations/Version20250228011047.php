<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250228011047 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE etudiant (user_id INT NOT NULL, nom VARCHAR(10) NOT NULL, prenom VARCHAR(10) NOT NULL, date_naissance DATE NOT NULL, annee INT NOT NULL, specialisation VARCHAR(255) NOT NULL, telephone VARCHAR(255) DEFAULT NULL, adresse VARCHAR(255) DEFAULT NULL, photo VARCHAR(255) DEFAULT NULL, date_inscription DATE NOT NULL, statut VARCHAR(255) DEFAULT NULL, moyenne DOUBLE PRECISION NOT NULL, cours VARCHAR(255) DEFAULT NULL, sexe VARCHAR(255) NOT NULL, cin VARCHAR(255) NOT NULL, etat_paiement TINYINT(1) DEFAULT NULL, linkdin VARCHAR(255) DEFAULT NULL, PRIMARY KEY(user_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE prof (user_id INT NOT NULL, nom VARCHAR(20) NOT NULL, prenom VARCHAR(20) NOT NULL, cin VARCHAR(20) NOT NULL, sexe VARCHAR(10) NOT NULL, matiere VARCHAR(255) DEFAULT NULL, grade VARCHAR(255) DEFAULT NULL, date_recrutement DATE DEFAULT NULL, telephone VARCHAR(255) DEFAULT NULL, photo VARCHAR(255) DEFAULT NULL, date_naissance DATE NOT NULL, nbr_annees INT NOT NULL, linkdin VARCHAR(255) DEFAULT NULL, PRIMARY KEY(user_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE user (id INT AUTO_INCREMENT NOT NULL, email VARCHAR(180) NOT NULL, roles JSON NOT NULL, password VARCHAR(255) NOT NULL, UNIQUE INDEX UNIQ_IDENTIFIER_EMAIL (email), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE messenger_messages (id BIGINT AUTO_INCREMENT NOT NULL, body LONGTEXT NOT NULL, headers LONGTEXT NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', available_at DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', delivered_at DATETIME DEFAULT NULL COMMENT \'(DC2Type:datetime_immutable)\', INDEX IDX_75EA56E0FB7336F0 (queue_name), INDEX IDX_75EA56E0E3BD61CE (available_at), INDEX IDX_75EA56E016BA31DB (delivered_at), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE etudiant ADD CONSTRAINT FK_717E22E3A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('ALTER TABLE prof ADD CONSTRAINT FK_5BBA70BBA76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE etudiant DROP FOREIGN KEY FK_717E22E3A76ED395');
        $this->addSql('ALTER TABLE prof DROP FOREIGN KEY FK_5BBA70BBA76ED395');
        $this->addSql('DROP TABLE etudiant');
        $this->addSql('DROP TABLE prof');
        $this->addSql('DROP TABLE user');
        $this->addSql('DROP TABLE messenger_messages');
    }
}
