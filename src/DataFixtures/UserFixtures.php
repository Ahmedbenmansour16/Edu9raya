<?php
namespace App\DataFixtures;

use App\Entity\User;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;

class UserFixtures extends Fixture
{
    public function load(ObjectManager $manager): void
    {
        // Création d'un utilisateur
        $user = new User();
        $user->setEmail('user@example.com');
        $user->setRoles(['ROLE_USER']);
        $user->setPassword('password123'); // Pour simplification, mot de passe non encodé

        // Persister l'utilisateur
        $manager->persist($user);

        // Enregistrer dans la base de données
        $manager->flush();
    }
}
