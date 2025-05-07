<?php

namespace App\Entity;

use App\Repository\ReclamationRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ReclamationRepository::class)]
class Reclamation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank(message: "Le sujet est obligatoire")]
    private ?string $sujet = null;

    #[ORM\Column(type: 'text')]
    #[Assert\NotBlank(message: "La justification est obligatoire")]
    #[Assert\Length(min: 10, minMessage: "La justification doit faire au moins {{ limit }} caractères")]
    private ?string $justification = null;

    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private ?string $imagePath = null;

    #[ORM\Column(type: 'datetime')]
    private ?\DateTime $dateEnvoi = null;

    #[ORM\Column(type: 'string', length: 50)]
    private ?string $statut = 'en_attente';

    #[ORM\ManyToOne(inversedBy: 'reclamations')]
    #[ORM\JoinColumn(nullable: false)]
    private ?User $user = null;

    #[ORM\Column(type: 'text', nullable: true)]
    private ?string $adminReponse = null;

    public function __construct()
    {
        $this->dateEnvoi = new \DateTime();
    }

    public function getId(): ?int { return $this->id; }

    public function getSujet(): ?string { return $this->sujet; }
    public function setSujet(string $sujet): self { $this->sujet = $sujet; return $this; }

    public function getJustification(): ?string { return $this->justification; }
    public function setJustification(string $justification): self { $this->justification = $justification; return $this; }

    public function getImagePath(): ?string { return $this->imagePath; }
    public function setImagePath(?string $imagePath): self { $this->imagePath = $imagePath; return $this; }

    public function getDateEnvoi(): ?\DateTime { return $this->dateEnvoi; }

    public function getStatut(): ?string { return $this->statut; }
    public function setStatut(string $statut): self { $this->statut = $statut; return $this; }

    public function getUser(): ?User { return $this->user; }
    public function setUser(?User $user): self { $this->user = $user; return $this; }

    public function getAdminReponse(): ?string { return $this->adminReponse; }
    public function setAdminReponse(?string $adminReponse): self { $this->adminReponse = $adminReponse; return $this; }
}