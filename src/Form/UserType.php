<?php

namespace App\Form;

use App\Entity\User;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;

class UserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('email', EmailType::class, [
            'required' => false,
            'attr' => [
            'type' => 'text', // Change le type de l'input pour ne pas être soumis à la validation HTML email
        ],  // Désactive la contrainte required
        ])
            ->add('roles', ChoiceType::class, [
                'choices'  => [
                    'Administrateur' => 'ROLE_ADMIN',
                    'Étudiant' => 'ROLE_ETUDIANT',
                    'Professeur' => 'ROLE_PROF',
                ],
                'expanded' => true,  // Affiche sous forme de boutons radio
                'multiple' => true, 
                'required' => false,// Permet de sélectionner un seul rôle
                'data' => $options['data']->getRoles(),
                'attr' => [
            'type' => 'text', // Change le type de l'input pour ne pas être soumis à la validation HTML email
        ],  // ✅ Corrige l'affichage du rôle actuel
            ])
            ->add('password', PasswordType::class, [
                'required' => false, 
                'attr' => [
            'type' => 'text', // Change le type de l'input pour ne pas être soumis à la validation HTML email
        ], // Désactive la contrainte required
            ]);
    }



    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => User::class,
        ]);
    }
}
