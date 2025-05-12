<?php

namespace App\Controller\Student;

use App\Entity\Resume;
use App\Entity\Stage;
use App\Form\ResumeType;
use App\Repository\StageRepository;
use App\Repository\ResumeRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/student/stage')]
class StageController extends AbstractController
{
    #[Route('/', name: 'student_stage_index', methods: ['GET'])]
    public function index(StageRepository $stageRepository, ResumeRepository $resumeRepository): Response
    {
        $stages = $stageRepository->findAll();
        $resumesByStage = [];
        foreach ($stages as $stage) {
            $resumesByStage[$stage->getId()] = $resumeRepository->findByStage($stage);
        }

        return $this->render('student/stage/index.html.twig', [
            'stages' => $stages,
            'resumesByStage' => $resumesByStage,
        ]);
    }

    #[Route('/apply/{id}', name: 'student_stage_apply', methods: ['GET', 'POST'])]
    public function apply(Request $request, Stage $stage, EntityManagerInterface $entityManager): Response
    {
        $resume = new Resume();
        $resume->setStage($stage);

        $form = $this->createForm(ResumeType::class, $resume);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $cvFile = $form->get('filename')->getData();
            if ($cvFile instanceof UploadedFile) {
                // Store the original filename (e.g., my_cv.pdf)
                $resume->setFilename($cvFile->getClientOriginalName());

                // Read the file content and store it as a BLOB
                $fileContent = file_get_contents($cvFile->getPathname());
                $resume->setFileContent($fileContent);
            }

            $entityManager->persist($resume);
            $entityManager->flush();

            $this->addFlash('success', 'Votre candidature a été envoyée avec succès.');
            return $this->redirectToRoute('student_stage_index');
        }

        return $this->render('student/stage/apply.html.twig', [
            'form' => $form->createView(),
            'stage' => $stage,
        ]);
    }

    #[Route('/{id}', name: 'student_stage_show', methods: ['GET'])]
    public function show(Stage $stage): Response
    {
        return $this->render('student/stage/show.html.twig', [
            'stage' => $stage,
        ]);
    }
}