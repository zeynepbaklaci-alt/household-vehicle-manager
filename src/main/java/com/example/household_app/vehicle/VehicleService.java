package com.example.household_app.vehicle;

import com.example.household_app.household.Household;
import com.example.household_app.membership.MembershipRepository;
import com.example.household_app.user.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final MembershipRepository membershipRepository;
    private final CurrentUserService currentUserService;

    /**
     * ✅ List vehicles for a household (member only)
     */
    public List<Vehicle> getVehicle(UUID householdId) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        if (!membershipRepository.isMember(householdId, currentUserId)) {
            throw new AccessDeniedException("No access to household");
        }

        return vehicleRepository.findAllByHouseholdId(householdId);
    }

    /**
     * ✅ Create vehicle (member only)
     */
    public Vehicle createVehicle(
            UUID householdId,
            String plate,
            String brand,
            String model,
            FuelType fuelType,
            Integer initialOdometer
    ) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        if (!membershipRepository.isMember(householdId, currentUserId)) {
            throw new AccessDeniedException("No access to household");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(plate);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setFuelType(fuelType);
        vehicle.setInitialOdometer(initialOdometer);

        Household household = new Household();
        household.setId(householdId);
        vehicle.setHousehold(household);

        return vehicleRepository.save(vehicle);
    }

    /**
     * ✅ Fetch vehicle ONLY if current user is member of its household
     * Used by Insurance, ITV, Fuel, etc.
     */
    public Vehicle getByIdAndAuthorize(UUID vehicleId) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        return membershipRepository.findAllHouseholdIdsByUserId(currentUserId)
                .stream()
                .map(householdId ->
                        vehicleRepository.findByIdAndHouseholdId(vehicleId, householdId)
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() ->
                        new AccessDeniedException("No access to this vehicle")
                );
    }

    /**
     * ✅ Update vehicle (member only)
     */
    public Vehicle updateVehicle(
            UUID householdId,
            UUID vehicleId,
            String brand,
            String model,
            FuelType fuelType,
            Integer initialOdometer
    ) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        if (!membershipRepository.isMember(householdId, currentUserId)) {
            throw new AccessDeniedException("No access to household");
        }

        Vehicle vehicle = vehicleRepository
                .findByIdAndHouseholdId(vehicleId, householdId)
                .orElseThrow(() ->
                        new AccessDeniedException("No access to this vehicle")
                );

        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setFuelType(fuelType);
        vehicle.setInitialOdometer(initialOdometer);

        return vehicleRepository.save(vehicle);
    }

    /**
     * ✅ Delete vehicle (member only)
     */
    public void deleteVehicle(UUID householdId, UUID vehicleId) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        if (!membershipRepository.isMember(householdId, currentUserId)) {
            throw new AccessDeniedException("No access to household");
        }

        Vehicle vehicle = vehicleRepository
                .findByIdAndHouseholdId(vehicleId, householdId)
                .orElseThrow(() ->
                        new AccessDeniedException("No access to this vehicle")
                );

        vehicleRepository.delete(vehicle);
    }
}