<?php

namespace App\Controller\front;

use App\Entity\Etudiant;
use App\Entity\User;
use App\Form\EtudiantFrontType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class EtudiantFrontController extends AbstractController
{
    /**
     * @Route("/etudiant/welcome", name="etudiant_welcome")
     */
    public function welcome(): Response
    {
        // Afficher la page de bienvenue
        return $this->render('front/etudiant_front/welcome.html.twig', [
            'message' => 'Bienvenue, étudiant !',
        ]);
    }

   

    /**
     * @Route("/inscription/etudiant/{userId}", name="etudiant_register")
     */
    public function registerEtudiant(
        Request $request,
        $userId,
        EntityManagerInterface $entityManager,
        UserPasswordHasherInterface $passwordHasher
    ): Response {
        // Récupérer l'utilisateur via son ID
        $user = $entityManager->getRepository(User::class)->find($userId);

        // Vérifier que l'utilisateur existe et a le bon rôle
        if (!$user || !in_array('ROLE_ETUDIANT', $user->getRoles())) {
            throw $this->createNotFoundException('Utilisateur non trouvé ou rôle invalide.');
        }

        // Créer un nouvel étudiant
        $etudiant = new Etudiant();

        // Créer le formulaire pour l'étudiant
        $form = $this->createForm(EtudiantFrontType::class, $etudiant);
        $form->handleRequest($request);

        // Traiter la soumission du formulaire
        if ($form->isSubmitted() && $form->isValid()) {
            // Lier l'étudiant à l'utilisateur
            $etudiant->setUser($user);

            // Sauvegarder l'étudiant en base de données
            $entityManager->persist($etudiant);
            $entityManager->flush();

            // Rediriger vers la page de bienvenue
            return $this->redirectToRoute('etudiant_welcome');
        }

        // Afficher le formulaire d'inscription
        return $this->render('front/etudiant_front/etudiant_front_register.html.twig', [
            'form' => $form->createView(),
        ]);
    }

   
    
}