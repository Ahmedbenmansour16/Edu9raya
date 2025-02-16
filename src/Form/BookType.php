<?php

namespace App\Form;

use App\Entity\Book;
use App\Entity\Categorie;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Validator\Constraints\Image;

class BookType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('id_book', TextType::class, ['label' => 'ID du Livre'])
            ->add('nom_book', TextType::class, ['label' => 'Nom du Livre'])
            ->add('cat_book', EntityType::class, [
                'class' => Categorie::class,
                'choice_label' => 'nom_cat',
                'label' => 'Catégorie',
                'placeholder' => 'Sélectionnez une catégorie',
                'required' => true,
            ])
            ->add('dispo_book', TextType::class, ['label' => 'Disponibilité'])
            ->add('description', TextareaType::class, [
                'label' => 'Description',
                'required' => false,
                'attr' => ['rows' => 4, 'placeholder' => 'Entrez la description du livre...'],
            ])
            ->add('pdfFile', FileType::class, [
                'label' => 'Fichier PDF (Optionnel)',
                'mapped' => false,
                'required' => false,
                'constraints' => [new File(['maxSize' => '5M', 'mimeTypes' => ['application/pdf'], 'mimeTypesMessage' => 'Veuillez télécharger un fichier PDF valide.'])],
            ])

                  // ✅ Add File ID field for Google Drive
                  ->add('file_id', TextType::class, [
                    'label' => 'Google Drive File ID',
                    'required' => false,
                    'mapped' => true,  // Maps to the entity
                    'attr' => ['placeholder' => 'Entrez l\'ID du fichier Google Drive...'],
                ])

                
            ->add('pictureFile', FileType::class, [
                'label' => 'Image du Livre',
                'mapped' => false,
                'required' => false,
                'constraints' => [new Image(['maxSize' => '5M'])],
            ])
            ->add('submit', SubmitType::class, ['label' => 'Ajouter Livre', 'attr' => ['class' => 'btn-primary']]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults(['data_class' => Book::class]);
    }
}
