<?php

namespace App\Entity;

use App\Repository\CategorieRepository;
use Doctrine\ORM\Mapping as ORM;




#[ORM\Entity]
class Categoriebook
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer")]
    private ?int $id = null; // ✅ Use "id" instead of "idCat"

    #[ORM\Column(type: "string", length: 255)]
    private ?string $nomCat = null;

    public function getId(): ?int
    {
        return $this->id; // ✅ Use "getId()" instead of "getidCat()"
    }

    public function getNomCat(): ?string
    {
        return $this->nomCat;
    }

    public function setNomCat(string $nomCat): self
    {
        $this->nomCat = $nomCat;
        return $this;
    }
}
