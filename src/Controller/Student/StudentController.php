<?php

namespace App\Controller\Student;

use DateTime;
use App\Entity\Niveau;
use App\Entity\Formation;
use App\Entity\UserAnswer;
use App\Service\CertificateGenerator;
use App\Service\YouTubeService;
use App\Repository\FormationRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\ResponseHeaderBag;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;

#[Route('/student')]
class StudentController extends AbstractController
{
    #[Route('/', name: 'student_index', methods: ['GET'])]
    public function index(FormationRepository $formationRepository): Response
    {
        $formations = $formationRepository->findAll();
        $grouped = [];
        foreach ($formations as $formation) {
            $catName = $formation->getCategorie()->getNom();
            if (!isset($grouped[$catName])) {
                $grouped[$catName] = [];
            }
            $grouped[$catName][] = $formation;
        }
        return $this->render('student/index.html.twig', [
            'groupedFormations' => $grouped,
        ]);
    }

    #[Route('/formation/{id}', name: 'student_formation_show', methods: ['GET'])]
    public function formationShow(Formation $formation): Response
    {
        return $this->render('student/formation_show.html.twig', [
            'formation' => $formation,
        ]);
    }

    #[Route('/level/{id}', name: 'student_level_show', methods: ['GET'])]
    public function levelShow(Niveau $niveau, YouTubeService $youtubeService = null): Response
    {
        if ($youtubeService) {
            foreach ($niveau->getContenus() as $contenu) {
                if ($contenu->getType() === 'youtube' && $contenu->getYoutubeId()) {
                    try {
                        // Placeholder for YouTube service logic
                    } catch (\Exception $e) {
                        $this->addFlash('warning', 'Impossible de récupérer toutes les informations des vidéos YouTube.');
                    }
                }
            }
        }
        
        return $this->render('student/level_show.html.twig', [
            'niveau' => $niveau,
        ]);
    }

    #[Route('/test/{formationId}', name: 'student_test', methods: ['GET', 'POST'])]
    public function test(Request $request, int $formationId, EntityManagerInterface $em): Response
    {
        $formation = $em->getRepository(Formation::class)->find($formationId);
        if (!$formation) {
            throw $this->createNotFoundException('Formation non trouvée.');
        }
        $test = $formation->getTest();
        if (!$test) {
            throw $this->createNotFoundException('Test non défini pour cette formation.');
        }

        // Get all questions and randomize them
        $questions = $test->getQuestions()->toArray();
        if (count($questions) < 5) {
            throw $this->createNotFoundException('Le test doit contenir au moins 5 questions.');
        }

        // Shuffle questions and take only the first 5
        shuffle($questions);
        $selectedQuestions = array_slice($questions, 0, 5);

        if ($request->isMethod('POST')) {
            $submittedAnswers = $request->request->all('answers') ?? [];
            $total = count($selectedQuestions);
            $correct = 0;
            $user = $this->getUser();
            foreach ($selectedQuestions as $question) {
                $qId = $question->getId();
                $submitted = $submittedAnswers[$qId] ?? null;
                if ($submitted !== null && (int)$submitted === $question->getCorrectAnswer()) {
                    $correct++;
                }
                // Save user answer
                $userAnswer = new UserAnswer();
                $userAnswer->setUser($user);
                $userAnswer->setTest($test);
                $userAnswer->setQuestion($question);
                $userAnswer->setSelectedAnswer($submitted !== null ? (int)$submitted : 0);
                $em->persist($userAnswer);
            }
            $em->flush();
            $score = ($total > 0) ? ($correct / $total) * 100 : 0;
            if ($score >= 50) {
                return $this->redirectToRoute('student_certificate', ['formationId' => $formationId, 'score' => $score]);
            } else {
                $this->addFlash('danger', "Votre score est de " . round($score, 2) . "%. Vous n'avez pas réussi le test.");
                return $this->redirectToRoute('student_test', ['formationId' => $formationId]);
            }
        }
        
        return $this->render('student/test.html.twig', [
            'test' => $test,
            'selectedQuestions' => $selectedQuestions,
        ]);
    }

    #[Route('/certificate/{formationId}/{score}', name: 'student_certificate', methods: ['GET'])]
    public function certificate(int $formationId, float $score, EntityManagerInterface $em): Response
    {
        $formation = $em->getRepository(Formation::class)->find($formationId);
        if (!$formation) {
            throw $this->createNotFoundException('Formation non trouvée.');
        }
        return $this->render('student/certificate.html.twig', [
            'formation' => $formation,
            'score' => $score,
        ]);
    }

    #[Route('/certificate/{formationId}/{score}/pdf', name: 'student_certificate_pdf', methods: ['GET'])]
    public function certificatePdf(
        int $formationId, 
        float $score, 
        EntityManagerInterface $em, 
        CertificateGenerator $certificateGenerator
    ): Response
    {
        $formation = $em->getRepository(Formation::class)->find($formationId);
        if (!$formation) {
            throw $this->createNotFoundException('Formation non trouvée.');
        }
        
        $pdf = $certificateGenerator->generateCertificatePdf(
            $formation,
            $this->getUser(),
            $score,
            new DateTime()
        );
        
        $response = new Response($pdf['binary']);
        
        $disposition = $response->headers->makeDisposition(
            ResponseHeaderBag::DISPOSITION_ATTACHMENT,
            $pdf['filename']
        );
        $response->headers->set('Content-Type', 'application/pdf');
        $response->headers->set('Content-Disposition', $disposition);
        
        return $response;
    }

    #[Route('/test-answers/{formationId}', name: 'student_test_answers', methods: ['GET'])]
    public function showTestAnswers(int $formationId, EntityManagerInterface $em): Response
    {
        $formation = $em->getRepository(Formation::class)->find($formationId);
        if (!$formation) {
            throw $this->createNotFoundException('Formation non trouvée.');
        }
        $test = $formation->getTest();
        if (!$test) {
            throw $this->createNotFoundException('Test non défini pour cette formation.');
        }

        $user = $this->getUser();
        $userAnswers = $em->getRepository(UserAnswer::class)->findBy([
            'user' => $user,
            'test' => $test,
        ]);
        $questions = $test->getQuestions()->toArray();
        $answersMap = [];
        foreach ($userAnswers as $ua) {
            $answersMap[$ua->getQuestion()->getId()] = $ua->getSelectedAnswer();
        }

        return $this->render('student/test_answers.html.twig', [
            'formation' => $formation,
            'questions' => $questions,
            'userAnswers' => $answersMap,
        ]);
    }
}