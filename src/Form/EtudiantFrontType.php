<?php
namespace App\Form;

use App\Entity\Etudiant;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;

class EtudiantFrontType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom')
            ->add('prenom')
            ->add('date_naissance', DateType::class, [
                'widget' => 'single_text', // Utiliser un sélecteur de date HTML5
                'format' => 'yyyy-MM-dd', // Format de date
            ])
            ->add('annee')
            ->add('specialisation')
            ->add('telephone', null, [
                'required' => false, // Champ optionnel
            ])
            ->add('adresse', null, [
                'required' => false, // Champ optionnel
            ])
            ->add('photo', null, [
                'required' => false, // Champ optionnel
            ])
            ->add('date_inscription', DateType::class, [
                'widget' => 'single_text', // Utiliser un sélecteur de date HTML5
                'format' => 'yyyy-MM-dd', // Format de date
            ])
            ->add('statut')
            ->add('moyenne')
            ->add('cours')
            ->add('sexe', ChoiceType::class, [
                'choices' => [
                    'Homme' => 'Homme',
                    'Femme' => 'Femme',
                    'Autre' => 'Autre',
                ],
                'placeholder' => 'Sélectionnez votre sexe', // Texte par défaut
                'required' => true,
            ])
            ->add('cin')
            ->add('etat_paiement', CheckboxType::class, [
                'label' => 'Paiement effectué',
                'required' => false, // Champ optionnel
            ])
            ->add('linkdin', null, [
                'required' => false, // Champ optionnel
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Etudiant::class, // Associer ce formulaire à l'entité Etudiant
        ]);
    }
}