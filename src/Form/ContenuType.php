<?php

namespace App\Form;

use App\Entity\Contenu;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class ContenuType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('type', ChoiceType::class, [
                'choices' => [
                    'Texte' => 'texte',
                    'Vidéo' => 'video',
                    'Image' => 'image',
                    'PDF'   => 'pdf',
                ],
                'label' => 'Type de contenu',
            ])
            ->add('contenu', TextareaType::class, [
                'label' => 'Contenu (texte, URL, chemin de fichier...)',
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Contenu::class,
        ]);
    }
}
