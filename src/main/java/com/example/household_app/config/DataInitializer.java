package com.example.household_app.config;

import com.example.household_app.household.Household;
import com.example.household_app.household.HouseholdRepository;
import com.example.household_app.user.Role;
import com.example.household_app.user.User;
import com.example.household_app.user.UserRepository;
import com.example.household_app.vehicle.FuelType;
import com.example.household_app.vehicle.Vehicle;
import com.example.household_app.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            UserRepository userRepository,
            HouseholdRepository householdRepository,
            VehicleRepository vehicleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            /* ===== USER ===== */

            User user = userRepository.findByEmail("test@example.com")
                    .orElseGet(() -> {
                        User u = new User();
                        u.setEmail("test@example.com");
                        u.setFullName("Test User");
                        u.setPasswordHash(passwordEncoder.encode("123456"));
                        u.setRole(Role.USER);
                        return userRepository.save(u);
                    });

            /* ===== HOUSEHOLD ===== */

            Household household = householdRepository.findAll()
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        Household h = new Household();
                        h.setName("Demo Household");
                        return householdRepository.save(h);
                    });

            long vehicleCount = vehicleRepository.count();

            if (vehicleCount == 0) {

                Vehicle corolla = new Vehicle();
                corolla.setHousehold(household);
                corolla.setPlate("1234-ABC");
                corolla.setBrand("Toyota");
                corolla.setModel("Corolla");
                corolla.setFuelType(FuelType.GASOLINE);
                corolla.setInitialOdometer(45000);

                Vehicle ibiza = new Vehicle();
                ibiza.setHousehold(household);
                ibiza.setPlate("5678-DEF");
                ibiza.setBrand("Seat");
                ibiza.setModel("Ibiza");
                ibiza.setFuelType(FuelType.DIESEL);
                ibiza.setInitialOdometer(82000);

                Vehicle mt07 = new Vehicle();
                mt07.setHousehold(household);
                mt07.setPlate("9012-GHI");
                mt07.setBrand("Yamaha");
                mt07.setModel("MT-07");
                mt07.setFuelType(FuelType.GASOLINE);
                mt07.setInitialOdometer(12000);

                vehicleRepository.save(corolla);
                vehicleRepository.save(ibiza);
                vehicleRepository.save(mt07);

            }
        };
    }
}