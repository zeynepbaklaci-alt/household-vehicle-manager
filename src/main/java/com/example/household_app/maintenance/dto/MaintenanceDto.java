package com.example.household_app.maintenance.dto;

import java.time.LocalDate;
import java.util.UUID;

public record MaintenanceDto(

        UUID id,
        LocalDate date,
        int odometerKm,
        double cost,
        String type,
        String workshop,
        String note
)
{
}
