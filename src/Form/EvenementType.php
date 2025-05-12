<?php

namespace App\Form;

use App\Entity\Evenement;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;

class EvenementType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('titre', TextType::class, [
                'required' => true,
                'attr' => [
                    'placeholder' => 'Entrez le titre de l\'événement'
                ]
            ])
            ->add('description', TextareaType::class, [
                'required' => true,
                'attr' => [
                    'placeholder' => 'Décrivez votre événement (minimum 10 caractères)'
                ]
            ])
            ->add('dateDebut', DateTimeType::class, [
                'widget' => 'single_text',
                'required' => true
            ])
            ->add('dateFin', DateTimeType::class, [
                'widget' => 'single_text',
                'required' => true
            ])
            ->add('lieu', TextType::class, [
                'required' => true,
                'attr' => [
                    'placeholder' => 'Lieu de l\'événement'
                ]
            ])
            ->add('typeEvent', ChoiceType::class, [
                'choices' => [
                    'Sportif' => 'Sportif',
                    'Culturel' => 'Culturel',
                    'Académique' => 'Académique'
                ],
                'required' => true,
                'placeholder' => 'Sélectionnez le type d\'événement'
            ])
            ->add('nbrParticipantsMax', NumberType::class, [
                'required' => true,
                'attr' => [
                    'placeholder' => 'Nombre maximum de participants',
                    'min' => 1
                ]
            ])
            ->add('status', ChoiceType::class, [
                'choices' => [
                    'Planifié' => 'Planifié',
                    'En cours' => 'En cours',
                    'Terminé' => 'Terminé'
                ],
                'required' => true,
                'placeholder' => 'Sélectionnez le status'
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Evenement::class,
        ]);
    }
}
