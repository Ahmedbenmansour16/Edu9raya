<?php
namespace App\Controller;

use App\Entity\Categorie;
use App\Form\CategorieType;
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
        return $this->render('categorie/index.html.twig', [
            'controller_name' => 'CategorieController',
        ]);
    }

    #[Route('/categorie/add', name: 'add_categorie')]
    public function addCategorie(Request $request, EntityManagerInterface $entityManager): Response
    {
        // Create a new category object
        $categorie = new Categorie();
        
        // Create the form
        $form = $this->createForm(CategorieType::class, $categorie);
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

        return $this->render('dhasbord/add_categorie.html.twig', [
            'form' => $form->createView(),
        ]);
    }



    #[Route('/categorie/list', name: 'list_categorie')]
    public function listCategories(EntityManagerInterface $entityManager): Response
    {
        $categories = $entityManager->getRepository(Categorie::class)->findAll();

        return $this->render('dhasbord/list_categorie.html.twig', [
            'categories' => $categories,
        ]);
    }

   
        #[Route('/categorie/update/{id}', name: 'update_categorie')]
        public function updateCategory(Request $request, Categorie $categorie, EntityManagerInterface $entityManager): Response
        {
            $form = $this->createForm(CategorieType::class, $categorie);
            $form->handleRequest($request);
    
            if ($form->isSubmitted() && $form->isValid()) {
                $entityManager->flush();
                $this->addFlash('success', 'Catégorie mise à jour avec succès!');
                return $this->redirectToRoute('list_categorie');
            }
    
            return $this->render('dhasbord/edit_categorie.html.twig', [
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
            return $this->render('categorie/confirm_delete.html.twig', [
                'categorie' => $categorie,
            ]);
        }
    
}
