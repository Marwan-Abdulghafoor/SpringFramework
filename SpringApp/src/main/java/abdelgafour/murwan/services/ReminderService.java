package abdelgafour.murwan.services;

import org.framework.annotation.Scheduled;
import org.framework.annotation.Service;

@Service
public class ReminderService {
    @Scheduled(fixedRate = 10000) // Remind every 10 seconds
    public void sendReminders() {
        System.out.println("This is a scheduled reminder every 10 seconds - fixed rate");
    }

    @Scheduled(cron = "5 0") // Send daily summary at midnight
    public void sendDailySummary() {
        System.out.println("This is a scheduled reminder every 5 seconds - cron rate");
    }
}
