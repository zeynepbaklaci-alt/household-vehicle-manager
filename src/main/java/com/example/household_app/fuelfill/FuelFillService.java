package com.example.household_app.fuelfill;

import com.example.household_app.fuelfill.dto.FuelFillDto;
import com.example.household_app.vehicle.Vehicle;
import com.example.household_app.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FuelFillService {

    private final FuelFillRepository repo;
    private final VehicleService vehicleService;

    public FuelFill create(UUID vehicleId, FuelFill fuelFill) {

        Vehicle vehicle = vehicleService.getByIdAndAuthorize(vehicleId);

        // 📅 Date validation
        if (fuelFill.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fuel fill date cannot be in the future");
        }

        // ⛽ Odometer validation
        repo.findTopByVehicleIdOrderByOdometerKmDesc(vehicleId)
                .ifPresent(last -> {
                    if (fuelFill.getOdometerKm() < last.getOdometerKm()) {
                        throw new IllegalArgumentException(
                                "Odometer cannot be less than last recorded value (" +
                                        last.getOdometerKm() + ")"
                        );
                    }
                });

        fuelFill.setVehicle(vehicle);

        fuelFill.setTotalCost(
                fuelFill.getLiters() * fuelFill.getPricePerLiter()
        );

        return repo.save(fuelFill);
    }


    public Page<FuelFillDto> getFuelFills(
            UUID vehicleId,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    ) {
        Vehicle vehicle = vehicleService.getByIdAndAuthorize(vehicleId);

        LocalDate fromDate = (from != null) ? from : LocalDate.of(1900, 1, 1);
        LocalDate toDate   = (to != null)   ? to   : LocalDate.now();

        return repo
                .findByVehicleAndDateBetween(vehicle, fromDate, toDate, pageable)
                .map(fuelFill -> new FuelFillDto(
                        fuelFill.getId(),
                        fuelFill.getDate(),
                        fuelFill.getOdometerKm(),
                        fuelFill.getLiters(),
                        fuelFill.getPricePerLiter(),
                        fuelFill.getTotalCost(),
                        fuelFill.getStation(),
                        fuelFill.getNote()
                ));
    }


    public void delete(UUID vehicleId, UUID fuelFillId) {
        Vehicle vehicle = vehicleService.getByIdAndAuthorize(vehicleId);

        FuelFill fuelFill = repo
                .findByIdAndVehicle(fuelFillId, vehicle)
                .orElseThrow(() ->
                        new AccessDeniedException("No access to this fuel fill")
                );

        repo.delete(fuelFill);
    }


}

