<?php

namespace App\Entity;

use App\Entity\Categoriebook;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity]
class Book
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "L'ID du livre est obligatoire.")]
    #[Assert\Length(min: 3, max: 20, minMessage: "L'ID doit contenir au moins 3 caractères.")]
    private ?string $id_book = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Le nom du livre est obligatoire.")]
    #[Assert\Length(min: 3, max: 255, minMessage: "Le nom doit contenir au moins 3 caractères.")]
    private ?string $nom_book = null;

    #[ORM\ManyToOne(targetEntity: Categoriebook::class)]
    #[ORM\JoinColumn(name: "cat_book", referencedColumnName: "id", nullable: false)]
    private ?Categoriebook $cat_book = null;
    

    #[ORM\Column(length: 3)]
    #[Assert\NotBlank(message: "La disponibilité est obligatoire.")]
    #[Assert\Choice(choices: ['oui', 'non'], message: "La disponibilité doit être 'oui' ou 'non'.")]
    private ?string $dispo_book = null;

    #[ORM\Column(type: "text", nullable: true)]
    #[Assert\Length(max: 1000, maxMessage: "La description ne peut pas dépasser 1000 caractères.")]
    private ?string $description = null;

    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private ?string $file_id = null;

    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private ?string $pdf_file = null;

    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private ?string $picture = null;

    // ✅ Getters & Setters

    public function getId(): ?int { return $this->id; }
    public function getCategoryName(): ?string
{
    return $this->cat_book ? $this->cat_book->getNomCat() : null;
}

    public function getIdBook(): ?string { return $this->id_book; }
    public function setIdBook(string $id_book): static { $this->id_book = $id_book; return $this; }

    public function getNomBook(): ?string { return $this->nom_book; }
    public function setNomBook(string $nom_book): static { $this->nom_book = $nom_book; return $this; }

    public function getCatBook(): ?Categoriebook { return $this->cat_book; }
    public function setCatBook(?Categoriebook $cat_book): static { $this->cat_book = $cat_book; return $this; }

    public function getDispoBook(): ?string { return $this->dispo_book; }
    public function setDispoBook(string $dispo_book): static { $this->dispo_book = $dispo_book; return $this; }

    public function getDescription(): ?string { return $this->description; }
    public function setDescription(string $description): static { $this->description = $description; return $this; }

    public function getFileId(): ?string { return $this->file_id; }
    public function setFileId(string $file_id): static { $this->file_id = $file_id; return $this; }

    public function getPdfFile(): ?string { return $this->pdf_file; }
    public function setPdfFile(?string $pdf_file): static { $this->pdf_file = $pdf_file; return $this; }

    public function getPicture(): ?string { return $this->picture; }
    public function setPicture(?string $picture): static { $this->picture = $picture; return $this; }
}
