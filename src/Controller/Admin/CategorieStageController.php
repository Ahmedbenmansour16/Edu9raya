<?php
namespace App\Controller\Admin;

use App\Entity\CategorieStage;
use App\Form\CategorieStageType;
use App\Repository\CategorieStageRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/admin/categorie-stage')]
class CategorieStageController extends AbstractController
{
    #[Route('/', name: 'admin_categorie_stage_index', methods: ['GET'])]
    public function index(CategorieStageRepository $categorieStageRepository): Response
    {
        return $this->render('admin/categorie_stage/index.html.twig', [
            'categories' => $categorieStageRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'admin_categorie_stage_new', methods: ['GET','POST'])]
    public function new(Request $request, EntityManagerInterface $em): Response
    {
        $categorieStage = new CategorieStage();
        $form = $this->createForm(CategorieStageType::class, $categorieStage);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($categorieStage);
            $em->flush();

            $this->addFlash('success', 'Catégorie créée avec succès !');
            return $this->redirectToRoute('admin_categorie_stage_index');
        }

        return $this->render('admin/categorie_stage/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'admin_categorie_stage_show', methods: ['GET'])]
    public function show(CategorieStage $categorieStage): Response
    {
        return $this->render('admin/categorie_stage/show.html.twig', [
            'categorie' => $categorieStage,
        ]);
    }

    #[Route('/{id}/edit', name: 'admin_categorie_stage_edit', methods: ['GET','POST'])]
    public function edit(Request $request, CategorieStage $categorieStage, EntityManagerInterface $em): Response
    {
        $form = $this->createForm(CategorieStageType::class, $categorieStage);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->flush();
            $this->addFlash('success', 'Catégorie mise à jour avec succès !');
            return $this->redirectToRoute('admin_categorie_stage_index');
        }

        return $this->render('admin/categorie_stage/edit.html.twig', [
            'form' => $form->createView(),
            'categorie' => $categorieStage,
        ]);
    }

    #[Route('/{id}', name: 'admin_categorie_stage_delete', methods: ['POST'])]
    public function delete(Request $request, CategorieStage $categorieStage, EntityManagerInterface $em): Response
    {
        if ($this->isCsrfTokenValid('delete'.$categorieStage->getId(), $request->request->get('_token'))) {
            $em->remove($categorieStage);
            $em->flush();
            $this->addFlash('success', 'Catégorie supprimée avec succès !');
        }
        return $this->redirectToRoute('admin_categorie_stage_index');
    }
}