<?php

namespace App\Controller\back;

use App\Entity\Etudiant;
use App\Form\EtudiantType;
use App\Repository\EtudiantRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;

#[Route('/etudiant')]
final class EtudiantController extends AbstractController
{
    
    #[Route(name: 'app_etudiant_index', methods: ['GET'])]
    public function index(EtudiantRepository $etudiantRepository): Response
    {
        return $this->render('back/etudiant/index.html.twig', [
            'etudiants' => $etudiantRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_etudiant_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $etudiant = new Etudiant();
        $form = $this->createForm(EtudiantType::class, $etudiant);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            if (!in_array('ROLE_ETUDIANT', $etudiant->getUser()->getRoles())) {
                $this->addFlash('error', 'L\'utilisateur sélectionné n\'est pas un étudiant.');
                return $this->redirectToRoute('app_etudiant_new');
            }

            $entityManager->persist($etudiant);
            $entityManager->flush();

            return $this->redirectToRoute('app_etudiant_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('back/etudiant/new.html.twig', [
            'etudiant' => $etudiant,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_etudiant_show', methods: ['GET'])]
    public function show(Etudiant $etudiant): Response
    {
        // L'étudiant est automatiquement récupéré par Doctrine grâce à ParamConverter
        return $this->render('back/etudiant/show.html.twig', [
            'etudiant' => $etudiant,
        ]);
    }
    

    #[Route('/{id}/edit', name: 'app_etudiant_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, int $id, EtudiantRepository $etudiantRepository, EntityManagerInterface $entityManager): Response
    {
        $etudiant = $etudiantRepository->find($id);

        if (!$etudiant) {
            throw $this->createNotFoundException('Etudiant non trouvé');
        }

        $form = $this->createForm(EtudiantType::class, $etudiant);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_etudiant_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('back/etudiant/edit.html.twig', [
            'etudiant' => $etudiant,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_etudiant_delete', methods: ['POST'])]
    public function delete(Request $request, int $id, EtudiantRepository $etudiantRepository, EntityManagerInterface $entityManager): Response
    {
        $etudiant = $etudiantRepository->find($id);

        if (!$etudiant) {
            throw $this->createNotFoundException('Etudiant non trouvé');
        }

        if ($this->isCsrfTokenValid('delete' . $etudiant->getId(), $request->request->get('_token'))) {
            $entityManager->remove($etudiant);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_etudiant_index', [], Response::HTTP_SEE_OTHER);
    }

    
}
