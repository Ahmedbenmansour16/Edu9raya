<?php

namespace App\Repository;

use App\Entity\Stage;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Stage>
 */
class StageRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Stage::class);
    }

    /**
     * Find all stages with pagination
     */
    public function findPaginated(int $page, int $limit): array
    {
        $query = $this->createQueryBuilder('s')
            ->orderBy('s.id', 'ASC')
            ->getQuery();

        return $query->getResult();
    }

    // Add custom methods if needed
}