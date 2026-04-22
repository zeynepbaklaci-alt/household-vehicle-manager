package com.example.household_app.itv;


import com.example.household_app.itv.dto.ItvInspectionRequestDTO;
import com.example.household_app.itv.dto.ItvInspectionResponseDTO;
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
    public ItvInspectionResponseDTO create(
            @PathVariable UUID vehicleId,
            @RequestBody ItvInspectionRequestDTO dto
    ) {
        // DTO → Entity
        ItvInspection inspection = new ItvInspection();
        inspection.setDate(dto.date());
        inspection.setValidUntil(dto.validUntil());
        inspection.setCost(dto.cost());
        inspection.setPassed(dto.passed());
        inspection.setNote(dto.note());

        ItvInspection saved =
                service.create(vehicleId, inspection);

        // Entity → ResponseDTO
        return new ItvInspectionResponseDTO(
                saved.getId(),
                saved.getDate(),
                saved.getValidUntil(),
                saved.getCost(),
                saved.isPassed(),
                saved.getNote()
        );
    }

    @GetMapping
    public List<ItvInspectionResponseDTO> list(
            @PathVariable UUID vehicleId
    ) {
        return service.listByVehicle(vehicleId)
                .stream()
                .map(itv -> new ItvInspectionResponseDTO(
                        itv.getId(),
                        itv.getDate(),
                        itv.getValidUntil(),
                        itv.getCost(),
                        itv.isPassed(),
                        itv.getNote()
                ))
                .toList();
    }
}


