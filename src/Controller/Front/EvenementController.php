<?php

namespace App\Controller\Front;

use App\Repository\EvenementRepository;
use App\Entity\Evenement;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/front/evenement')]
class EvenementController extends AbstractController
{
    #[Route('/', name: 'app_front_evenement_index', methods: ['GET'])]
    public function index(EvenementRepository $evenementRepository): Response
    {
        return $this->render('front/evenement/index.html.twig', [
            'evenements' => $evenementRepository->findAll(),
        ]);
    }

    #[Route('/{id}', name: 'app_front_evenement_show', methods: ['GET'])]
    public function show(Evenement $evenement): Response
    {
        return $this->render('front/evenement/show.html.twig', [
            'evenement' => $evenement,
        ]);
    }
}
