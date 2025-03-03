<?php

namespace App\Controller\front;  // Namespace 'front' en minuscule

use App\Entity\Prof;
use App\Entity\User;
use App\Form\ProfType;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;

class ProfFrontController extends AbstractController  // Nom du contrôleur corrigé
{
    /**
     * @Route("/inscription/prof/{userId}", name="prof_register")
     */
    public function registerProf(Request $request, $userId)
    {
        // Récupérer l'utilisateur via son ID
        $user = $this->getDoctrine()->getRepository(User::class)->find($userId);

        // Vérifier que l'utilisateur existe et a le bon rôle
        if (!$user || !in_array('ROLE_PROF', $user->getRoles())) {
            throw $this->createNotFoundException('Utilisateur non trouvé ou rôle invalide.');
        }

        // Créer un objet Prof
        $prof = new Prof();

        // Créer le formulaire pour le Prof
        $form = $this->createForm(ProfType::class, $prof);

        // Traiter la soumission du formulaire
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Lier le Prof à l'utilisateur
            $prof->setUser($user);

            // Sauvegarder le Prof dans la base de données
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($prof);
            $entityManager->flush();

            // Rediriger vers une page spécifique pour le Prof après l'enregistrement
            return $this->redirectToRoute('prof_welcome');
        }

        // Afficher la vue du formulaire
        return $this->render('front/prof_front/prof_front_register.html.twig', [  // Vue spécifique pour 'front'
            'form' => $form->createView(),
        ]);
    }
}
