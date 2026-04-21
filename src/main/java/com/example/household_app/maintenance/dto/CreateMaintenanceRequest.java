package com.example.household_app.maintenance.dto;

import java.time.LocalDate;

public record CreateMaintenanceRequest(
        LocalDate date,
        int odometerKm,
        double cost,
        String type,
        String workshop,
        String note
) {
}
