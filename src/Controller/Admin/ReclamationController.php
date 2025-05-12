<?php

namespace App\Controller\Admin;

use App\Entity\Reclamation;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Csrf\CsrfToken;
use Symfony\Component\Security\Csrf\CsrfTokenManagerInterface;

#[Route('/admin/reclamation')]
class ReclamationController extends AbstractController
{
    private $entityManager;
    private $csrfTokenManager;

    public function __construct(EntityManagerInterface $entityManager, CsrfTokenManagerInterface $csrfTokenManager)
    {
        $this->entityManager = $entityManager;
        $this->csrfTokenManager = $csrfTokenManager;
    }

    #[Route('/', name: 'admin_reclamation_index', methods: ['GET'])]
    public function index(EntityManagerInterface $entityManager): Response
    {
        $reclamations = $entityManager
            ->getRepository(Reclamation::class)
            ->findBy([], ['dateEnvoi' => 'DESC']);

        return $this->render('admin/reclamation/index.html.twig', [
            'reclamations' => $reclamations,
        ]);
    }

    #[Route('/{id}/repondre', name: 'admin_reclamation_repondre', methods: ['POST'])]
    public function repondre(Request $request, Reclamation $reclamation, EntityManagerInterface $entityManager): Response
    {
        $token = new CsrfToken('repondre_reclamation_' . $reclamation->getId(), $request->request->get('token'));
        if (!$this->csrfTokenManager->isTokenValid($token)) {
            $this->addFlash('error', 'Jeton CSRF invalide.');
            return $this->redirectToRoute('admin_reclamation_index');
        }

        $response = $request->request->get('response');
        if (!$response || !trim($response)) {
            $this->addFlash('error', 'La réponse ne peut pas être vide.');
            return $this->redirectToRoute('admin_reclamation_index');
        }

        $reclamation->setAdminResponse($response);
        $reclamation->setStatut('traite');
        $entityManager->flush();

        $this->addFlash('success', 'Réponse envoyée avec succès.');
        return $this->redirectToRoute('admin_reclamation_index');
    }
}