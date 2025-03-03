<?php
namespace App\Form;

use App\Entity\Prof;
use App\Entity\User;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Validator\Constraints\File;

class ProfType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            // Champ pour l'utilisateur lié avec un rôle ROLE_PROF
            ->add('user', EntityType::class, [
                'class' => User::class,
                'choice_label' => 'email', // Afficher l'email de l'utilisateur
                'query_builder' => function ($repo) {
                    return $repo->createQueryBuilder('u')
                        ->where('u.roles LIKE :role') // Vérifie si "ROLE_PROF" est dans le champ roles
                        ->setParameter('role', '%ROLE_PROF%');
                },
                'placeholder' => 'Sélectionnez un utilisateur',
                'required' => true,
            ])
            ->add('nom', TextType::class)
            ->add('prenom', TextType::class)
            ->add('cin', TextType::class)
            ->add('sexe', TextType::class)
            ->add('matiere', TextType::class)
            ->add('grade', TextType::class)
            ->add('date_recrutement', DateType::class, [
                'widget' => 'single_text',
                'input' => 'datetime',
                'required' => true
            ])
            ->add('telephone', TextType::class)
            
            // Champ photo avec contrainte d'upload de fichier
            ->add('photo', FileType::class, [
                'label' => 'Photo (JPG, PNG, JPEG)',
                'mapped' => false, // Important pour les fichiers non mappés à l'entité
                'required' => false,
                'constraints' => [
                    new File([
                        'maxSize' => '5M',
                        'mimeTypes' => ['image/jpeg', 'image/png', 'image/jpg'],
                        'mimeTypesMessage' => 'Veuillez télécharger une image valide (JPG, PNG, JPEG).',
                    ])
                ],
            ])
            ->add('date_naissance', DateType::class, [
                'widget' => 'single_text',
                'input' => 'datetime',
                'required' => true
            ])
            ->add('nbr_annees', TextType::class)
            ->add('linkdin', TextType::class);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Prof::class,
        ]);
    }
}
