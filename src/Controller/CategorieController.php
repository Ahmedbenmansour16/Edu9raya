<?php
namespace App\Controller;

use App\Entity\Categoriebook;
use App\Form\CategoriebookType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

final class CategorieController extends AbstractController
{
    #[Route('/categorie', name: 'app_categorie')]
    public function index(): Response
    {
        return $this->render('admin/index.html.twig', [
            'controller_name' => 'CategorieController',
        ]);
    }

    #[Route('/categorie/add', name: 'add_categorie')]
    public function addCategorie(Request $request, EntityManagerInterface $entityManager): Response
    {
        // Create a new category object
        $categorie = new Categoriebook();
        
        // Create the form
        $form = $this->createForm(CategoriebookType::class, $categorie);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Save the category
            $entityManager->persist($categorie);
            $entityManager->flush();

            // Flash message
            $this->addFlash('success', 'Catégorie ajoutée avec succès.');

            // Redirect to category list (or other page)
            return $this->redirectToRoute('list_categorie');
        }

        return $this->render('admin/add_categorie.html.twig', [
            'form' => $form->createView(),
        ]);
    }



    #[Route('/categorie/list', name: 'list_categorie')]
    public function listCategories(EntityManagerInterface $entityManager): Response
    {
        $categories = $entityManager->getRepository(Categoriebook::class)->findAll();

        return $this->render('admin/list_categorie.html.twig', [
            'categories' => $categories,
        ]);
    }

   
        #[Route('/categorie/update/{id}', name: 'update_categorie')]
        public function updateCategory(Request $request, Categoriebook $categorie, EntityManagerInterface $entityManager): Response
        {
            $form = $this->createForm(CategoriebookType::class, $categorie);
            $form->handleRequest($request);
    
            if ($form->isSubmitted() && $form->isValid()) {
                $entityManager->flush();
                $this->addFlash('success', 'Catégorie mise à jour avec succès!');
                return $this->redirectToRoute('list_categorie');
            }
    
            return $this->render('admin/edit_categorie.html.twig', [
                'form' => $form->createView(),
            ]);
        }

  
        #[Route('/categorie/delete/{id}', name: 'delete_categorie', methods: ['POST'])]
        public function deleteBook(categorie $categorie, EntityManagerInterface $entityManager, Request $request): Response
        {
            if ($request->isMethod('POST')) {
                // Remove the categorie from the database
                $entityManager->remove($categorie);
                $entityManager->flush();
    
                // Flash message to confirm deletion
                $this->addFlash('success', 'Le categorie a été supprimé avec succès.');
    
                // Redirect to the categorie table
                return $this->redirectToRoute('list_categorie');
            }
    
            // Render a confirmation page (optional)
            return $this->render('admin/confirm_delete.html.twig', [
                'categorie' => $categorie,
            ]);
        }
    
}
