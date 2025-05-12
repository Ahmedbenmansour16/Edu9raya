<?php

namespace App\Controller;

use App\Entity\Evenement;
use App\Service\StripeService;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;

#[Route('/payment')]
class PaymentController extends AbstractController
{
    private $stripeService;
    private $entityManager;

    public function __construct(StripeService $stripeService, EntityManagerInterface $entityManager)
    {
        $this->stripeService = $stripeService;
        $this->entityManager = $entityManager;
    }

    #[Route('/event/{id}/checkout', name: 'payment_event_checkout', methods: ['GET'])]
    public function eventCheckout(Evenement $event): Response
    {
        try {
            if ($event->getNbrParticipantsMax() <= 0) {
                throw new \Exception('Sorry, this event is fully booked.');
            }

            $amount = 10; // Amount in TND
            
            return $this->render('payment/checkout.html.twig', [
                'stripe_public_key' => $this->stripeService->getPublicKey(),
                'amount' => $amount,
                'event' => $event
            ]);
        } catch (\Exception $e) {
            $this->addFlash('error', $e->getMessage());
            return $this->redirectToRoute('app_front_events');
        }
    }

    #[Route('/event/{id}/create-payment-intent', name: 'create_payment_intent', methods: ['POST'])]
    public function createPaymentIntent(Request $request, Evenement $event): JsonResponse
    {
        try {
            if ($event->getNbrParticipantsMax() <= 0) {
                throw new \Exception('Sorry, this event is fully booked.');
            }

            $amount = 10; // Fixed amount in TND
            $paymentIntent = $this->stripeService->createPaymentIntent($amount);

            return new JsonResponse([
                'clientSecret' => $paymentIntent->client_secret,
            ]);
        } catch (\Exception $e) {
            return new JsonResponse([
                'error' => $e->getMessage()
            ], 400);
        }
    }

    #[Route('/event/{id}/success', name: 'payment_event_success', methods: ['GET'])]
    public function eventSuccess(Evenement $event): Response
    {
        try {
            if ($event->getNbrParticipantsMax() <= 0) {
                throw new \Exception('Sorry, this event is fully booked.');
            }

            // Decrement the number of participants
            $event->setNbrParticipantsMax($event->getNbrParticipantsMax() - 1);
            $this->entityManager->flush();

            $this->addFlash('success', 'Payment successful! You are now registered for the event.');
            return $this->render('payment/success.html.twig', [
                'event' => $event
            ]);
        } catch (\Exception $e) {
            $this->addFlash('error', $e->getMessage());
            return $this->redirectToRoute('app_front_events');
        }
    }

    #[Route('/event/{id}/cancel', name: 'payment_event_cancel', methods: ['GET'])]
    public function eventCancel(Evenement $event): Response
    {
        return $this->render('payment/cancel.html.twig', [
            'event' => $event
        ]);
    }
}
