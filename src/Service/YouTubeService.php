<?php
namespace App\Service;

use Symfony\Contracts\HttpClient\HttpClientInterface;

class YouTubeService
{
    private $httpClient;
    private $apiKey;

    public function __construct(HttpClientInterface $httpClient, string $youtubeApiKey)
    {
        $this->httpClient = $httpClient;
        $this->apiKey = $youtubeApiKey;
    }

    public function searchVideos(string $query, int $maxResults = 10): array
    {
        $response = $this->httpClient->request('GET', 'https://www.googleapis.com/youtube/v3/search', [
            'query' => [
                'part' => 'snippet',
                'q' => $query,
                'maxResults' => $maxResults,
                'type' => 'video',
                'key' => $this->apiKey,
            ]
        ]);

        return $response->toArray();
    }

    public function getVideoDetails(string $videoId): array
    {
        $response = $this->httpClient->request('GET', 'https://www.googleapis.com/youtube/v3/videos', [
            'query' => [
                'part' => 'snippet,contentDetails,statistics',
                'id' => $videoId,
                'key' => $this->apiKey,
            ]
        ]);

        return $response->toArray();
    }

    public function getYoutubeIdFromUrl(string $url): ?string
    {
        $pattern = '/(?:youtube\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})/';
        if (preg_match($pattern, $url, $matches)) {
            return $matches[1];
        }
        
        return null;
    }
}