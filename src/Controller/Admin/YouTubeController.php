<?php
namespace App\Controller\Admin;

use App\Service\YouTubeService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/admin/youtube')]
class YouTubeController extends AbstractController
{
    #[Route('/search', name: 'admin_youtube_search', methods: ['GET'])]
    public function search(Request $request, YouTubeService $youtubeService): JsonResponse
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

    #[Route('/videos/{videoId}', name: 'admin_youtube_video_details', methods: ['GET'])]
    public function getVideoDetails(string $videoId, YouTubeService $youtubeService): JsonResponse
    {
        try {
            $videoDetails = $youtubeService->getVideoDetails($videoId);
            return $this->json($videoDetails);
        } catch (\Exception $e) {
            return $this->json(['error' => 'Erreur lors de la récupération des détails de la vidéo: ' . $e->getMessage()], Response::HTTP_INTERNAL_SERVER_ERROR);
        }
    }
}