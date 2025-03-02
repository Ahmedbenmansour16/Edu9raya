<?php

namespace App\Controller;

use App\Entity\Book;
use App\Form\BookType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\String\Slugger\SluggerInterface;

class AjjouterController extends AbstractController
{
    #[Route('/ajjouter', name: 'app_ajjouter')]
    public function index(Request $request, EntityManagerInterface $entityManager, SluggerInterface $slugger): Response

    {
        $book = new Book();
        $form = $this->createForm(BookType::class, $book);

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            /** @var UploadedFile $pdfFile */
            $pdfFile = $form->get('pdfFile')->getData();
            /** @var UploadedFile $pictureFile */
            $pictureFile = $form->get('pictureFile')->getData();

            // ✅ Handle PDF Upload
            if ($pdfFile) {
                $newFilename = $slugger->slug(pathinfo($pdfFile->getClientOriginalName(), PATHINFO_FILENAME)) . '-' . uniqid() . '.' . $pdfFile->guessExtension();
                try {
                    $pdfFile->move($this->getParameter('pdf_directory'), $newFilename);
                    $book->setPdfFile($newFilename);
                } catch (FileException $e) {
                    $this->addFlash('error', 'Erreur lors du téléchargement du fichier PDF.');
                }
            }

            // ✅ Handle Picture Upload
            if ($pictureFile) {
                $newPictureFilename = $slugger->slug(pathinfo($pictureFile->getClientOriginalName(), PATHINFO_FILENAME)) . '-' . uniqid() . '.' . $pictureFile->guessExtension();
                try {
                    $pictureFile->move($this->getParameter('pictures_directory'), $newPictureFilename);
                    $book->setPicture($newPictureFilename);
                } catch (FileException $e) {
                    $this->addFlash('error', 'Erreur lors du téléchargement de l\'image.');
                }
            }

            $entityManager->persist($book);
            $entityManager->flush();

            $this->addFlash('success', 'Livre ajouté avec succès!');
            return $this->redirectToRoute('app_ajjouter');
        }

        return $this->render('admin/ajj.html.twig', ['form' => $form->createView()]);
    }
}
