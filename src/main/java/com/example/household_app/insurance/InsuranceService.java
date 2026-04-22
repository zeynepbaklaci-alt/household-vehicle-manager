package com.example.household_app.insurance;

import com.example.household_app.insurance.dto.CreateInsuranceRequest;
import com.example.household_app.insurance.dto.InsuranceDto;
import com.example.household_app.vehicle.Vehicle;
import com.example.household_app.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final VehicleService vehicleService;
    private final InsuranceReminderService insuranceReminderService;

    public InsuranceDto createPolicy(UUID vehicleId, CreateInsuranceRequest request) {

        Vehicle vehicle = vehicleService.getByIdAndAuthorize(vehicleId);

        InsurancePolicy policy = new InsurancePolicy();
        policy.setId(null); // safety
        policy.setVehicle(vehicle);
        policy.setProvider(request.provider());
        policy.setPolicyNumber(request.policyNumber());
        policy.setStartDate(request.startDate());
        policy.setEndDate(request.endDate());
        policy.setPremium(request.premium());
        policy.setPeriodicity(request.periodicity());

        // 1️⃣ Policy record
        InsurancePolicy saved = insuranceRepository.save(policy);

        // 2️⃣ Insurance reminder create
        insuranceReminderService.createReminderForPolicy(saved);

        // 3️⃣ Response
        return toDto(saved);
    }

    public List<InsuranceDto> getPolicies(UUID vehicleId) {
        Vehicle vehicle = vehicleService.getByIdAndAuthorize(vehicleId);

        return insuranceRepository.findByVehicle(vehicle)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private InsuranceDto toDto(InsurancePolicy p) {
        return new InsuranceDto(
                p.getId(),
                p.getProvider(),
                p.getPolicyNumber(),
                p.getStartDate(),
                p.getEndDate(),
                p.getPremium(),
                p.getPeriodicity()
        );
    }
}
