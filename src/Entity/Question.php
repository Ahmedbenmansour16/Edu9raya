<?php
namespace App\Entity;

use App\Repository\QuestionRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: QuestionRepository::class)]
class Question
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\ManyToOne(targetEntity: Test::class, inversedBy: 'questions')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Test $test = null;

    #[ORM\Column(type: 'text')]
    private ?string $enonce = null;

    // On stocke 4 réponses
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private ?string $answer1 = null;
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private ?string $answer2 = null;
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private ?string $answer3 = null;
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private ?string $answer4 = null;

    // La bonne réponse (1, 2, 3 ou 4 par exemple)
    #[ORM\Column(type: 'integer')]
    private ?int $correctAnswer = null;

    // GETTERS / SETTERS ...
    public function getId(): ?int
    {
        return $this->id;
    }
    public function getTest(): ?Test
    {
        return $this->test;
    }
    public function setTest(?Test $test): self
    {
        $this->test = $test;
        return $this;
    }
    public function getEnonce(): ?string
    {
        return $this->enonce;
    }
    public function setEnonce(string $enonce): self
    {
        $this->enonce = $enonce;
        return $this;
    }
    public function getAnswer1(): ?string
    {
        return $this->answer1;
    }
    public function setAnswer1(?string $answer1): self
    {
        $this->answer1 = $answer1;
        return $this;
    }
    public function getAnswer2(): ?string
    {
        return $this->answer2;
    }
    public function setAnswer2(?string $answer2): self
    {
        $this->answer2 = $answer2;
        return $this;
    }
    public function getAnswer3(): ?string
    {
        return $this->answer3;
    }
    public function setAnswer3(?string $answer3): self
    {
        $this->answer3 = $answer3;
        return $this;
    }
    public function getAnswer4(): ?string
    {
        return $this->answer4;
    }
    public function setAnswer4(?string $answer4): self
    {
        $this->answer4 = $answer4;
        return $this;
    }
    public function getCorrectAnswer(): ?int
    {
        return $this->correctAnswer;
    }
    public function setCorrectAnswer(int $correctAnswer): self
    {
        $this->correctAnswer = $correctAnswer;
        return $this;
    }
}
