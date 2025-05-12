<?php

namespace App\Command;

use App\Service\EventReminderService;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;

class CheckEventRemindersCommand extends Command
{
    protected static $defaultName = 'app:check-event-reminders';
    private $reminderService;

    public function __construct(EventReminderService $reminderService)
    {
        parent::__construct();
        $this->reminderService = $reminderService;
    }

    protected function configure(): void
    {
        $this
            ->setDescription('Checks for upcoming events and creates reminders')
            ->setHelp('This command checks for events starting in the next 7 days and creates reminders for registered users');
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $output->writeln('Checking for upcoming events...');
        
        try {
            $this->reminderService->checkUpcomingEvents();
            $output->writeln('Reminders created successfully!');
            return Command::SUCCESS;
        } catch (\Exception $e) {
            $output->writeln('Error: ' . $e->getMessage());
            return Command::FAILURE;
        }
    }
}
