package com.example.household_app.itv;

import com.example.household_app.vehicle.Vehicle;
import com.example.household_app.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItvInspectionService {

    private final ItvInspectionRepository repository;
    private final VehicleService vehicleService;

    public ItvInspection create(
            UUID vehicleId,
            ItvInspection inspection
    ) {
        Vehicle vehicle =
                vehicleService.getByIdAndAuthorize(vehicleId);

        inspection.setId(null);        // safety
        inspection.setVehicle(vehicle);

        return repository.save(inspection);
    }

    public List<ItvInspection> listByVehicle(UUID vehicleId) {
        Vehicle vehicle =
                vehicleService.getByIdAndAuthorize(vehicleId);

        return repository.findByVehicle(vehicle);
    }
}