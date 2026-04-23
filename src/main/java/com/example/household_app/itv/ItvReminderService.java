package com.example.household_app.itv;

import com.example.household_app.reminder.Reminder;
import com.example.household_app.reminder.ReminderRepository;
import com.example.household_app.reminder.ReminderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ItvReminderService {

    private final ItvInspectionRepository itvInspectionRepository;
    private final ReminderRepository reminderRepository;

    public void createUpcomingItvReminders() {
        LocalDate today = LocalDate.now();

        itvInspectionRepository.findAll().forEach(itv -> {
            LocalDate remindAt = itv.getValidUntil().minusDays(30);

            if (remindAt.isEqual(today)) {

                boolean exists =
                        reminderRepository.existsByVehicleAndTypeAndRemindAt(
                                itv.getVehicle(),
                                ReminderType.ITV,
                                remindAt
                        );

                if (!exists) {
                    Reminder reminder = new Reminder();
                    reminder.setVehicle(itv.getVehicle());
                    reminder.setType(ReminderType.ITV);
                    reminder.setRemindAt(remindAt);
                    reminder.setSent(false);

                    reminderRepository.save(reminder);
                }
            }
        });
    }



    public void createReminderForInspection(ItvInspection inspection) {

        LocalDate remindAt = inspection.getValidUntil().minusDays(30);

        boolean exists =
                reminderRepository.existsByVehicleAndTypeAndRemindAt(
                        inspection.getVehicle(),
                        ReminderType.ITV,
                        remindAt
                );

        if (exists) {
            return;
        }

        Reminder reminder = new Reminder();
        reminder.setVehicle(inspection.getVehicle());
        reminder.setType(ReminderType.ITV);
        reminder.setRemindAt(remindAt);
        reminder.setSent(false);
        reminder.setSentAt(null);

        reminderRepository.save(reminder);
    }
}