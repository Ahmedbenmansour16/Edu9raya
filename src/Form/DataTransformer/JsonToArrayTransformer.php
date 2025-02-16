<?php

namespace App\Form\DataTransformer;

use Symfony\Component\Form\DataTransformerInterface;

class JsonToArrayTransformer implements DataTransformerInterface
{
    /**
     * Transforme le tableau (stocké en base) vers la chaîne (pour l'afficher dans le formulaire).
     */
    public function transform($array): string
    {
        if (!is_array($array)) {
            return '';
        }

        return json_encode($array);
    }

    /**
     * Transforme la chaîne saisie dans le formulaire en tableau (pour l'entité).
     */
    public function reverseTransform($string): array
    {
        if (!$string) {
            return [];
        }

        $decoded = json_decode($string, true);

        // Si la chaîne n'est pas un JSON valide, on renvoie un tableau vide (ou on peut lever une exception).
        if (!is_array($decoded)) {
            return [];
        }

        return $decoded;
    }
}
