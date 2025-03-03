<?php
namespace App\Controller\Admin;

use App\Entity\Test;
use App\Entity\Niveau;
use App\Entity\Contenu;
use App\Entity\Question;
use App\Entity\Categorie;
use App\Entity\Formation;
use App\Service\YouTubeService;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;

#[Route('/admin/formation')]
class FormationController extends AbstractController
{
    #[Route('/', name: 'admin_formation_index', methods: ['GET'])]
    public function index(Request $request, EntityManagerInterface $em): Response
    {
        // Recherche
        $searchTerm = $request->query->get('search');
        if ($searchTerm) {
            $queryBuilder = $em->createQueryBuilder()
                ->select('f')
                ->from(Formation::class, 'f')
                ->leftJoin('f.categorie', 'c')
                ->where('f.nom LIKE :search')
                ->orWhere('f.description LIKE :search')
                ->orWhere('c.nom LIKE :search')
                ->setParameter('search', '%'.$searchTerm.'%')
                ->orderBy('f.dateCreation', 'DESC');
            $formations = $queryBuilder->getQuery()->getResult();
        } else {
            $formations = $em->getRepository(Formation::class)->findBy([], ['dateCreation' => 'DESC']);
        }

        // Quelques indicateurs (cards)
        $totalFormations = count($formations);
        $totalCategories = $em->getRepository(Categorie::class)->count([]);

        return $this->render('admin/formation/index.html.twig', [
            'formations' => $formations,
            'totalFormations' => $totalFormations,
            'totalCategories' => $totalCategories,
        ]);
    }

    #[Route('/show/{id}', name: 'admin_formation_show', methods: ['GET'])]
    public function show(Formation $formation): Response
    {
        return $this->render('admin/formation/show.html.twig', [
            'formation' => $formation,
        ]);
    }

    #[Route('/edit/{id}', name: 'admin_formation_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Formation $formation, EntityManagerInterface $em, YouTubeService $youtubeService): Response
    {
        if ($request->isMethod('POST')) {
            // 1. Mise à jour des informations de base
            $formation->setNom($request->request->get('nom'));
            $formation->setDescription($request->request->get('description'));

            // Mise à jour de la catégorie
            $categorieId = $request->request->get('categorie');
            $categorie = $em->getRepository(Categorie::class)->find($categorieId);
            if (!$categorie) {
                $this->addFlash('danger', 'Catégorie invalide.');
                return $this->redirectToRoute('admin_formation_edit', ['id' => $formation->getId()]);
            }
            $formation->setCategorie($categorie);

            // Gestion de l'upload d'une nouvelle image si elle est fournie
            $imageFile = $request->files->get('imageFile');
            if ($imageFile) {
                $newFilename = uniqid() . '.' . $imageFile->guessExtension();
                $imageFile->move($this->getParameter('uploads_dir'), $newFilename);
                $formation->setImage($newFilename);
            }

            // 2. Mise à jour des niveaux et de leurs contenus
            // On récupère toutes les données envoyées pour les niveaux
            $niveauxData = $request->request->all('niveaux');
            // Pour chaque niveau existant
            foreach ($formation->getNiveaux() as $niveau) {
                $ordre = $niveau->getOrdre();
                if (isset($niveauxData[$ordre])) {
                    $niveauData = $niveauxData[$ordre];

                    // a) Mise à jour des contenus existants
                    if (isset($niveauData['contenus']['existing']) && is_array($niveauData['contenus']['existing'])) {
                        foreach ($niveauData['contenus']['existing'] as $contenuId => $contenuData) {
                            // On parcourt les contenus de ce niveau pour trouver celui dont l'ID correspond
                            foreach ($niveau->getContenus() as $contenu) {
                                if ($contenu->getId() == $contenuId) {
                                    if ($contenu->getType() === 'description') {
                                        $contenu->setDescription($contenuData['description'] ?? '');
                                    } elseif ($contenu->getType() === 'youtube') {
                                        $youtubeValue = $contenuData['youtubeId'] ?? '';
                                        // Extraire l'ID YouTube si une URL complète est fournie
                                        $youtubeId = $youtubeService->getYoutubeIdFromUrl($youtubeValue) ?: $youtubeValue;
                                        $contenu->setYoutubeId($youtubeId);
                                    } else {
                                        // Pour un contenu de type fichier, on vérifie si un nouveau fichier est uploadé
                                        $file = $request->files->get('niveaux')[$ordre]['contenus']['existing'][$contenuId]['file'] ?? null;
                                        if ($file && $file instanceof \Symfony\Component\HttpFoundation\File\UploadedFile) {
                                            $fileName = uniqid() . '.' . $file->guessExtension();
                                            $file->move($this->getParameter('uploads_dir'), $fileName);
                                            $contenu->setFichier($fileName);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // b) Ajout de nouveaux contenus
                    if (isset($niveauData['contenus']['new']) && is_array($niveauData['contenus']['new'])) {
                        foreach ($niveauData['contenus']['new'] as $idx => $newItem) {
                            $type = $newItem['type'] ?? null;
                            $contenu = new Contenu();
                            $contenu->setType($type);
                            $contenu->setNiveau($niveau);
                            
                            if ($type === 'description') {
                                $contenu->setDescription($newItem['description'] ?? '');
                            } elseif ($type === 'youtube') {
                                $youtubeValue = $newItem['youtubeId'] ?? '';
                                // Extraire l'ID YouTube si une URL complète est fournie
                                $youtubeId = $youtubeService->getYoutubeIdFromUrl($youtubeValue) ?: $youtubeValue;
                                $contenu->setYoutubeId($youtubeId);
                            } else {
                                // Récupérer le fichier uploadé pour ce nouveau contenu
                                $file = $request->files->get('niveaux')[$ordre]['contenus']['new'][$idx]['file'] ?? null;
                                if ($file && $file instanceof \Symfony\Component\HttpFoundation\File\UploadedFile) {
                                    $fileName = uniqid() . '.' . $file->guessExtension();
                                    $file->move($this->getParameter('uploads_dir'), $fileName);
                                    $contenu->setFichier($fileName);
                                }
                            }
                            $niveau->addContenu($contenu);
                        }
                    }
                }
            }

            // 3. Mise à jour du test et des questions associées
            $testData = $request->request->all('test');
            if ($formation->getTest()) {
                $test = $formation->getTest();
                if (isset($testData['questions']['existing']) && is_array($testData['questions']['existing'])) {
                    foreach ($testData['questions']['existing'] as $questionId => $qData) {
                        // On parcourt les questions du test pour trouver celle à modifier
                        foreach ($test->getQuestions() as $question) {
                            if ($question->getId() == $questionId) {
                                $question->setEnonce($qData['enonce'] ?? '');
                                $question->setAnswer1($qData['answer1'] ?? '');
                                $question->setAnswer2($qData['answer2'] ?? '');
                                $question->setAnswer3($qData['answer3'] ?? '');
                                $question->setAnswer4($qData['answer4'] ?? '');
                                $question->setCorrectAnswer((int)($qData['correct'] ?? 1));
                            }
                        }
                    }
                }
                // Si vous souhaitez gérer l'ajout de nouvelles questions, vous pouvez traiter ici $testData['questions']['new']
            }

            // 4. Sauvegarde en base
            $em->flush();
            $this->addFlash('success', 'Formation modifiée avec succès.');
            return $this->redirectToRoute('admin_formation_index');
        }

        return $this->render('admin/formation/edit.html.twig', [
            'formation'  => $formation,
            'categories' => $em->getRepository(Categorie::class)->findAll(),
        ]);
    }
    
    #[Route('/delete/{id}', name: 'admin_formation_delete', methods: ['POST'])]
    public function delete(Request $request, Formation $formation, EntityManagerInterface $em): Response
    {
        if ($this->isCsrfTokenValid('delete'.$formation->getId(), $request->request->get('_token'))) {
            $em->remove($formation);
            $em->flush();
        }
        return $this->redirectToRoute('admin_formation_index');
    }

    // -------------------------------------------------------------
    // Le "wizard" pour créer une nouvelle formation
    // -------------------------------------------------------------
    #[Route('/new', name: 'admin_formation_new', methods: ['GET','POST'])]
    public function new(Request $request, EntityManagerInterface $em, YouTubeService $youtubeService): Response
    {
        if ($request->isMethod('POST')) {
            // 1) Récupérer les données POST
            $nom = $request->request->get('nom');
            $description = $request->request->get('description');
            $categorieId = $request->request->get('categorie');
            $imageFile = $request->files->get('imageFile');
            $niveauxData = $request->request->all('niveaux');
            $testData = $request->request->all('test');
    
            // 2) Créer la Formation
            $formation = new Formation();
            $formation->setNom($nom);
            $formation->setDescription($description);
            $formation->setDateCreation(new \DateTime());
    
            // Gérer la catégorie
            $cat = $em->getRepository(Categorie::class)->find($categorieId);
            if (!$cat) {
                $this->addFlash('danger', 'Catégorie invalide.');
                return $this->redirectToRoute('admin_formation_new');
            }
            $formation->setCategorie($cat);
    
            // Gérer l'upload de l'image
            if ($imageFile) {
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move($this->getParameter('uploads_dir'), $newFilename);
                $formation->setImage($newFilename);
            }
    
            // 3) Créer les 5 niveaux
            for ($i = 1; $i <= 5; $i++) {
                if (!isset($niveauxData[$i])) {
                    continue;
                }
                
                $niveauData = $niveauxData[$i];
                $niveau = new Niveau();
                $niveau->setOrdre($i);
                $niveau->setFormation($formation);
    
                // Récupérer la liste des contenus
                if (isset($niveauData['contenus']) && is_array($niveauData['contenus'])) {
                    foreach ($niveauData['contenus'] as $idx => $contenuItem) {
                        $type = $contenuItem['type'] ?? null;
                        
                        $contenu = new Contenu();
                        $contenu->setType($type);
                        $contenu->setNiveau($niveau);
    
                        if ($type === 'description') {
                            $contenu->setDescription($contenuItem['description'] ?? '');
                        } elseif ($type === 'youtube') {
                            $youtubeValue = $contenuItem['youtubeId'] ?? '';
                            // Extraire l'ID YouTube si une URL complète est fournie
                            $youtubeId = $youtubeService->getYoutubeIdFromUrl($youtubeValue) ?: $youtubeValue;
                            $contenu->setYoutubeId($youtubeId);
                        } else {
                            // Récupérer le fichier correctement
                            $file = $request->files->get('niveaux')[$i]['contenus'][$idx]['file'] ?? null;
                            
                            if ($file instanceof UploadedFile) {
                                $fileName = uniqid().'.'.$file->guessExtension();
                                $file->move($this->getParameter('uploads_dir'), $fileName);
                                $contenu->setFichier($fileName);
                            }
                        }
                        $niveau->addContenu($contenu);
                    }
                }
                $formation->addNiveau($niveau);
            }
    
            // 4) Créer le Test et ses questions
            $test = new Test();
            $test->setFormation($formation);
            if (isset($testData['questions']) && is_array($testData['questions'])) {
                $questionCount = 0;
                foreach ($testData['questions'] as $qData) {
                    // Vérifier que l'énoncé n'est pas vide
                    if (empty($qData['enonce'])) {
                        continue;
                    }
                    
                    $question = new Question();
                    $question->setEnonce($qData['enonce'] ?? '');
                    $question->setAnswer1($qData['answer1'] ?? '');
                    $question->setAnswer2($qData['answer2'] ?? '');
                    $question->setAnswer3($qData['answer3'] ?? '');
                    $question->setAnswer4($qData['answer4'] ?? '');
                    $question->setCorrectAnswer((int)($qData['correct'] ?? 1));
                    $test->addQuestion($question);
                    $questionCount++;
                }
                
                // Vérifier qu'il y a au moins 3 questions
                if ($questionCount < 3) {
                    $this->addFlash('danger', 'Le test doit contenir au moins 3 questions.');
                    return $this->redirectToRoute('admin_formation_new');
                }
            }
            $formation->setTest($test);
            // 5) Sauvegarder
            $em->persist($formation);
            $em->flush();
    
            $this->addFlash('success', 'Formation créée avec succès.');
            return $this->redirectToRoute('admin_formation_index');
        }
    
        // Rendre la vue du wizard
        $categories = $em->getRepository(Categorie::class)->findAll();
        return $this->render('admin/formation/new_wizard.html.twig', [
            'categories' => $categories,
        ]);
    }
    
    #[Route('/stats', name: 'admin_formation_stats', methods: ['GET'])]
    public function stats(EntityManagerInterface $em): Response
    {
        // Nombre de formations par catégorie
        $qb = $em->createQueryBuilder();
        $qb->select('c.nom AS category, COUNT(f.id) AS count')
           ->from(Formation::class, 'f')
           ->leftJoin('f.categorie', 'c')
           ->groupBy('c.nom')
           ->orderBy('c.nom', 'ASC');

        $rows = $qb->getQuery()->getResult();
        $labels = [];
        $counts = [];
        foreach ($rows as $row) {
            $labels[] = $row['category'] ?? 'Sans catégorie';
            $counts[] = (int)$row['count'];
        }

        return $this->json([
            'labels' => $labels,
            'counts' => $counts,
        ]);
    }

    #[Route('/stats/time', name: 'admin_formation_stats_time', methods: ['GET'])]
    public function statsTime(EntityManagerInterface $em): Response
    {
        // Évolution du nombre de formations par mois (année-mois)
        $qb = $em->createQueryBuilder();
        $qb->select("DATE_FORMAT(f.dateCreation, '%Y-%m') AS period, COUNT(f.id) AS count")
           ->from(Formation::class, 'f')
           ->groupBy('period')
           ->orderBy('period', 'ASC');

        $rows = $qb->getQuery()->getResult();
        $labels = [];
        $counts = [];
        foreach ($rows as $row) {
            $labels[] = $row['period'];
            $counts[] = (int)$row['count'];
        }

        return $this->json([
            'labels' => $labels,
            'counts' => $counts,
        ]);
    }
    
    #[Route('/youtube/search', name: 'admin_formation_youtube_search', methods: ['GET'])]
    public function youtubeSearch(Request $request, YouTubeService $youtubeService): Response
    {
        $query = $request->query->get('q');
        if (!$query) {
            return $this->json(['error' => 'Le paramètre de recherche est requis'], Response::HTTP_BAD_REQUEST);
        }

        try {
            $results = $youtubeService->searchVideos($query);
            return $this->json($results);
        } catch (\Exception $e) {
            return $this->json(['error' => 'Erreur lors de la recherche YouTube: ' . $e->getMessage()], Response::HTTP_INTERNAL_SERVER_ERROR);
        }
    }
}