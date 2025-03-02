<?php

namespace App\Form;

use App\Entity\Categoriebook;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class CategoriebookType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nomCat', TextType::class, [ // ✅ Vérifiez que ce nom est bien "nomCat"
                'label' => 'Nom de la Catégorie',
                'attr' => ['placeholder' => 'Entrez le nom de la catégorie...', 'class' => 'input']
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Categoriebook::class, // ✅ Assurez-vous que l'entité est bien associée
        ]);
    }
}
