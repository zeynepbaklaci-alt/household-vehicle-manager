package com.example.household_app.reminder;

import com.example.household_app.itv.ItvReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ItvReminderService itvReminderService;

    /**
     * Runs every day at 02:00 AM
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void runDailyItvReminders() {
        itvReminderService.createUpcomingItvReminders();
    }
}