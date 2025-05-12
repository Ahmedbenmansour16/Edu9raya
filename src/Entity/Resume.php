<?php
namespace App\Entity;

use App\Repository\ResumeRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ResumeRepository::class)]
class Resume
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank(message: "Le fichier CV est obligatoire")]
    private ?string $filename = null;

    #[ORM\Column(type: 'blob', nullable: true)]
    private $fileContent = null;

    #[ORM\ManyToOne(inversedBy: 'resumes')]
    #[ORM\JoinColumn(name: 'stage_id', referencedColumnName: 'id', nullable: false)]
    #[Assert\NotBlank(message: "Le stage est obligatoire")]
    private ?Stage $stage = null;

    public function getId(): ?int { return $this->id; }

    public function getFilename(): ?string { return $this->filename; }
    public function setFilename(string $filename): self { $this->filename = $filename; return $this; }

    public function getFileContent() { return $this->fileContent; }
    public function setFileContent($fileContent): self { $this->fileContent = $fileContent; return $this; }

    public function getStage(): ?Stage { return $this->stage; }
    public function setStage(?Stage $stage): self { $this->stage = $stage; return $this; }
}