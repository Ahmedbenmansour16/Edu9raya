<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250302155143 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE book DROP FOREIGN KEY FK_CBE5A331D6D65480');
        $this->addSql('ALTER TABLE book ADD CONSTRAINT FK_CBE5A331D6D65480 FOREIGN KEY (cat_book) REFERENCES categoriebook (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE book DROP FOREIGN KEY FK_CBE5A331D6D65480');
        $this->addSql('ALTER TABLE book ADD CONSTRAINT FK_CBE5A331D6D65480 FOREIGN KEY (cat_book) REFERENCES categorie (id)');
    }
}
