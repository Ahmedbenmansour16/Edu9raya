<?php
namespace App\Entity;

use App\Repository\TestRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: TestRepository::class)]
class Test
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    // Lié à Formation (OneToOne)
    #[ORM\OneToOne(inversedBy: 'test')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Formation $formation = null;

    // 10 questions
    #[ORM\OneToMany(mappedBy: 'test', targetEntity: Question::class, cascade: ['persist','remove'])]
    private Collection $questions;

    public function __construct()
    {
        $this->questions = new ArrayCollection();
    }

    // GETTERS / SETTERS ...
    public function getId(): ?int
    {
        return $this->id;
    }
    public function getFormation(): ?Formation
    {
        return $this->formation;
    }
    public function setFormation(?Formation $formation): self
    {
        $this->formation = $formation;
        // Bidirectionnel
        if ($formation && $formation->getTest() !== $this) {
            $formation->setTest($this);
        }
        return $this;
    }
    /** @return Collection<int, Question> */
    public function getQuestions(): Collection
    {
        return $this->questions;
    }
    public function addQuestion(Question $question): self
    {
        if (!$this->questions->contains($question)) {
            $this->questions->add($question);
            $question->setTest($this);
        }
        return $this;
    }
    public function removeQuestion(Question $question): self
    {
        if ($this->questions->removeElement($question)) {
            if ($question->getTest() === $this) {
                $question->setTest(null);
            }
        }
        return $this;
    }
}
