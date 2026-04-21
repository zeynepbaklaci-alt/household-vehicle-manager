package com.example.household_app.insurance;


import com.example.household_app.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InsuranceRepository extends JpaRepository<InsurancePolicy, UUID> {

    List<InsurancePolicy> findByVehicle(Vehicle vehicle);

    List<InsurancePolicy> findByVehicleAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Vehicle vehicle,
            LocalDate to,
            LocalDate from
    );
}

