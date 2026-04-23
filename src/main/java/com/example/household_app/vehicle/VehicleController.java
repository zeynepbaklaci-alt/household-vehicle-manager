package com.example.household_app.vehicle;

import com.example.household_app.vehicle.dto.VehicleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/households/{householdId}/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public List<VehicleDto> list(@PathVariable UUID householdId) {

        return vehicleService.getVehicle(householdId)
                .stream()

                .map(v -> new VehicleDto(
                        v.getId(),
                        v.getPlate(),
                        v.getBrand(),
                        v.getModel(),
                        v.getFuelType(),
                        v.getInitialOdometer()
                ))

                .toList();
    }


    @PostMapping
    public void create(
            @PathVariable UUID householdId,
            @RequestBody VehicleDto dto
    ) {
        vehicleService.createVehicle(
                householdId,
                dto.plate(),
                dto.brand(),
                dto.model(),
                dto.fuelType(),
                dto.initialOdometer()
        );
    }


    @PutMapping("/{vehicleId}")
    public void update(
            @PathVariable UUID householdId,
            @PathVariable UUID vehicleId,
            @RequestBody VehicleDto dto
    ) {
        vehicleService.updateVehicle(
                householdId,
                vehicleId,
                dto.brand(),
                dto.model(),
                dto.fuelType(),
                dto.initialOdometer()
        );
    }


    @DeleteMapping("/{vehicleId}")
    public void delete(
            @PathVariable UUID householdId,
            @PathVariable UUID vehicleId
    ) {
        vehicleService.deleteVehicle(householdId, vehicleId);
    }


}