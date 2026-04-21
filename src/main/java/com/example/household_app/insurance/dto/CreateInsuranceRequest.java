package com.example.household_app.insurance.dto;

import java.time.LocalDate;

public record CreateInsuranceRequest(
        String provider,
        String policyNumber,
        LocalDate startDate,
        LocalDate endDate,
        double premium,
        String periodicity

) {
}
