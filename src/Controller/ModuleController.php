<?php

namespace App\Controller;

use App\Entity\Module;
use App\Form\ModuleType;
use App\Repository\ModuleRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/module')]
class ModuleController extends AbstractController
{
    #[Route('/', name: 'module_index', methods: ['GET'])]
    public function index(ModuleRepository $moduleRepository): Response
    {
        return $this->render('module/index.html.twig', [
            'modules' => $moduleRepository->findAll(),
        ]);
    } 
    
    #[Route('/new', name: 'module_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $module = new Module();
        $form = $this->createForm(ModuleType::class, $module);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($module);
            $entityManager->flush();
    
            return $this->redirectToRoute('module_index');
        }
    
        return $this->render('module/new.html.twig', [
            'module' => $module,
            'form' => $form->createView(),
        ]);
    } 
    
}

    
