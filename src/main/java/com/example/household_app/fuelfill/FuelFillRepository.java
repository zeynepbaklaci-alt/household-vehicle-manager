package com.example.household_app.fuelfill;

import com.example.household_app.vehicle.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FuelFillRepository extends JpaRepository<FuelFill, UUID> {

    List<FuelFill> findByVehicleIdOrderByDateAsc(UUID vehicleId);
    Optional<FuelFill> findByIdAndVehicle(UUID id, Vehicle vehicle);

    Optional<FuelFill> findTopByVehicleIdOrderByOdometerKmDesc(UUID vehicleId);

    Page<FuelFill> findByVehicleAndDateBetween(
            Vehicle vehicle,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    );

    List<FuelFill> findByVehicleAndDateBetween(
            Vehicle vehicle,
            LocalDate from,
            LocalDate to
    );
}

