<?php

namespace App\Repository;

use App\Entity\CategorieStage;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<CategorieStage>
 */
class CategorieStageRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, CategorieStage::class);
    }

    /**
     * Find all categories with pagination
     */
    public function findPaginated(int $page, int $limit): array
    {
        $query = $this->createQueryBuilder('c')
            ->orderBy('c.id', 'ASC')
            ->getQuery();

        return $query->getResult();
    }

    // Add custom methods if needed
}