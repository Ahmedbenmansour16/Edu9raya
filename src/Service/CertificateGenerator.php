<?php
// src/Service/CertificateGenerator.php
namespace App\Service;

use Knp\Snappy\Pdf;
use Symfony\Component\HttpFoundation\Response;
use Twig\Environment;

class CertificateGenerator
{
    private $pdf;
    private $twig;
    private $projectDir;

    public function __construct(
        Pdf $pdf, 
        Environment $twig, 
        string $projectDir
    ) {
        $this->pdf = $pdf;
        $this->twig = $twig;
        $this->projectDir = $projectDir;
    }

    public function generateCertificatePdf($formation, $user, $score, $date)
    {
        // Rendre le template Twig
        $html = $this->twig->render('student/certificate_pdf.html.twig', [
            'formation' => $formation,
            'user' => $user,
            'score' => $score,
            'date' => $date
        ]);

        // Générer le PDF
        $filename = 'certificat_' . $this->sanitizeFilename($formation->getNom()) . '_' . date('Y-m-d') . '.pdf';
        
        // Option pour s'assurer que les polices et images sont correctement chargées
        $this->pdf->setOption('enable-local-file-access', true);
        
        return [
            'binary' => $this->pdf->getOutputFromHtml($html),
            'filename' => $filename
        ];
    }
    
    private function sanitizeFilename($filename)
    {
        // Remplacer les caractères non alphanumériques par des tirets
        return preg_replace('/[^a-z0-9]+/', '-', strtolower($filename));
    }
}