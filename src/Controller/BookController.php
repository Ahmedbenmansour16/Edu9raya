<?php

namespace App\Controller;

use App\Entity\Book;
use App\Form\BookType;
use App\Form\BookFilterType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
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
        return $this->render('student/details.html.twig', [
            'book' => $book,
        ]);
    }

    #[Route('/table_book', name: 'table_book')]
    public function listBooks(EntityManagerInterface $entityManager): Response
    {
        // Fetch all books
        $books = $entityManager->getRepository(Book::class)->findAll();

        return $this->render('admin/table_book.html.twig', [
            'books' => $books,
        ]);
    }

    #[Route('/book/delete/{id}', name: 'delete_book', methods: ['POST', 'GET'])]
    public function deleteBook(Book $book, EntityManagerInterface $entityManager, Request $request): Response
    {
        if ($request->isMethod('POST')) {
            $entityManager->remove($book);
            $entityManager->flush();

            $this->addFlash('success', 'Le livre a été supprimé avec succès.');

            return $this->redirectToRoute('table_book');
        }

        return $this->render('book/confirm_delete.html.twig', [
            'book' => $book,
        ]);
    }

    #[Route('/book/update/{id}', name: 'update_book')]
    public function updateBook(Book $book, Request $request, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(BookType::class, $book);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            $this->addFlash('success', 'Le livre a été mis à jour avec succès.');

            return $this->redirectToRoute('table_book');
        }

        return $this->render('admin/update_book.html.twig', [
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

        $book = $entityManager->getRepository(Book::class)->findOneBy(['nomBook' => $bookName]);

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
        return $this->render('student/404.html.twig');
    }

    #[Route('/books', name: 'filtered_books_list', methods: ['GET'])]
    public function filteredBooksList(EntityManagerInterface $entityManager, Request $request): Response
    {
        // ✅ Create the filter form
        $form = $this->createForm(BookFilterType::class);
        $form->handleRequest($request);

        // ✅ Query Builder to fetch books
        $queryBuilder = $entityManager->getRepository(Book::class)->createQueryBuilder('b');

        if ($form->isSubmitted() && $form->isValid()) {
            $filters = $form->getData();

            // 🔹 Filter by Disponibility
            if ($filters['dispo_book'] !== null) { // ✅ Use 'dispoBook'
                $queryBuilder->andWhere('b.dispo_book = :dispo')
                    ->setParameter('dispo', $filters['dispo_book']);
            }

            // 🔹 Filter by PDF File Availability
            if ($filters['pdf_file'] !== null) {
                $queryBuilder->andWhere('b.pdf_file IS NOT NULL');
            }

            // 🔹 Filter by Category
            if ($filters['cat_book']) {
                $queryBuilder->join('b.cat_book', 'c')
                    ->andWhere('c.id = :cat_book')
                    ->setParameter('cat_book', $filters['cat_book']->getId());
            }

            // 🔹 Sorting by Name
            $queryBuilder->orderBy('b.nom_book', $filters['nom_book'] === 'desc' ? 'DESC' : 'ASC');
        }

        // ✅ Fetch Books
        $books = $queryBuilder->getQuery()->getResult();

        return $this->render('student/front.html.twig', [
            'books' => $books,
            'form' => $form->createView(), // ✅ Correctly named as 'form'
        ]);
    }
}
