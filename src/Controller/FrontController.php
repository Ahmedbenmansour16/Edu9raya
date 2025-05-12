<?php

namespace App\Controller;

use App\Entity\Feedback;
use App\Form\FeedbackType;
use App\Repository\EvenementRepository;
use App\Repository\FeedbackRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/')]
class FrontController extends AbstractController
{
    #[Route('/', name: 'app_front_home')]
    public function index(EvenementRepository $evenementRepository): Response
    {
        return $this->render('front/index.html.twig', [
            'evenements' => $evenementRepository->findBy([], ['dateDebut' => 'DESC'], 6),
        ]);
    }

    #[Route('/events', name: 'app_front_events')]
    public function events(EvenementRepository $evenementRepository): Response
    {
        return $this->render('front/events.html.twig', [
            'evenements' => $evenementRepository->findBy([], ['dateDebut' => 'DESC']),
        ]);
    }

    #[Route('/event/{id}', name: 'app_front_event_show')]
    public function show(int $id, EvenementRepository $evenementRepository): Response
    {
        $evenement = $evenementRepository->find($id);

        if (!$evenement) {
            throw $this->createNotFoundException('Event not found');
        }

        return $this->render('front/show.html.twig', [
            'evenement' => $evenement,
        ]);
    }

    #[Route('/feedbacks', name: 'app_front_feedbacks')]
    public function feedbacks(FeedbackRepository $feedbackRepository): Response
    {
        return $this->render('front/feedbacks.html.twig', [
            'feedbacks' => $feedbackRepository->findBy([], ['dateFeedback' => 'DESC']),
        ]);
    }

    #[Route('/feedback/{id}', name: 'app_front_feedback_show', methods: ['GET'])]
    public function showFeedback(Feedback $feedback): Response
    {
        return $this->render('front/feedback/show.html.twig', [
            'feedback' => $feedback,
        ]);
    }

    #[Route('/feedback/{id}/edit', name: 'app_front_feedback_edit', methods: ['GET', 'POST'])]
    public function editFeedback(Request $request, Feedback $feedback, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(FeedbackType::class, $feedback);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            $this->addFlash('success', 'Feedback updated successfully!');
            return $this->redirectToRoute('app_front_feedback_show', ['id' => $feedback->getId()]);
        }

        return $this->renderForm('front/feedback/edit.html.twig', [
            'feedback' => $feedback,
            'form' => $form,
        ]);
    }

    #[Route('/feedback/{id}', name: 'app_front_feedback_delete', methods: ['POST'])]
    public function deleteFeedback(Request $request, Feedback $feedback, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$feedback->getId(), $request->request->get('_token'))) {
            $entityManager->remove($feedback);
            $entityManager->flush();
            $this->addFlash('success', 'Feedback deleted successfully!');
        }

        return $this->redirectToRoute('app_front_feedbacks');
    }
}
