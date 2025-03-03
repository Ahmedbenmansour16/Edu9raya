<?php
namespace App\Entity;

use App\Repository\ProfRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ProfRepository::class)]
class Prof
{
    #[ORM\Id]
    #[ORM\OneToOne(targetEntity: User::class, cascade: ['persist', 'remove'])]
    #[ORM\JoinColumn(name: "user_id", referencedColumnName: "id", nullable: false, unique: true, onDelete: "SET NULL")]
    private ?User $user = null;

    #[ORM\Column(length: 20)]
    private ?string $nom = null;

    #[ORM\Column(length: 20)]
    private ?string $prenom = null;

    #[ORM\Column(length: 20)]
    private ?string $cin = null;

    #[ORM\Column(length: 10)]
    private ?string $sexe = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $matiere = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $grade = null;

    #[ORM\Column(type: Types::DATE_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $date_recrutement = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $telephone = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $photo = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    private ?\DateTimeInterface $date_naissance = null;

    #[ORM\Column]
    private ?int $nbr_annees = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $linkdin = null;

    public function getId(): ?int
    {
        return $this->user ? $this->user->getId() : null;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(User $user): static
    {
        $this->user = $user;
        return $this;
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

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): static
    {
        $this->prenom = $prenom;
        return $this;
    }

    public function getCin(): ?string
    {
        return $this->cin;
    }

    public function setCin(string $cin): static
    {
        $this->cin = $cin;
        return $this;
    }

    public function getSexe(): ?string
    {
        return $this->sexe;
    }

    public function setSexe(string $sexe): static
    {
        $this->sexe = $sexe;
        return $this;
    }

    public function getMatiere(): ?string
    {
        return $this->matiere;
    }

    public function setMatiere(?string $matiere): static
    {
        $this->matiere = $matiere;
        return $this;
    }

    public function getGrade(): ?string
    {
        return $this->grade;
    }

    public function setGrade(?string $grade): static
    {
        $this->grade = $grade;
        return $this;
    }

    public function getDateRecrutement(): ?\DateTimeInterface
    {
        return $this->date_recrutement;
    }

    public function setDateRecrutement(?\DateTimeInterface $date_recrutement): static
    {
        $this->date_recrutement = $date_recrutement;
        return $this;
    }

    public function getTelephone(): ?string
    {
        return $this->telephone;
    }

    public function setTelephone(?string $telephone): static
    {
        $this->telephone = $telephone;
        return $this;
    }

    public function getPhoto(): ?string
    {
        return $this->photo;
    }

    public function setPhoto(?string $photo): static
    {
        $this->photo = $photo;
        return $this;
    }

    public function getDateNaissance(): ?\DateTimeInterface
    {
        return $this->date_naissance;
    }

    public function setDateNaissance(\DateTimeInterface $date_naissance): static
    {
        $this->date_naissance = $date_naissance;
        return $this;
    }

    public function getNbrAnnees(): ?int
    {
        return $this->nbr_annees;
    }

    public function setNbrAnnees(int $nbr_annees): static
    {
        $this->nbr_annees = $nbr_annees;
        return $this;
    }

    public function getLinkdin(): ?string
    {
        return $this->linkdin;
    }

    public function setLinkdin(?string $linkdin): static
    {
        $this->linkdin = $linkdin;
        return $this;
    }
}