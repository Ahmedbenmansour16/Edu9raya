<?php 

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\BookRepository;

final class DhasbordController extends AbstractController  
{
    #[Route('/dhasbord', name: 'app_dhasbord')]
    public function index(BookRepository $bookRepository): Response
    {
        $books = $bookRepository->findAll(); // Fetch books from the database

        return $this->render('dhasbord/front.html.twig', [
            'controller_name' => 'DhasbordController',
            'books' => $books, // Pass books to the template
        ]);
    }

    #[Route('/dashboard', name: 'app_dashboard')]
    public function dashboard(BookRepository $bookRepository): Response
    {
        $books = $bookRepository->findAll(); // Fetch books

        return $this->render('dhasbord/front.html.twig', [ // Ensure correct template
            'books' => $books,
        ]);
    }
}
