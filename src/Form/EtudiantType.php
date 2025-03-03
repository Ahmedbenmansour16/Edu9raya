<?php

namespace App\Form;

use App\Entity\Etudiant;
use App\Entity\User;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class EtudiantType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom')
            ->add('prenom')
            ->add('date_naissance', null, [
                'widget' => 'single_text',
            ])
            ->add('annee')
            ->add('specialisation')
            ->add('telephone')
            ->add('adresse')
            ->add('photo')
            ->add('date_inscription', null, [
                'widget' => 'single_text',
            ])
            ->add('statut')
            ->add('moyenne')
            ->add('cours')
            ->add('sexe')
            ->add('cin')
            ->add('etat_paiement')
            ->add('linkdin')
            ->add('user', EntityType::class, [
                'class' => User::class,
                'choice_label' => 'email',
                'query_builder' => function ($repo) {
                    return $repo->createQueryBuilder('u')
                        ->where('u.roles LIKE :role')
                        ->setParameter('role', '%ROLE_ETUDIANT%');
                },
                'placeholder' => 'Sélectionnez un utilisateur',
                'required' => true,
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Etudiant::class,
        ]);
    }
}
