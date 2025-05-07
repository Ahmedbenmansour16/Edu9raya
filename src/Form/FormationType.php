<?php
namespace App\Form;

use App\Entity\Formation;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CollectionType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\OptionsResolver\OptionsResolver;

class FormationType extends AbstractType
{
    public function buildForm(\Symfony\Component\Form\FormBuilderInterface $builder, array $options)
    {
        $builder
          ->add('nom', TextType::class, ['label' => 'Nom'])
          ->add('description', TextareaType::class, ['label' => 'Description'])
          ->add('image', TextType::class, ['label' => 'Image URL'])
          ->add('categorie', TextType::class, ['label' => 'Catégorie'])
          // On ajoute une collection de niveaux
          ->add('niveaux', CollectionType::class, [
              'entry_type'    => \App\Form\NiveauType::class,
              'allow_add'     => true,
              'by_reference'  => false,
              'label'         => 'Niveaux de la Formation (5 niveaux requis)'
          ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
          'data_class' => Formation::class,
        ]);
    }
}
