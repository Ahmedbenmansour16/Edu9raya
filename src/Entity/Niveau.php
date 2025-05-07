<?php
namespace App\Entity;

use App\Repository\NiveauRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: NiveauRepository::class)]
class Niveau
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    // Ordre du niveau (1 à 5)
    #[ORM\Column(type: 'integer')]
    private ?int $ordre = null;

    // Relation vers Formation
    #[ORM\ManyToOne(targetEntity: Formation::class, inversedBy: 'niveaux')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Formation $formation = null;

    // Contenus multiples (video, pdf, image, description)
    #[ORM\OneToMany(mappedBy: 'niveau', targetEntity: Contenu::class, cascade: ['persist','remove'])]
    private Collection $contenus;

    public function __construct()
    {
        $this->contenus = new ArrayCollection();
    }

    // GETTERS / SETTERS ...
    public function getId(): ?int
    {
        return $this->id;
    }
    public function getOrdre(): ?int
    {
        return $this->ordre;
    }
    public function setOrdre(int $ordre): self
    {
        $this->ordre = $ordre;
        return $this;
    }
    public function getFormation(): ?Formation
    {
        return $this->formation;
    }
    public function setFormation(?Formation $formation): self
    {
        $this->formation = $formation;
        return $this;
    }
    /** @return Collection<int, Contenu> */
    public function getContenus(): Collection
    {
        return $this->contenus;
    }
    public function addContenu(Contenu $contenu): self
    {
        if (!$this->contenus->contains($contenu)) {
            $this->contenus->add($contenu);
            $contenu->setNiveau($this);
        }
        return $this;
    }
    public function removeContenu(Contenu $contenu): self
    {
        if ($this->contenus->removeElement($contenu)) {
            if ($contenu->getNiveau() === $this) {
                $contenu->setNiveau(null);
            }
        }
        return $this;
    }
}
