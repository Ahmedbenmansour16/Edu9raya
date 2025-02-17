<?php

namespace App\DataFixtures;

use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;
use App\Entity\Etudiant;
use App\Entity\Prof;

class AppFixtures extends Fixture
{
    public function load(ObjectManager $manager): void
    {
        // $product = new Product();
        
        $etudiant= new Etudiant;
        $etudiant->setName('youcef');
        $etudiant->setPrenom("kharroubi");
        $etudiant->setAge(21);
        $etudiant->setEmail("kharroubi.youcef@esprit.tn");
        $etudiant->setClasse("3A64"); 
        $etudiant->setAdresse("kram");
        $manager->persist($etudiant);

        $prof= new Prof;
        $prof->setNom('ali');
        $prof->setPrenom("ali");
        $prof->setAge(40);
        $prof->setEmail("ali.ali@ali.tn");
        $prof->setMatiere("math");
        $prof->setAdresse("kram");
        $manager->persist($prof);






        $manager->flush();
    }
}
