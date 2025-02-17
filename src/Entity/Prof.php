<?php

namespace App\Entity;

use App\Repository\ProfRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ProfRepository::class)]
class Prof
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 10)]
    private ?string $nom = null;

    #[ORM\Column(length: 10)]
    private ?string $prenom = null;

    #[ORM\Column]
    private ?int $age = null;

    #[ORM\Column(length: 20)]
    private ?string $matiere = null;

    #[ORM\Column(length: 20)]
    private ?string $email = null;

    #[ORM\Column(length: 20)]
    private ?string $adresse = null;

    #[ORM\Column(length: 10)]
    private ?string $role = null;


    public function __construct()
    {
        // Automatically set role to "PROF" for new instances
        $this->role = 'PROF';
    }

    public function getRole(): string
    {
        return $this->role;
    }

    // No setter for role, making it read-only


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

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): static
    {
        $this->prenom = $prenom;

        return $this;
    }

    public function getAge(): ?int
    {
        return $this->age;
    }

    public function setAge(int $age): static
    {
        $this->age = $age;

        return $this;
    }

    public function getMatiere(): ?string
    {
        return $this->matiere;
    }

    public function setMatiere(string $matiere): static
    {
        $this->matiere = $matiere;

        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): static
    {
        $this->email = $email;

        return $this;
    }

    public function getAdresse(): ?string
    {
        return $this->adresse;
    }

    public function setAdresse(string $adresse): static
    {
        $this->adresse = $adresse;

        return $this;
    }

    

    public function setRole(string $role): static
    {
        $this->role = $role;

        return $this;
    }
}
