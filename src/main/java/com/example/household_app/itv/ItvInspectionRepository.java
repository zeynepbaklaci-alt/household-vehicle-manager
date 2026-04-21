package com.example.household_app.itv;

import com.example.household_app.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ItvInspectionRepository
        extends JpaRepository<ItvInspection, UUID> {

    List<ItvInspection> findByVehicleAndDateBetween(
            Vehicle vehicle,
            LocalDate from,
            LocalDate to
    );

    List<ItvInspection> findByVehicle(Vehicle vehicle);
}
