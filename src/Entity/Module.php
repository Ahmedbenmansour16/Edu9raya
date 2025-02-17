<?php

namespace App\Entity;

use App\Repository\ModuleRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ModuleRepository::class)]
class Module
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Assert\NotBlank(message: "Le nom du module ne peut pas être vide.")]
    #[Assert\Length(min: 3, max: 255, minMessage: "Le nom doit contenir au moins 3 caractères.")]
    private ?string $nom = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Assert\NotBlank(message: "Le nom de l'enseignant est requis.")]
    #[Assert\Length(min: 3, max: 255, minMessage: "Le nom de l'enseignant doit contenir au moins 3 caractères.")]
    private ?string $Enseigant = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "La durée est requise.")]
    #[Assert\Regex(pattern: "/^\d+h$/", message: "La durée doit être au format 'Xh' (ex: 30h).")]
    private ?string $Duree = null;

    #[ORM\Column(length: 10)]
    #[Assert\NotBlank(message: "Le coefficient est requis.")]
    #[Assert\Type(type: "numeric", message: "Le coefficient doit être un nombre.")]
    #[Assert\Range(min: 1, max: 10, notInRangeMessage: "Le coefficient doit être entre 1 et 10.")]
    private ?string $Coefficient = null;

    #[ORM\OneToMany(mappedBy: "module", targetEntity: Cour::class, cascade: ["persist", "remove"])]
    private Collection $cours;

    public function __construct()
    {
        $this->cours = new ArrayCollection();
    }

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

    public function getCours(): Collection
    {
        return $this->cours;
    }

    public function addCour(Cour $cour): static
    {
        if (!$this->cours->contains($cour)) {
            $this->cours->add($cour);
            $cour->setModule($this);
        }
        return $this;
    }

    public function removeCour(Cour $cour): static
    {
        if ($this->cours->removeElement($cour)) {
            if ($cour->getModule() === $this) {
                $cour->setModule(null);
            }
        }
        return $this;
    }
}
