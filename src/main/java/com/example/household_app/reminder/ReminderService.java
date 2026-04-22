package com.example.household_app.reminder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Transactional
    public void dismissAllPendingReminders() {
        reminderRepository.markAllPendingAsSent(LocalDate.now());
    }
}