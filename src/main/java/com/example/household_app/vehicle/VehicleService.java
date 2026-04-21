package com.example.household_app.vehicle;

import com.example.household_app.household.Household;
import com.example.household_app.membership.MembershipRepository;
import com.example.household_app.user.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final MembershipRepository membershipRepository;
    private final CurrentUserService currentUserService;

    public List<Vehicle> getVehicle(UUID householdId) {

        UUID currentUserId = currentUserService.getCurrentUserId();

        boolean isMember =
                membershipRepository.isMember(householdId, currentUserId);

        if (!isMember) {
            throw new AccessDeniedException("No access to household");
        }

        return vehicleRepository.findAllByHouseholdId(householdId);
    }

    public Vehicle createVehicle(
            UUID householdId,
            String plate,
            String brand,
            String model,
            FuelType fuelType,
            Integer initialOdometer
    ) {

        UUID currentUserId = currentUserService.getCurrentUserId();

        boolean isMember =
                membershipRepository.isMember(householdId, currentUserId);

        if (!isMember) {
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
     * 🔍 Vehicle fetch + authorization

    public Vehicle getVehicleById(UUID vehicleId) {

        UUID currentUserId = currentUserService.getCurrentUserId();

        return membershipRepository.findAllHouseholdIdsByUserId(currentUserId)
                .stream()
                .map(householdId ->
                        vehicleRepository
                                .findByIdAndHouseholdId(vehicleId, householdId)
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() ->
                        new AccessDeniedException("No access to this vehicle")
                );
    }
     */

    public Vehicle getVehicleById(UUID vehicleId) {

        UUID currentUserId = currentUserService.getCurrentUserId();
        System.out.println("DEBUG userId=" + currentUserId);

        return membershipRepository.findAllHouseholdIdsByUserId(currentUserId)
                .stream()
                .peek(hid -> System.out.println("DEBUG householdId=" + hid))
                .map(householdId ->
                        vehicleRepository.findByIdAndHouseholdId(vehicleId, householdId)
                )
                .peek(v -> System.out.println("DEBUG find result=" + v))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() ->
                        new AccessDeniedException("No access to this vehicle")
                );
    }

    public Vehicle getByIdAndAuthorize(UUID vehicleId) {
        return getVehicleById(vehicleId);
    }

    /**
     * ✏️ UPDATE Vehicle (Edit)
     * Plate intentionally NOT editable
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
     * 🗑 DELETE Vehicle

    public void deleteVehicle(UUID vehicleId) {
        Vehicle vehicle = getByIdAndAuthorize(vehicleId);
        vehicleRepository.delete(vehicle);
    }
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
