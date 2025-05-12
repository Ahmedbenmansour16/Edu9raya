<?php

namespace App\Entity;

use App\Repository\FeedbackRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: FeedbackRepository::class)]
class Feedback
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\ManyToOne(inversedBy: 'feedback')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Evenement $idEvent = null;

    #[ORM\Column]
    private ?int $idParticipant = null;

    #[ORM\Column]
    #[Assert\NotBlank]
    #[Assert\Range(
        min: 1,
        max: 5,
        notInRangeMessage: 'Note must be between {{ min }} and {{ max }}',
    )]
    private ?int $note = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $remarque = null;

    #[ORM\Column(type: "datetime")]
    private ?\DateTimeInterface $dateFeedback = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getIdEvent(): ?Evenement
    {
        return $this->idEvent;
    }

    public function setIdEvent(?Evenement $idEvent): static
    {
        $this->idEvent = $idEvent;

        return $this;
    }

    public function getIdParticipant(): ?int
    {
        return $this->idParticipant;
    }

    public function setIdParticipant(int $idParticipant): static
    {
        $this->idParticipant = $idParticipant;

        return $this;
    }

    public function getNote(): ?int
    {
        return $this->note;
    }

    public function setNote(int $note): static
    {
        $this->note = $note;

        return $this;
    }

    public function getRemarque(): ?string
    {
        return $this->remarque;
    }

    public function setRemarque(?string $remarque): static
    {
        $this->remarque = $remarque;

        return $this;
    }

    public function getDateFeedback(): ?\DateTimeInterface
    {
        return $this->dateFeedback;
    }

    public function setDateFeedback(\DateTimeInterface $dateFeedback): static
    {
        $this->dateFeedback = $dateFeedback;

        return $this;
    }
}
