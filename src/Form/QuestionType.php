<?php

namespace App\Form;

use App\Entity\Question;
use App\Form\DataTransformer\JsonToArrayTransformer;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class QuestionType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('enonce', TextareaType::class, [
                'label' => 'Énoncé'
            ])
            ->add('choix', TextareaType::class, [
                'label' => 'Choix (format JSON, ex: ["A","B","C","D"])'
            ])
            ->add('reponseCorrecte')
        ;

        // On récupère le champ "choix" pour lui appliquer le transformer
        $builder->get('choix')->addModelTransformer(new JsonToArrayTransformer());
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Question::class,
        ]);
    }
}
