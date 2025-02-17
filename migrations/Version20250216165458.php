<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250216165458 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE cour ADD module_id INT NOT NULL');
        $this->addSql('ALTER TABLE cour ADD CONSTRAINT FK_A71F964FAFC2B591 FOREIGN KEY (module_id) REFERENCES module (id)');
        $this->addSql('CREATE INDEX IDX_A71F964FAFC2B591 ON cour (module_id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE cour DROP FOREIGN KEY FK_A71F964FAFC2B591');
        $this->addSql('DROP INDEX IDX_A71F964FAFC2B591 ON cour');
        $this->addSql('ALTER TABLE cour DROP module_id');
    }
}
