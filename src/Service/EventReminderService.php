<?php

namespace App\Service;

use App\Entity\Notification;
use App\Repository\EvenementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Security\Core\Security;

class EventReminderService
{
    private $evenementRepository;
    private $entityManager;

    public function __construct(
        EvenementRepository $evenementRepository,
        EntityManagerInterface $entityManager
    ) {
        $this->evenementRepository = $evenementRepository;
        $this->entityManager = $entityManager;
    }

    public function checkUpcomingEvents(): void
    {
        $now = new \DateTime();
        $sevenDaysFromNow = (new \DateTime())->modify('+7 days');

        // Get all events starting in the next 7 days
        $upcomingEvents = $this->evenementRepository->createQueryBuilder('e')
            ->where('e.dateDebut BETWEEN :now AND :sevenDays')
            ->andWhere('e.status = :status')
            ->setParameter('now', $now)
            ->setParameter('sevenDays', $sevenDaysFromNow)
            ->setParameter('status', 'Planifié')
            ->getQuery()
            ->getResult();

        foreach ($upcomingEvents as $event) {
            // Check if notification already exists
            $existingNotification = $this->entityManager->getRepository(Notification::class)
                ->findOneBy([
                    'event' => $event,
                    'isRead' => false,
                ]);

            if (!$existingNotification) {
                $daysUntilEvent = $now->diff($event->getDateDebut())->days;
                
                $notification = new Notification();
                $notification->setEvent($event);
                $notification->setMessage(sprintf(
                    'Event "%s" starts in %d days on %s',
                    $event->getTitre(),
                    $daysUntilEvent,
                    $event->getDateDebut()->format('F j, Y')
                ));

                $this->entityManager->persist($notification);
            }
        }

        $this->entityManager->flush();
    }

    public function getUnreadNotifications(): array
    {
        return $this->entityManager->getRepository(Notification::class)
            ->findBy(['isRead' => false], ['createdAt' => 'DESC']);
    }

    public function markAsRead(Notification $notification): void
    {
        $notification->setIsRead(true);
        $this->entityManager->flush();
    }
}
