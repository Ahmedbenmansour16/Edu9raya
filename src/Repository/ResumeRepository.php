<?php

namespace App\Repository;

use App\Entity\Resume;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Resume>
 */
class ResumeRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Resume::class);
    }

    /**
     * @return Resume[] Returns an array of Resume objects for a specific stage
     */
    public function findByStage($stage): array
    {
        return $this->createQueryBuilder('r')
            ->andWhere('r.stage = :stage')
            ->setParameter('stage', $stage)
            ->getQuery()
            ->getResult();
    }
}