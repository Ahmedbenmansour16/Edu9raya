<?php

namespace App\Controller;
use Symfony\Component\HttpFoundation\JsonResponse;
use App\Entity\Book;
use App\Form\BookType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;


final class BookController extends AbstractController
{
    #[Route('/book', name: 'app_book')]
    public function index(): Response
    {
        return $this->render('book/index.html.twig', [
            'controller_name' => 'BookController',
        ]);
    }


    #[Route('/book/{id}', name: 'book_detail')]
    public function show(Book $book): Response
    {
        return $this->render('dhasbord/details.html.twig', [
            'book' => $book,
        ]);
    }

    #[Route('/table_book', name: 'table_book')]
    public function listBooks(EntityManagerInterface $entityManager): Response
    {
        // Fetch all books from the database
        $books = $entityManager->getRepository(Book::class)->findAll();

        // Render the Twig template with books
        return $this->render('dhasbord/table_book.html.twig', [
            'books' => $books,
        ]);
    }


    #[Route('/book/delete/{id}', name: 'delete_book', methods: ['POST', 'GET'])]
    public function deleteBook(Book $book, EntityManagerInterface $entityManager, Request $request): Response
    {
        if ($request->isMethod('POST')) {
            // Remove the book from the database
            $entityManager->remove($book);
            $entityManager->flush();

            // Flash message to confirm deletion
            $this->addFlash('success', 'Le livre a été supprimé avec succès.');

            // Redirect to the book table
            return $this->redirectToRoute('table_book');
        }

        // Render a confirmation page (optional)
        return $this->render('book/confirm_delete.html.twig', [
            'book' => $book,
        ]);
    }


    #[Route('/book/update/{id}', name: 'update_book')]
    public function updateBook(Book $book, Request $request, EntityManagerInterface $entityManager): Response
    {
        // Create the form
        $form = $this->createForm(BookType::class, $book);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Save the updated book
            $entityManager->flush();

            // Flash message
            $this->addFlash('success', 'Le livre a été mis à jour avec succès.');

            // Redirect to the book table
            return $this->redirectToRoute('table_book');
        }

        return $this->render('dhasbord/update_book.html.twig', [
            'form' => $form->createView(),
            'book' => $book,
        ]);
    }


    #[Route('/search-book', name: 'search_book')]
    public function searchBook(Request $request, EntityManagerInterface $entityManager): JsonResponse
    {
        $bookName = $request->query->get('name', '');
    
        if (empty($bookName)) {
            return new JsonResponse(['found' => false]);
        }
    
        $book = $entityManager->getRepository(Book::class)->findOneBy(['nom_book' => $bookName]);
    
        if ($book) {
            return new JsonResponse([
                'found' => true,
                'url' => $this->generateUrl('book_detail', ['id' => $book->getId()]),
            ]);
        }
    
        return new JsonResponse(['found' => false]);
    }
    

    #[Route('/not-found', name: 'not_found')]
public function notFound(): Response
{
    return $this->render('dhasbord/404.html.twig', [], new Response('', 404));
}


}




    

