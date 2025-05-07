<?php

namespace App\Form;

use App\Entity\Book;
use App\Entity\Categoriebook;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Validator\Constraints\Image;

class BookType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('id_book', TextType::class, [
                'label' => 'ID du Livre',
                'constraints' => [
                    new Assert\NotBlank(['message' => "L'ID du livre est obligatoire."]),
                    new Assert\Length([
                        'min' => 3,
                        'max' => 20,
                        'minMessage' => "L'ID doit contenir au moins 3 caractères.",
                        'maxMessage' => "L'ID ne peut pas dépasser 20 caractères."
                    ])
                ],
            ])
            ->add('nom_book', TextType::class, [
                'label' => 'Nom du Livre',
                'constraints' => [
                    new Assert\NotBlank(['message' => "Le nom du livre est obligatoire."]),
                    new Assert\Length(['min' => 3, 'max' => 255])
                ],
            ])
            ->add('cat_book', EntityType::class, [
                'class' => Categoriebook::class,
                'choice_label' => 'nom_cat',
                'label' => 'Catégorie',
                'placeholder' => 'Sélectionnez une catégorie',
                'constraints' => [new Assert\NotBlank(['message' => "Veuillez sélectionner une catégorie."])],
            ])
            ->add('dispo_book', TextType::class, [
                'label' => 'Disponibilité',
                'constraints' => [
                    new Assert\NotBlank(['message' => "La disponibilité est obligatoire."]),
                    new Assert\Choice(['choices' => ['oui', 'non'], 'message' => "La disponibilité doit être 'oui' ou 'non'."])
                ],
            ])
            ->add('description', TextareaType::class, [
                'label' => 'Description',
                'required' => false,
                'attr' => ['rows' => 4, 'placeholder' => 'Entrez la description du livre...'],
                'constraints' => [new Assert\Length(['max' => 1000])]
            ])
            ->add('file_id', TextType::class, [
                'label' => 'Google Drive File ID',
                'required' => false,
                'constraints' => [
                    new Assert\Length(['max' => 50, 'maxMessage' => "L'ID ne peut pas dépasser 50 caractères."])
                ],
            ])
            ->add('pdfFile', FileType::class, [
                'label' => 'Fichier PDF (Optionnel)',
                'mapped' => false,
                'required' => false,
                'constraints' => [
                    new File([
                        'maxSize' => '5M',
                        'mimeTypes' => ['application/pdf'],
                        'mimeTypesMessage' => 'Veuillez télécharger un fichier PDF valide.',
                    ])
                ],
            ])
            ->add('pictureFile', FileType::class, [
                'label' => 'Image du Livre',
                'mapped' => false,
                'required' => false,
                'constraints' => [new Image(['maxSize' => '5M'])],
            ]);
      
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults(['data_class' => Book::class]);
    }
}
