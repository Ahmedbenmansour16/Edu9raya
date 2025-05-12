<?php

namespace App\Repository;

use App\Entity\UserAnswer;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<UserAnswer>
 *
 * @method UserAnswer|null find($id, $lockMode = null, $lockVersion = null)
 * @method UserAnswer|null findOneBy(array $criteria, array $orderBy = null)
 * @method UserAnswer[]    findAll()
 * @method UserAnswer[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class UserAnswerRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, UserAnswer::class);
    }

    /**
     * Find user answers by user and test
     *
     * @param mixed $user The user entity or user ID
     * @param mixed $test The test entity or test ID
     * @return UserAnswer[]
     */
    public function findByUserAndTest($user, $test): array
    {
        $qb = $this->createQueryBuilder('ua')
            ->andWhere('ua.user = :user')
            ->andWhere('ua.test = :test')
            ->setParameter('user', $user)
            ->setParameter('test', $test)
            ->orderBy('ua.id', 'ASC');

        return $qb->getQuery()->getResult();
    }

    // Uncomment and customize if you need to add custom methods
    /*
    public function save(UserAnswer $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);
        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function remove(UserAnswer $entity, bool $flush = false): void
    {
        $this->getEntityManager()->remove($entity);
        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }
    */
}