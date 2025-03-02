<?php

namespace App\Controller;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Contracts\HttpClient\HttpClientInterface;

final class GerminiController extends AbstractController
{
    private HttpClientInterface $client;
    private string $apiKey;

    public function __construct(HttpClientInterface $client)
    {
        $this->client = $client;
        $this->apiKey = $_ENV['GEMINI_API_KEY']; // ✅ Load API key from .env
    }

    #[Route('/api/gemini', name: 'gemini_api', methods: ['POST'])]
    public function getGeminiResponse(Request $request): JsonResponse
    {
        $data = json_decode($request->getContent(), true);
        $userPrompt = strtolower(trim($data['prompt'] ?? ''));

        // ✅ Allowed book-related keywords
        $allowedKeywords = ['book', 'author', 'novel', 'library', 'reading', 'literature', 'story', 'publisher', 'fiction', 'non-fiction'];

        // ❌ Reject non-book-related questions
        $isBookRelated = false;
        foreach ($allowedKeywords as $keyword) {
            if (str_contains($userPrompt, $keyword)) {
                $isBookRelated = true;
                break;
            }
        }

        if (!$isBookRelated) {
            return new JsonResponse([
                'error' => '🚫 I only answer book-related questions. Please ask about books, authors, or literature,.'
            ], JsonResponse::HTTP_BAD_REQUEST);
        }

        // ✅ Modify prompt to force Gemini to stay within book-related answers
        $finalPrompt = "You are an AI assistant specialized in books. 
        Only answer questions related to books, authors, literature, or publishing. 
        If the question is not related to books, respond with: 'I only answer book-related questions.'. 
        Question: {$userPrompt}";

        try {
            $response = $this->client->request('POST', 'https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key', [
                'query' => ['key' => $this->apiKey],
                'json' => [
                    'contents' => [['parts' => [['text' => $finalPrompt]]]]
                ]
            ]);

            return new JsonResponse($response->toArray(), JsonResponse::HTTP_OK);
        } catch (\Exception $e) {
            return new JsonResponse([
                'error' => '❌ Failed to fetch response from Gemini API.',
                'details' => $e->getMessage()
            ], JsonResponse::HTTP_INTERNAL_SERVER_ERROR);
        }
    }}