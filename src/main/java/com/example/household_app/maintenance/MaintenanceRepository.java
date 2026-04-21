package com.example.household_app.maintenance;

import com.example.household_app.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public interface MaintenanceRepository extends JpaRepository<Maintenance, UUID> {

    List<Maintenance> findByVehicleAndDateBetween(
            Vehicle vehicle,
            LocalDate from,
            LocalDate to
    );

    List<Maintenance> findByVehicleOrderByDateAsc(Vehicle vehicle);
}

