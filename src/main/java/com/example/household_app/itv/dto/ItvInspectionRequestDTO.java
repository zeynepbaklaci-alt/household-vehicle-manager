package com.example.household_app.itv.dto;

import java.time.LocalDate;

public record ItvInspectionRequestDTO(
        LocalDate date,
        LocalDate validUntil,
        double cost,
        boolean passed,
        String note
) {}
