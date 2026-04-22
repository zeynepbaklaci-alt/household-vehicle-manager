package com.example.household_app.reminder.dto;


import java.time.LocalDate;
import java.util.UUID;

public record ReminderResponseDTO(
        UUID id,
        UUID vehicleId,
        String vehicleLabel,
        String type,
        LocalDate remindAt
) {}

