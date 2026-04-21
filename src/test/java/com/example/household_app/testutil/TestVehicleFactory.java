package com.example.household_app.testutil;

import com.example.household_app.household.Household;
import com.example.household_app.vehicle.FuelType;
import com.example.household_app.vehicle.Vehicle;

import java.util.UUID;

public final class TestVehicleFactory {

    private TestVehicleFactory() {
        // util class → instance yok
    }

    public static Vehicle vehicleFor(Household household) {
        return new Vehicle(
                UUID.randomUUID(),
                "TEST-" + UUID.randomUUID().toString().substring(0, 6), // plate
                "TestBrand",
                "TestModel",
                FuelType.GASOLINE,
                1000,
                household
        );
    }
}