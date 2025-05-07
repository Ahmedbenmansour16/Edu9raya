<?php
namespace App\Form;

use App\Entity\Test;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CollectionType;
use Symfony\Component\OptionsResolver\OptionsResolver;

class TestType extends AbstractType
{
    public function buildForm(\Symfony\Component\Form\FormBuilderInterface $builder, array $options)
    {
        $builder
          ->add('questions', CollectionType::class, [
              'entry_type'   => \App\Form\QuestionType::class,
              'allow_add'    => true,
              'by_reference'=> false,
              'label'        => 'Questions (10 questions requises)'
          ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
          'data_class' => Test::class,
        ]);
    }
}
