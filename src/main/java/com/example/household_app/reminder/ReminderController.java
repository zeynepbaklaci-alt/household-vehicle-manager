package com.example.household_app.reminder;

import com.example.household_app.reminder.dto.ReminderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderRepository reminderRepository;

    @GetMapping("/dashboard")
    public List<ReminderResponseDTO> dashboardReminders() {

        LocalDate today = LocalDate.now();

        return reminderRepository
                .findBySentFalseAndRemindAtLessThanEqual(today)
                .stream()
                .map(r -> new ReminderResponseDTO(
                        r.getId(),
                        r.getVehicle().getId(),
                        r.getVehicle().getPlate(),
                        r.getType().name(),
                        r.getRemindAt()
                ))
                .toList();
    }


    @PostMapping("/{id}/dismiss")
    public void dismiss(@PathVariable UUID id) {

        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reminder not found"));

        reminder.setSent(true);
        reminder.setSentAt(LocalDateTime.now());

        reminderRepository.save(reminder);
    }

    @PostMapping("/dismiss-all")
    public void dismissAll() {

        List<Reminder> reminders =
                reminderRepository.findBySentFalseAndRemindAtLessThanEqual(
                        LocalDate.now()
                );

        reminders.forEach(r -> {
            r.setSent(true);
            r.setSentAt(LocalDateTime.now());
        });

        reminderRepository.saveAll(reminders);
    }


}
