package com.example.household_app.fuelfill;

public record FuelReportResult(
        double totalFuelCost,
        int kmDriven,
        double costPerKm,
        double avgConsumptionPer100Km
) {
}