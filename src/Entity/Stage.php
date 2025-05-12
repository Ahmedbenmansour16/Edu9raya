<?php
namespace App\Entity;

use App\Entity\CategorieStage;
use App\Repository\StageRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: StageRepository::class)]
#[UniqueEntity(fields: ['titre'], message: 'Ce titre est déjà utilisé.')]
class Stage
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank(message: 'Le titre est obligatoire.')]
    #[Assert\Length(
        min: 3,
        max: 255,
        minMessage: 'Le titre doit contenir au moins {{ limit }} caractères.',
        maxMessage: 'Le titre ne doit pas dépasser {{ limit }} caractères.'
    )]
    private ?string $titre = null;

    #[ORM\Column(type: 'text', nullable: true)]
    private ?string $description = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank(message: 'L\'entreprise est obligatoire.')]
    #[Assert\Length(
        min: 2,
        max: 255,
        minMessage: 'L\'entreprise doit contenir au moins {{ limit }} caractères.',
        maxMessage: 'L\'entreprise ne doit pas dépasser {{ limit }} caractères.'
    )]
    private ?string $entreprise = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank(message: 'Le lieu est obligatoire.')]
    #[Assert\Length(
        min: 2,
        max: 255,
        minMessage: 'Le lieu doit contenir au moins {{ limit }} caractères.',
        maxMessage: 'Le lieu ne doit pas dépasser {{ limit }} caractères.'
    )]
    private ?string $lieu = null;

    #[ORM\Column(type: 'integer')]
    #[Assert\NotBlank(message: 'La durée est obligatoire.')]
    #[Assert\Range(
        min: 1,
        max: 12,
        notInRangeMessage: 'La durée doit être entre {{ min }} et {{ max }} mois.'
    )]
    private ?int $duree = null;

    #[ORM\Column(type: 'date')]
    #[Assert\NotBlank(message: 'La date de début est obligatoire.')]
    private ?\DateTimeInterface $date_debut = null;

    #[ORM\ManyToOne(targetEntity: CategorieStage::class)]
    #[ORM\JoinColumn(nullable: false)]
    #[Assert\NotBlank(message: 'La catégorie est obligatoire.')]
    private ?CategorieStage $categorie = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getTitre(): ?string
    {
        return $this->titre;
    }

    public function setTitre(string $titre): self
    {
        $this->titre = $titre;
        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(?string $description): self
    {
        $this->description = $description;
        return $this;
    }

    public function getEntreprise(): ?string
    {
        return $this->entreprise;
    }

    public function setEntreprise(string $entreprise): self
    {
        $this->entreprise = $entreprise;
        return $this;
    }

    public function getLieu(): ?string
    {
        return $this->lieu;
    }

    public function setLieu(string $lieu): self
    {
        $this->lieu = $lieu;
        return $this;
    }

    public function getDuree(): ?int
    {
        return $this->duree;
    }

    public function setDuree(int $duree): self
    {
        $this->duree = $duree;
        return $this;
    }

    public function getDateDebut(): ?\DateTimeInterface
    {
        return $this->date_debut;
    }

    public function setDateDebut(\DateTimeInterface $date_debut): self
    {
        $this->date_debut = $date_debut;
        return $this;
    }

    public function getCategorie(): ?CategorieStage
    {
        return $this->categorie;
    }

    public function setCategorie(?CategorieStage $categorie): self
    {
        $this->categorie = $categorie;
        return $this;
    }

    public function __toString(): string
    {
        return $this->titre ?? '';
    }
}