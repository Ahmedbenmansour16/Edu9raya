<?php

namespace App\Command;

use App\Service\EventReminderService;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Style\SymfonyStyle;

class CheckNotificationsCommand extends Command
{
    protected static $defaultName = 'app:check-notifications';
    private $reminderService;

    public function __construct(EventReminderService $reminderService)
    {
        parent::__construct();
        $this->reminderService = $reminderService;
    }

    protected function configure()
    {
        $this->setDescription('Check for upcoming events and create notifications');
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $io = new SymfonyStyle($input, $output);

        $io->note('Checking for upcoming events...');
        $this->reminderService->checkUpcomingEvents();
        $io->success('Notifications have been created for upcoming events!');

        return Command::SUCCESS;
    }
}
