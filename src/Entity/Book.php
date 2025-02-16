<?php

namespace App\Entity;

use App\Repository\BookRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: BookRepository::class)]
class Book
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $id_book = null;

    #[ORM\Column(length: 255)]
    private ?string $nom_book = null;

    #[ORM\ManyToOne(targetEntity: Categorie::class)]
    #[ORM\JoinColumn(name: "cat_book", referencedColumnName: "id", nullable: true)]
    private ?Categorie $cat_book = null;

    #[ORM\Column(length: 3)]
    private ?string $dispo_book = null;

    #[ORM\Column(type: "text", nullable: true)]
    private ?string $description = null;

    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private ?string $pdf_file = null;

    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private ?string $file_id = null;

    #[ORM\Column(type: "string", length: 255, nullable: true)]
    private ?string $picture = null;

    // ✅ Getter for Category Name (Fix for your error)
    public function getCategoryName(): ?string
    {
        return $this->cat_book ? $this->cat_book->getNomCat() : 'Non défini';
    }

    // ✅ Other Getters and Setters
    public function getId(): ?int { return $this->id; }
    public function getIdBook(): ?string { return $this->id_book; }
    public function setIdBook(string $id_book): static { $this->id_book = $id_book; return $this; }

    public function getNomBook(): ?string { return $this->nom_book; }
    public function setNomBook(string $nom_book): static { $this->nom_book = $nom_book; return $this; }

    public function getCatBook(): ?Categorie { return $this->cat_book; }
    public function setCatBook(?Categorie $cat_book): static { $this->cat_book = $cat_book; return $this; }

    public function getDispoBook(): ?string { return $this->dispo_book; }
    public function setDispoBook(string $dispo_book): static { $this->dispo_book = $dispo_book; return $this; }

    public function getPdfFile(): ?string { return $this->pdf_file; }
    public function setPdfFile(?string $pdf_file): static { $this->pdf_file = $pdf_file; return $this; }

    public function getFileId(): ?string { return $this->file_id; }
    public function setFileId(string $file_id): static { $this->file_id = $file_id; return $this; }

    public function getDescription(): ?string { return $this->description; }
    public function setDescription(?string $description): static { $this->description = $description; return $this; }

    public function getPicture(): ?string { return $this->picture; }
    public function setPicture(?string $picture): static { $this->picture = $picture; return $this; }
}
