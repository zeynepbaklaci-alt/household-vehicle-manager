package com.example.household_app.fuelfill.dto;

import java.time.LocalDate;
import java.util.UUID;

public record FuelFillDto(
        UUID id,
        LocalDate date,
        int odometerKm,
        double liters,
        double pricePerLiter,
        double totalCost,
        String station,
        String note
) {}

