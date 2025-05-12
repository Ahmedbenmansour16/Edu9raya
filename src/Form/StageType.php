<?php
namespace App\Form;

use App\Entity\CategorieStage;
use App\Entity\Stage;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class StageType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('titre', TextType::class, [
                'label' => 'Titre',
                'required' => true,
                'attr' => ['class' => 'form-control'],
            ])
            ->add('description', TextareaType::class, [
                'label' => 'Description',
                'required' => false,
                'attr' => ['class' => 'form-control', 'rows' => 5],
            ])
            ->add('entreprise', TextType::class, [
                'label' => 'Entreprise',
                'required' => true,
                'attr' => ['class' => 'form-control'],
            ])
            ->add('lieu', TextType::class, [
                'label' => 'Lieu',
                'required' => true,
                'attr' => ['class' => 'form-control'],
            ])
            ->add('duree', IntegerType::class, [
                'label' => 'Durée (mois)',
                'required' => true,
                'attr' => ['class' => 'form-control'],
            ])
            ->add('date_debut', DateType::class, [
                'label' => 'Date de Début',
                'required' => true,
                'widget' => 'single_text',
                'attr' => ['class' => 'form-control'],
            ])
            ->add('categorie', EntityType::class, [
                'label' => 'Catégorie',
                'required' => true,
                'class' => CategorieStage::class,
                'choice_label' => 'nom',
                'attr' => ['class' => 'form-control'],
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Stage::class,
        ]);
    }
}