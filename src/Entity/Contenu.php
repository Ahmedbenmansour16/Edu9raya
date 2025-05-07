<?php
namespace App\Entity;

use App\Repository\ContenuRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ContenuRepository::class)]
class Contenu
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    // type = "video", "pdf", "image", "description", "youtube"
    #[ORM\Column(type: 'string', length: 50)]
    private ?string $type = null;

    // Chemin du fichier si c'est video, pdf, image
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private ?string $fichier = null;

    // Texte si c'est "description"
    #[ORM\Column(type: 'text', nullable: true)]
    private ?string $description = null;

    // ID de la vidéo YouTube si type = "youtube"
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private ?string $youtubeId = null;

    #[ORM\ManyToOne(targetEntity: Niveau::class, inversedBy: 'contenus')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Niveau $niveau = null;

    public function getId(): ?int
    {
        return $this->id;
    }
    
    public function getType(): ?string
    {
        return $this->type;
    }
    
    public function setType(string $type): self
    {
        $this->type = $type;
        return $this;
    }
    
    public function getFichier(): ?string
    {
        return $this->fichier;
    }
    
    public function setFichier(?string $fichier): self
    {
        $this->fichier = $fichier;
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
    
    public function getYoutubeId(): ?string
    {
        return $this->youtubeId;
    }
    
    public function setYoutubeId(?string $youtubeId): self
    {
        $this->youtubeId = $youtubeId;
        return $this;
    }
    
    public function getNiveau(): ?Niveau
    {
        return $this->niveau;
    }
    
    public function setNiveau(?Niveau $niveau): self
    {
        $this->niveau = $niveau;
        return $this;
    }
}