package com.example.household_app.insurance;

import com.example.household_app.reminder.Reminder;
import com.example.household_app.reminder.ReminderRepository;
import com.example.household_app.reminder.ReminderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InsuranceReminderService {

    private final InsuranceRepository insuranceRepository;
    private final ReminderRepository reminderRepository;


    public void createReminderForPolicy(InsurancePolicy policy) {

        LocalDate remindAt = policy.getEndDate().minusDays(30);

        boolean exists =
                reminderRepository.existsByVehicleAndTypeAndRemindAt(
                        policy.getVehicle(),
                        ReminderType.INSURANCE,
                        remindAt
                );

        if (exists) {
            return;
        }

        Reminder reminder = new Reminder();
        reminder.setVehicle(policy.getVehicle());
        reminder.setType(ReminderType.INSURANCE);
        reminder.setRemindAt(remindAt);
        reminder.setSent(false);
        reminder.setSentAt(null);

        reminderRepository.save(reminder);
    }

    public void createUpcomingInsuranceReminders() {

        LocalDate today = LocalDate.now();

        insuranceRepository.findAll().forEach(policy -> {

            LocalDate remindAt = policy.getEndDate().minusDays(30);

            if (remindAt.isEqual(today)) {

                boolean exists =
                        reminderRepository.existsByVehicleAndTypeAndRemindAt(
                                policy.getVehicle(),
                                ReminderType.INSURANCE,
                                remindAt
                        );

                if (!exists) {
                    Reminder reminder = new Reminder();
                    reminder.setVehicle(policy.getVehicle());
                    reminder.setType(ReminderType.INSURANCE);
                    reminder.setRemindAt(remindAt);
                    reminder.setSent(false);

                    reminderRepository.save(reminder);
                }
            }
        });
    }
}