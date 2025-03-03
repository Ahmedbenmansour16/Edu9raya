<?php
namespace App\Entity;

use App\Repository\EtudiantRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;

#[ORM\Entity(repositoryClass: EtudiantRepository::class)]
class Etudiant
{
    #[ORM\Id]
    #[ORM\OneToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(nullable: false)]
    private User $user;

    #[ORM\Column(length: 10)]
    private ?string $nom = null;

    #[ORM\Column(length: 10)]
    private ?string $prenom = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    private ?\DateTimeInterface $date_naissance = null;

    #[ORM\Column]
    private ?int $annee = null;

    #[ORM\Column(length: 255)]
    private ?string $specialisation = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $telephone = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $adresse = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $photo = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    private ?\DateTimeInterface $date_inscription = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $statut = null;

    #[ORM\Column]
    private ?float $moyenne = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $cours = null;

    #[ORM\Column(length: 255)]
    private ?string $sexe = null;

    #[ORM\Column(length: 255)]
    private ?string $cin = null;

    #[ORM\Column(nullable: true)]
    private ?bool $etat_paiement = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $linkdin = null; // Propriété renommée en "linkedin"

    public function getId(): ?int
    {
        return $this->user->getId();
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

    public function getDateNaissance(): ?\DateTimeInterface
    {
        return $this->date_naissance;
    }

    public function setDateNaissance(\DateTimeInterface $date_naissance): static
    {
        $this->date_naissance = $date_naissance;
        return $this;
    }

    public function getAnnee(): ?int
    {
        return $this->annee;
    }

    public function setAnnee(int $annee): static
    {
        $this->annee = $annee;
        return $this;
    }

    public function getSpecialisation(): ?string
    {
        return $this->specialisation;
    }

    public function setSpecialisation(string $specialisation): static
    {
        $this->specialisation = $specialisation;
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

    public function getAdresse(): ?string
    {
        return $this->adresse;
    }

    public function setAdresse(?string $adresse): static
    {
        $this->adresse = $adresse;
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

    public function getDateInscription(): ?\DateTimeInterface
    {
        return $this->date_inscription;
    }

    public function setDateInscription(\DateTimeInterface $date_inscription): static
    {
        $this->date_inscription = $date_inscription;
        return $this;
    }

    public function getStatut(): ?string
    {
        return $this->statut;
    }

    public function setStatut(?string $statut): static
    {
        $this->statut = $statut;
        return $this;
    }

    public function getMoyenne(): ?float
    {
        return $this->moyenne;
    }

    public function setMoyenne(float $moyenne): static
    {
        $this->moyenne = $moyenne;
        return $this;
    }

    public function getCours(): ?string
    {
        return $this->cours;
    }

    public function setCours(?string $cours): static
    {
        $this->cours = $cours;
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

    public function getCin(): ?string
    {
        return $this->cin;
    }

    public function setCin(string $cin): static
    {
        $this->cin = $cin;
        return $this;
    }

    public function getEtatPaiement(): ?bool
    {
        return $this->etat_paiement;
    }

    public function setEtatPaiement(?bool $etat_paiement): static
    {
        $this->etat_paiement = $etat_paiement;
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

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(User $user): static
    {
        $this->user = $user;
        return $this;
    }
}