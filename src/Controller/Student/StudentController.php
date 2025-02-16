<?php

namespace App\Controller\Student;

use App\Entity\Formation;
use App\Entity\Niveau;
use Doctrine\ORM\EntityManagerInterface;
use App\Repository\FormationRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/student')]
class StudentController extends AbstractController
{
    // Affiche la liste des formations groupées par catégorie
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

    // Affiche le détail d'une formation (ses 5 niveaux + lien vers le test)
    #[Route('/formation/{id}', name: 'student_formation_show', methods: ['GET'])]
    public function formationShow(Formation $formation): Response
    {
        return $this->render('student/formation_show.html.twig', [
            'formation' => $formation,
        ]);
    }

    // Affiche le détail d'un niveau (les contenus du niveau)
    #[Route('/level/{id}', name: 'student_level_show', methods: ['GET'])]
    public function levelShow(Niveau $niveau): Response
    {
        return $this->render('student/level_show.html.twig', [
            'niveau' => $niveau,
        ]);
    }

    // Affiche le test de la formation et traite la soumission
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
        
        if ($request->isMethod('POST')) {
            $submittedAnswers = $request->request->all('answers') ?? [];
            $total = count($test->getQuestions());
            $correct = 0;
            foreach ($test->getQuestions() as $question) {
                $qId = $question->getId();
                $submitted = $submittedAnswers[$qId] ?? null;
                if ($submitted !== null && (int)$submitted === $question->getCorrectAnswer()) {
                    $correct++;
                }
            }
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
        ]);
    }

    // Affiche le certificat si le test est réussi
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
}
