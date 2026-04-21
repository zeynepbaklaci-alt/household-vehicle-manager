package com.example.household_app.vehicle.dto;

import com.example.household_app.vehicle.FuelType;

import java.util.UUID;

public record VehicleDto(
        UUID id,
        String plate,
        String brand,
        String model,
        FuelType fuelType,
        Integer initialOdometer

) {}