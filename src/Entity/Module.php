<?php

namespace App\Entity;

use App\Repository\ModuleRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ModuleRepository::class)]
class Module
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(type: Types::TEXT)]
    private ?string $nom = null;

    #[ORM\Column(type: Types::TEXT)]
    private ?string $Enseigant = null;

    #[ORM\Column(length: 255)]
    private ?string $Duree = null;

    #[ORM\Column(length: 10)]
    private ?string $Coefficient = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): static
    {
        $this->nom = $nom;

        return $this;
    }

    public function getEnseigant(): ?string
    {
        return $this->Enseigant;
    }

    public function setEnseigant(string $Enseigant): static
    {
        $this->Enseigant = $Enseigant;

        return $this;
    }

    public function getDuree(): ?string
    {
        return $this->Duree;
    }

    public function setDuree(string $Duree): static
    {
        $this->Duree = $Duree;

        return $this;
    }

    public function getCoefficient(): ?string
    {
        return $this->Coefficient;
    }

    public function setCoefficient(string $Coefficient): static
    {
        $this->Coefficient = $Coefficient;

        return $this;
    }
}
