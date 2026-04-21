package com.example.household_app.fuelfill.dto;

public record MonthlyFuelReportDto(
        int year,
        int month,
        double fuelCost,
        double maintenanceCost,
        double insuranceCost,
        double itvCost,
        double totalCost,
        int kmDriven,
        double costPerKm,
        double avgConsumptionPer100Km
) {}

