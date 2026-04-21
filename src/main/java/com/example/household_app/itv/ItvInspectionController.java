package com.example.household_app.itv;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles/{vehicleId}/itv")
@RequiredArgsConstructor
public class ItvInspectionController {

    private final ItvInspectionService service;

    @PostMapping
    public ItvInspection create(
            @PathVariable UUID vehicleId,
            @RequestBody ItvInspection inspection
    ) {
        return service.create(vehicleId, inspection);
    }

    @GetMapping
    public List<ItvInspection> list(
            @PathVariable UUID vehicleId
    ) {
        return service.listByVehicle(vehicleId);
    }
}

