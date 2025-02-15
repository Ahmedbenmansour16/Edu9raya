<?php

namespace App\Entity;

use App\Repository\CourRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CourRepository::class)]
class Cour
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
private ?string $code_cours = null;

    #[ORM\Column(type: Types::TEXT)]
    private ?string $Titre = null;

    #[ORM\Column(type: Types::TEXT)]
    private ?string $Niveau = null;

    #[ORM\Column(type: Types::TEXT)]
    private ?string $Categorie = null;

    public function getCodeCours(): ?string
{
    return $this->code_cours;
}

public function setCodeCours(string $code_cours): static
{
    $this->code_cours = $code_cours;

    return $this;
}


    public function getTitre(): ?string
    {
        return $this->Titre;
    }

    public function setTitre(string $Titre): static
    {
        $this->Titre = $Titre;

        return $this;
    }

    public function getNiveau(): ?string
    {
        return $this->Niveau;
    }

    public function setNiveau(string $Niveau): static
    {
        $this->Niveau = $Niveau;

        return $this;
    }

    public function getCategorie(): ?string
    {
        return $this->Categorie;
    }

    public function setCategorie(string $Categorie): static
    {
        $this->Categorie = $Categorie;

        return $this;
    }
}
