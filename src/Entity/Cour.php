<?php

namespace App\Entity;

use App\Repository\CourRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: CourRepository::class)]
class Cour
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Le code du cours est requis.")]
    #[Assert\Length(min: 3, max: 255, minMessage: "Le code doit contenir au moins 3 caractères.")]
    private ?string $code_cours = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Assert\NotBlank(message: "Le titre est requis.")]
    #[Assert\Length(min: 3, max: 255, minMessage: "Le titre doit contenir au moins 3 caractères.")]
    private ?string $Titre = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Assert\NotBlank(message: "Le niveau est requis.")]
    #[Assert\Choice(choices: ["Débutant", "Intermédiaire", "Avancé"], message: "Le niveau doit être Débutant, Intermédiaire ou Avancé.")]
    private ?string $Niveau = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Assert\NotBlank(message: "La catégorie est requise.")]
    private ?string $Categorie = null;

    #[ORM\ManyToOne(targetEntity: Module::class, inversedBy: "cours")]
    #[ORM\JoinColumn(nullable: false)]
    private ?Module $module = null;

    public function getId(): ?int
    {
        return $this->id;
    }

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

    public function getModule(): ?Module
    {
        return $this->module;
    }

    public function setModule(?Module $module): static
    {
        $this->module = $module;
        return $this;
    }
}
