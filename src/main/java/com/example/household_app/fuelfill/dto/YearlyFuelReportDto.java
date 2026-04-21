package com.example.household_app.fuelfill.dto;

public record YearlyFuelReportDto(
        int year,
        double fuelCost,
        double maintenanceCost,
        double insuranceCost,
        double itvCost,
        double totalCost,
        int kmDriven,
        double avgCostPerKm,
        double avgConsumptionPer100K

) {
}
