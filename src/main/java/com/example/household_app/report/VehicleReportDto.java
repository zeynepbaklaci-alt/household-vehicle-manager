package com.example.household_app.report;

import lombok.Data;

@Data
public class VehicleReportDto {

    private String period;
    private int kmDriven;
    private double totalCost;
    private double costPerKm;
    private double avgConsumption;

}
