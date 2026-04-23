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
    private final ItvReminderService itvReminderService;

    public ItvInspection create(
            UUID vehicleId,
            ItvInspection inspection
    ) {

        Vehicle vehicle =
                vehicleService.getByIdAndAuthorize(vehicleId);

        inspection.setId(null);        // safety
        inspection.setVehicle(vehicle);

        // 1️⃣ ITV record
        ItvInspection saved = repository.save(inspection);

        // 2️⃣ Reminder create for itv
        itvReminderService.createReminderForInspection(saved);

        // 3️⃣ Bring recorded itv
        return saved;
    }


    public List<ItvInspection> listByVehicle(UUID vehicleId) {
        Vehicle vehicle =
                vehicleService.getByIdAndAuthorize(vehicleId);

        return repository.findByVehicle(vehicle);
    }
}