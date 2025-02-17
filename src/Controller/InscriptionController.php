<?php
namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

final class InscriptionController extends AbstractController
{
    #[Route('/inscription', name: 'app_inscription')]
    public function index(): Response
    {
        return $this->render('inscription/index.html.twig');
    }

    #[Route('/inscription/{type}', name: 'app_inscription_type')]
    public function inscrireType(string $type): Response
    {
        // Rediriger selon le type sélectionné
        if ($type === 'prof') {
            return $this->redirectToRoute('app_prof_new');  // Remplacez par la route de votre CRUD ProfType
        } elseif ($type === 'etudiant') {
            return $this->redirectToRoute('app_etudiant_new');  // Remplacez par la route de votre CRUD EtudiantType
        }

        // Si le type n'est pas valide, on redirige vers la page d'accueil
        return $this->redirectToRoute('app_home');
    }
}
