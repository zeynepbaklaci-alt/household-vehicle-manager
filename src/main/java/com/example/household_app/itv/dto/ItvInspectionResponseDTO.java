package com.example.household_app.itv.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ItvInspectionResponseDTO(
        UUID id,
        LocalDate date,
        LocalDate validUntil,
        double cost,
        boolean passed,
        String note
) {}
