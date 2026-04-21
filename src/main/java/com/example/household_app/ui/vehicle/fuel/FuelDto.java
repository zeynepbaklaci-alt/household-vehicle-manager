package com.example.household_app.ui.vehicle.fuel;

import lombok.Data;

@Data
public class FuelDto {

    private String id;

    // ISO-8601 date string (yyyy-MM-dd)
    private String date;

    // odometer at fill time
    private int odometerKm;

    private double liters;
    private double pricePerLiter;

    // liters * pricePerLiter
    private double totalCost;

    private String station;
}