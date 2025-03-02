<?php

namespace App\Form;

use App\Entity\Categoriebook;
use App\Entity\Book;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class BookFilterType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('dispo_book', ChoiceType::class, [
                'choices' => [
                    'Tous' => null,
                    'Disponible' => 'oui',
                    'Non disponible' => 'non',
                ],
                'required' => false,
                'label' => 'Disponibilité',
            ])
            ->add('pdf_file', ChoiceType::class, [
                'choices' => [
                    'Tous' => null,
                    'Avec PDF' => true,
                    'Sans PDF' => false,
                ],
                'required' => false,
                'label' => 'Livres avec PDF',
            ])
            ->add('cat_book', EntityType::class, [
                'class' => Categoriebook::class,
                'choice_label' => 'nomCat',
                'placeholder' => 'Toutes les catégories',
                'required' => false,
                'label' => 'Catégorie',
            ])
            ->add('nom_book', ChoiceType::class, [
                'choices' => [
                    'A-Z (Croissant)' => 'asc',
                    'Z-A (Décroissant)' => 'desc',
                ],
                'required' => false,
                'label' => 'Trier par',
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'method' => 'GET', // ✅ Important for filter forms
            'csrf_protection' => false, // ✅ No need for CSRF in filters
        ]);
    }
}
