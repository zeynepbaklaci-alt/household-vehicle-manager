package com.example.household_app.fuelfill;

import java.util.List;

public class FuelReportCalculator {

    public static FuelReportResult calculate(List<FuelFill> fuelFills) {

        if (fuelFills == null || fuelFills.size() < 2) {
            return new FuelReportResult(0, 0, 0, 0);
        }

        List<FuelFill> sorted =
                fuelFills.stream()
                        .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                        .toList();

        int startKm = sorted.get(0).getOdometerKm();
        int endKm   = sorted.get(sorted.size() - 1).getOdometerKm();
        int kmDriven = endKm - startKm;

        double totalCost =
                sorted.stream()
                        .mapToDouble(FuelFill::getTotalCost)
                        .sum();

        double totalLiters =
                sorted.stream()
                        .mapToDouble(FuelFill::getLiters)
                        .sum();

        double costPerKm =
                kmDriven > 0 ? totalCost / kmDriven : 0;

        double avgConsumption =
                kmDriven > 0 ? (totalLiters / kmDriven) * 100 : 0;

        return new FuelReportResult(
                totalCost,
                kmDriven,
                costPerKm,
                avgConsumption
        );
    }
}