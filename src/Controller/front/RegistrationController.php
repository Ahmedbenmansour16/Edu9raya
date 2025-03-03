<?php

namespace App\Controller\front;

use App\Entity\User;
use App\Form\RegistrationType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

final class RegistrationController extends AbstractController
{
    #[Route('/inscription', name: 'app_registration')]
    public function register(
        Request $request, 
        EntityManagerInterface $entityManager, 
        UserPasswordHasherInterface $passwordHasher
    ): Response {
        // Création d'un nouvel utilisateur
        $user = new User();
        $form = $this->createForm(RegistrationType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && !$form->isValid()) {
            dump($form->getErrors(true)); // Affichez les erreurs de validation
        }

        // Vérification de la soumission et validation du formulaire
        if ($form->isSubmitted() && $form->isValid()) {
            // Récupération des rôles sélectionnés par l'utilisateur
            dump($user); // Vérifiez les données de l'utilisateur
    dump($roles);
    $roles = $form->get('roles')->getData();
    if (!is_array($roles)) {
        $roles = [$roles]; // Convertir en tableau si nécessaire
    }
    $user->setRoles($roles);

            // Hachage du mot de passe avant de l'enregistrer
            $hashedPassword = $passwordHasher->hashPassword($user, $user->getPassword());
            $user->setPassword($hashedPassword);

            try {
                $entityManager->persist($user);
                $entityManager->flush();
            } catch (\Exception $e) {
                dump($e->getMessage()); // Affichez l'erreur
            }
            

            // Redirection selon le rôle choisi
            if (in_array('ROLE_ETUDIANT', $roles)) {
                dump('Redirection vers etudiant_register');
                return $this->redirectToRoute('etudiant_register', ['user' => $user->getId()]);
            } elseif (in_array('ROLE_PROF', $roles)) {
                dump('Redirection vers prof_register');
                return $this->redirectToRoute('prof_register', ['user' => $user->getId()]);
            }
            

            // Si aucun rôle spécifique n'est défini, redirection vers la page d'accueil
            return $this->redirectToRoute('app_home');
        }

        // Affichage du formulaire d'inscription
        return $this->render('front/registration/index.html.twig', [
            'form' => $form->createView(),
        ]);
    }
}
