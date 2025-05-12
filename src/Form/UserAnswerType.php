<?php

namespace App\Form;

use App\Entity\UserAnswer;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;

class UserAnswerType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('user', EntityType::class, [
                'class' => User::class,
                'choice_label' => 'fullName',
                'label' => 'Utilisateur',
            ])
            ->add('test', EntityType::class, [
                'class' => Test::class,
                'choice_label' => 'id',
                'label' => 'Test',
            ])
            ->add('question', EntityType::class, [
                'class' => Question::class,
                'choice_label' => 'enonce',
                'label' => 'Question',
            ])
            ->add('selectedAnswer', null, [
                'label' => 'Réponse Sélectionnée (1-4)',
                'attr' => ['min' => 1, 'max' => 4],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => UserAnswer::class,
        ]);
    }
}