<?php
namespace App\Controller\back;

use App\Entity\Prof;
use App\Form\ProfType;
use App\Repository\ProfRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/prof')]
final class ProfController extends AbstractController
{
    #[Route(name: 'app_prof_index', methods: ['GET'])]
    public function index(ProfRepository $profRepository): Response
    {
        return $this->render('back/prof/index.html.twig', [
            'profs' => $profRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_prof_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $prof = new Prof();
        $form = $this->createForm(ProfType::class, $prof);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Ensure a User is selected before checking roles
            $user = $prof->getUser(); // Get the associated User
            if ($user === null) {
                $this->addFlash('error', 'Aucun utilisateur sélectionné.');
                return $this->redirectToRoute('app_prof_new');
            }

            // Check if the selected User has the ROLE_PROF role
            if (!in_array('ROLE_PROF', $user->getRoles())) {
                $this->addFlash('error', 'L\'utilisateur sélectionné n\'est pas un professeur.');
                return $this->redirectToRoute('app_prof_new');
            }

            // Persist the Prof entity and flush to the database
            $entityManager->persist($prof);
            $entityManager->flush();

            return $this->redirectToRoute('app_prof_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('back/prof/new.html.twig', [
            'prof' => $prof,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_prof_show', methods: ['GET'])]
    public function show(int $id, ProfRepository $profRepository): Response
    {
        $prof = $profRepository->find($id);
        
        if (!$prof) {
            throw $this->createNotFoundException('Prof not found');
        }

        return $this->render('back/prof/show.html.twig', [
            'prof' => $prof,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_prof_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, int $id, ProfRepository $profRepository, EntityManagerInterface $entityManager): Response
    {
        $prof = $profRepository->find($id);
        
        if (!$prof) {
            throw $this->createNotFoundException('Prof not found');
        }

        $form = $this->createForm(ProfType::class, $prof);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Make sure to persist the changes
            $entityManager->flush();

            return $this->redirectToRoute('app_prof_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('back/prof/edit.html.twig', [
            'prof' => $prof,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_prof_delete', methods: ['POST'])]
    public function delete(Request $request, int $id, ProfRepository $profRepository, EntityManagerInterface $entityManager): Response
    {
        $prof = $profRepository->find($id);
        
        if (!$prof) {
            throw $this->createNotFoundException('Prof not found');
        }

        if ($this->isCsrfTokenValid('delete'.$prof->getId(), $request->request->get('_token'))) {
            $entityManager->remove($prof);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_prof_index', [], Response::HTTP_SEE_OTHER);
    }
}
