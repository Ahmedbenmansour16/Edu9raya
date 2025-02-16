<?php
namespace App\Entity;

use App\Repository\FormationRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: FormationRepository::class)]
class Formation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank]
    private ?string $nom = null;

    #[ORM\Column(type: 'text')]
    #[Assert\NotBlank]
    private ?string $description = null;

    // On stocke simplement le chemin du fichier image
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private ?string $image = null;

    #[ORM\Column(type: 'datetime')]
    private ?\DateTimeInterface $dateCreation = null;

    // Relation avec Categorie (ManyToOne)
    #[ORM\ManyToOne(targetEntity: Categorie::class)]
    #[ORM\JoinColumn(nullable: false)]
    #[Assert\NotNull(message: 'Veuillez choisir une catégorie.')]
    private ?Categorie $categorie = null;

    // 5 niveaux
    #[ORM\OneToMany(mappedBy: 'formation', targetEntity: Niveau::class, cascade: ['persist', 'remove'])]
    private Collection $niveaux;

    // Test final (10 questions)
    #[ORM\OneToOne(mappedBy: 'formation', targetEntity: Test::class, cascade: ['persist', 'remove'])]
    private ?Test $test = null;

    public function __construct()
    {
        $this->niveaux = new ArrayCollection();
        $this->dateCreation = new \DateTime();
    }

    // GETTERS / SETTERS ...

    public function getId(): ?int
    {
        return $this->id;
    }
    public function getNom(): ?string
    {
        return $this->nom;
    }
    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }
    public function getDescription(): ?string
    {
        return $this->description;
    }
    public function setDescription(string $description): self
    {
        $this->description = $description;
        return $this;
    }
    public function getImage(): ?string
    {
        return $this->image;
    }
    public function setImage(?string $image): self
    {
        $this->image = $image;
        return $this;
    }
    public function getDateCreation(): ?\DateTimeInterface
    {
        return $this->dateCreation;
    }
    public function setDateCreation(\DateTimeInterface $dateCreation): self
    {
        $this->dateCreation = $dateCreation;
        return $this;
    }
    public function getCategorie(): ?Categorie
    {
        return $this->categorie;
    }
    public function setCategorie(?Categorie $categorie): self
    {
        $this->categorie = $categorie;
        return $this;
    }

    /** @return Collection<int, Niveau> */
    public function getNiveaux(): Collection
    {
        return $this->niveaux;
    }
    public function addNiveau(Niveau $niveau): self
    {
        if (!$this->niveaux->contains($niveau)) {
            $this->niveaux->add($niveau);
            $niveau->setFormation($this);
        }
        return $this;
    }
    public function removeNiveau(Niveau $niveau): self
    {
        if ($this->niveaux->removeElement($niveau)) {
            if ($niveau->getFormation() === $this) {
                $niveau->setFormation(null);
            }
        }
        return $this;
    }

    public function getTest(): ?Test
    {
        return $this->test;
    }
    public function setTest(?Test $test): self
    {
        $this->test = $test;
        // Mise à jour bidirectionnelle
        if ($test && $test->getFormation() !== $this) {
            $test->setFormation($this);
        }
        return $this;
    }
}
