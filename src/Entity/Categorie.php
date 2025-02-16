<?php

namespace App\Entity;

use App\Repository\CategorieRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CategorieRepository::class)]
class Categorie
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $id_cat = null;

    #[ORM\Column(length: 255)]
    private ?string $nom_cat = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getIdCat(): ?string
    {
        return $this->id_cat;
    }

    public function setIdCat(string $id_cat): static
    {
        $this->id_cat = $id_cat;
        return $this;
    }

    public function getNomCat(): ?string
    {
        return $this->nom_cat;
    }

    public function setNomCat(string $nom_cat): static
    {
        $this->nom_cat = $nom_cat;
        return $this;
    }

    // ✅ Add this method to fix the error
    public function __toString(): string
    {
        return $this->nom_cat;  // Return the category name when converted to a string
    }
}
