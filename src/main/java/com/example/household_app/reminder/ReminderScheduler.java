package com.example.household_app.reminder;

import com.example.household_app.insurance.InsuranceReminderService;
import com.example.household_app.itv.ItvReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ItvReminderService itvReminderService;
    private final InsuranceReminderService insuranceReminderService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduleReminders() {
        itvReminderService.createUpcomingItvReminders();
        insuranceReminderService.createUpcomingInsuranceReminders();
    }

}