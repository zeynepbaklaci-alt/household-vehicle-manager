package com.example.household_app.insurance.dto;

import java.time.LocalDate;
import java.util.UUID;

public record InsuranceDto(
        UUID id,
        String provider,
        String policyNumber,
        LocalDate startDate,
        LocalDate endDate,
        double premium,
        String periodicit
) {
}
