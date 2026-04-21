package com.example.household_app.fuelfill;

import com.example.household_app.fuelfill.dto.FuelFillDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles/{vehicleId}/fuel-fills")
@RequiredArgsConstructor
public class FuelFillController {

    private final FuelFillService fuelFillService;

    @GetMapping
    public Page<FuelFillDto> list(
            @PathVariable UUID vehicleId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Pageable pageable
    ) {
        return fuelFillService.getFuelFills(vehicleId, from, to, pageable);
    }

    @PostMapping
    public void create(
            @PathVariable UUID vehicleId,
            @RequestBody FuelFill fuelFill
    ) {
        fuelFillService.create(vehicleId, fuelFill);
    }

    @DeleteMapping("/{fuelFillId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID vehicleId,
            @PathVariable UUID fuelFillId
    ) {
        fuelFillService.delete(vehicleId, fuelFillId);
        return ResponseEntity.noContent().build();
    }
}