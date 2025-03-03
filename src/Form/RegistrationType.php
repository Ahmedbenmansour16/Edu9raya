<?php

namespace App\Form;

use App\Entity\User;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\RepeatedType;
use Symfony\Component\Validator\Constraints\Email;

class RegistrationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            // Champ email avec validation
            ->add('email', TextType::class, [
                'constraints' => [
                    new Email([
                        'message' => 'Veuillez entrer une adresse email valide.',
                    ]),
                ],
            ])

            // Champ roles : l'utilisateur choisit son rôle (Admin, Étudiant, Prof)
            ->add('roles', ChoiceType::class, [
                'choices' => [
                    'Étudiant' => 'ROLE_ETUDIANT',
                    'Professeur' => 'ROLE_PROF',
                    'Administrateur' => 'ROLE_ADMIN',
                ],
                'expanded' => true, // Affiche des cases à cocher
                'multiple' => true, // Permet de sélectionner plusieurs rôles si nécessaire
            ])

            // Champ mot de passe avec confirmation du mot de passe
            ->add('password', RepeatedType::class, [
                'type' => PasswordType::class,
                'first_options' => ['label' => 'Mot de passe'],
                'second_options' => ['label' => 'Confirmer le mot de passe'],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => User::class, // Associe ce formulaire à l'entité User
        ]);
    }
}
