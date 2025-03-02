<?php

namespace App\Controller\Api;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Session\SessionInterface;

#[Route('/api/timer')]
class TimerController extends AbstractController
{
    #[Route('/start/{formationId}', name: 'api_timer_start', methods: ['POST'])]
    public function startTimer(int $formationId, SessionInterface $session): JsonResponse
    {
        $endTime = time() + 60; // 60 secondes à partir de maintenant
        $session->set("timer_$formationId", $endTime);

        return new JsonResponse(['end_time' => $endTime]);
    }

    #[Route('/check/{formationId}', name: 'api_timer_check', methods: ['GET'])]
    public function checkTimer(int $formationId, SessionInterface $session): JsonResponse
    {
        $endTime = $session->get("timer_$formationId", time());
        $timeLeft = max(0, $endTime - time());

        return new JsonResponse(['time_left' => $timeLeft]);
    }
}
