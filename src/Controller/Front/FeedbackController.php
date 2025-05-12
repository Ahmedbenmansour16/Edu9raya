<?php

namespace App\Controller\Front;

use App\Entity\Feedback;
use App\Form\FeedbackType;
use App\Repository\EvenementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/front/feedback')]
class FeedbackController extends AbstractController
{
    #[Route('/new/{eventId}', name: 'app_front_feedback_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager, EvenementRepository $evenementRepository, int $eventId): Response
    {
        // Get the event or throw 404
        $event = $evenementRepository->find($eventId);
        if (!$event) {
            throw $this->createNotFoundException('Event not found');
        }

        $feedback = new Feedback();
        $feedback->setDateFeedback(new \DateTime());
        $feedback->setIdEvent($event); // Set the event automatically
        
        $form = $this->createForm(FeedbackType::class, $feedback);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($feedback);
            $entityManager->flush();

            $this->addFlash('success', 'Thank you for your feedback!');
            return $this->redirectToRoute('app_front_evenement_show', ['id' => $eventId], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('front/feedback/new.html.twig', [
            'feedback' => $feedback,
            'event' => $event,
            'form' => $form,
        ]);
    }
}
