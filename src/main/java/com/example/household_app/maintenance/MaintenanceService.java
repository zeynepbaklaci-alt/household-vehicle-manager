package com.example.household_app.maintenance;

import com.example.household_app.maintenance.dto.CreateMaintenanceRequest;
import com.example.household_app.maintenance.dto.MaintenanceDto;
import com.example.household_app.vehicle.Vehicle;
import com.example.household_app.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleService vehicleService;

    public MaintenanceDto createMaintenance(
            UUID vehicleId,
            CreateMaintenanceRequest request
    ) {
        Vehicle vehicle = vehicleService.getByIdAndAuthorize(vehicleId);

        Maintenance maintenance = new Maintenance();
        maintenance.setVehicle(vehicle);
        maintenance.setDate(request.date());
        maintenance.setOdometerKm(request.odometerKm());
        maintenance.setCost(request.cost());
        maintenance.setType(request.type());
        maintenance.setWorkshop(request.workshop());
        maintenance.setNote(request.note());

        Maintenance saved = maintenanceRepository.save(maintenance);

        return toDto(saved);
    }

    public List<MaintenanceDto> getMaintenances(UUID vehicleId) {
        Vehicle vehicle = vehicleService.getByIdAndAuthorize(vehicleId);

        return maintenanceRepository
                .findByVehicleOrderByDateAsc(vehicle)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private MaintenanceDto toDto(Maintenance m) {
        return new MaintenanceDto(
                m.getId(),
                m.getDate(),
                m.getOdometerKm(),
                m.getCost(),
                m.getType(),
                m.getWorkshop(),
                m.getNote()
        );
    }
}
