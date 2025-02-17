<?php

namespace App\Controller;

use App\Entity\Cour;
use App\Entity\Module;
use App\Form\Cour1Type;
use App\Repository\CourRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/cour')]
final class CourController extends AbstractController
{
    #[Route(name: 'cours', methods: ['GET'])]
    public function index(CourRepository $courRepository): Response
    {
        return $this->render('cour/cours.html.twig', [
            'cours' => $courRepository->findAll(),
        ]);
    }

    #[Route('/back', name: 'app_cour_index', methods: ['GET'])]
    public function in(CourRepository $courRepository): Response
    {
        return $this->render('cour/index.html.twig', [
            'cours' => $courRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_cour_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $cour = new Cour();
        $form = $this->createForm(Cour1Type::class, $cour);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($cour);
            $entityManager->flush();

            return $this->redirectToRoute('app_cour_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('cour/new.html.twig', [
            'cour' => $cour,
            'form' => $form,
        ]);
    }

    #[Route('/module/{id}/cours', name: 'module_cours')]
    public function showCourses(Module $module, CourRepository $courRepository): Response
    {
        $cours = $courRepository->findBy(['module' => $module]);

        return $this->render('cour/index.html.twig', [
            'module' => $module,
            'cours' => $cours,
        ]);
    }


    #[Route('/{id}/edit', name: 'app_cour_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Cour $cour, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(Cour1Type::class, $cour);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_cour_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('cour/edit.html.twig', [
            'cour' => $cour,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_cour_delete', methods: ['POST'])]
    public function delete(Request $request, Cour $cour, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete' . $cour->getId(), $request->getPayload()->getString('_token'))) {
            $entityManager->remove($cour);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_cour_index', [], Response::HTTP_SEE_OTHER);
    }
}
