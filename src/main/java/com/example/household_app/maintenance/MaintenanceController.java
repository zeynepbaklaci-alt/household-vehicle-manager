package com.example.household_app.maintenance;


import com.example.household_app.maintenance.dto.CreateMaintenanceRequest;
import com.example.household_app.maintenance.dto.MaintenanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles/{vehicleId}/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    public MaintenanceDto create(
            @PathVariable UUID vehicleId,
            @RequestBody CreateMaintenanceRequest request
    ) {
        return maintenanceService.createMaintenance(vehicleId, request);
    }

    @GetMapping
    public List<MaintenanceDto> list(
            @PathVariable UUID vehicleId
    ) {
        return maintenanceService.getMaintenances(vehicleId);
    }
}

