package com.example.household_app.insurance;

import com.example.household_app.insurance.dto.CreateInsuranceRequest;
import com.example.household_app.insurance.dto.InsuranceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles/{vehicleId}/insurance-policies")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @PostMapping
    public InsuranceDto create(
            @PathVariable UUID vehicleId,
            @RequestBody CreateInsuranceRequest request
    ) {
        return insuranceService.createPolicy(vehicleId, request);
    }

    @GetMapping
    public List<InsuranceDto> list(
            @PathVariable UUID vehicleId
    ) {
        return insuranceService.getPolicies(vehicleId);
    }
}

