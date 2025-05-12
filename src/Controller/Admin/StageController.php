<?php
namespace App\Controller\Admin;

use App\Entity\Stage;
use App\Form\StageType;
use App\Repository\StageRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/admin/stage')]
class StageController extends AbstractController
{
    #[Route('/', name: 'admin_stage_index', methods: ['GET'])]
    public function index(StageRepository $stageRepository): Response
    {
        return $this->render('admin/stage/index.html.twig', [
            'stages' => $stageRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'admin_stage_new', methods: ['GET','POST'])]
    public function new(Request $request, EntityManagerInterface $em): Response
    {
        $stage = new Stage();
        $form = $this->createForm(StageType::class, $stage);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($stage);
            $em->flush();

            $this->addFlash('success', 'Stage créé avec succès !');
            return $this->redirectToRoute('admin_stage_index');
        }

        return $this->render('admin/stage/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'admin_stage_show', methods: ['GET'])]
    public function show(Stage $stage): Response
    {
        return $this->render('admin/stage/show.html.twig', [
            'stage' => $stage,
        ]);
    }

    #[Route('/{id}/edit', name: 'admin_stage_edit', methods: ['GET','POST'])]
    public function edit(Request $request, Stage $stage, EntityManagerInterface $em): Response
    {
        $form = $this->createForm(StageType::class, $stage);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->flush();
            $this->addFlash('success', 'Stage mis à jour avec succès !');
            return $this->redirectToRoute('admin_stage_index');
        }

        return $this->render('admin/stage/edit.html.twig', [
            'form' => $form->createView(),
            'stage' => $stage,
        ]);
    }

    #[Route('/{id}', name: 'admin_stage_delete', methods: ['POST'])]
    public function delete(Request $request, Stage $stage, EntityManagerInterface $em): Response
    {
        if ($this->isCsrfTokenValid('delete'.$stage->getId(), $request->request->get('_token'))) {
            $em->remove($stage);
            $em->flush();
            $this->addFlash('success', 'Stage supprimé avec succès !');
        }
        return $this->redirectToRoute('admin_stage_index');
    }
}