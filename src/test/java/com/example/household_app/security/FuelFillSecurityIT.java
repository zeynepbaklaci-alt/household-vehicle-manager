package com.example.household_app.security;

import com.example.household_app.fuelfill.FuelFillRepository;
import com.example.household_app.household.Household;
import com.example.household_app.household.HouseholdRepository;
import com.example.household_app.membership.Membership;
import com.example.household_app.membership.MembershipRepository;
import com.example.household_app.user.Role;
import com.example.household_app.user.User;
import com.example.household_app.user.UserRepository;
import com.example.household_app.vehicle.FuelType;
import com.example.household_app.vehicle.Vehicle;
import com.example.household_app.vehicle.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class FuelFillSecurityIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HouseholdRepository householdRepository;

    @Autowired
    MembershipRepository membershipRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    FuelFillRepository fuelFillRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User memberA;
    User otherUser;
    Household householdA;
    Vehicle vehicleA;

    @BeforeEach
    void setup() {

        householdA = householdRepository.save(
                new Household("Household A")
        );

        memberA = userRepository.save(
                new User(
                        UUID.randomUUID(),
                        "member@email.com",
                        passwordEncoder.encode("123456"),
                        "Member",
                        Role.USER,
                        java.time.Instant.now()
                )
        );

        otherUser = userRepository.save(
                new User(
                        UUID.randomUUID(),
                        "other@email.com",
                        passwordEncoder.encode("123456"),
                        "Other",
                        Role.USER,
                        java.time.Instant.now()
                )
        );

        membershipRepository.save(
                new Membership(memberA, householdA, Role.USER)
        );

        vehicleA = vehicleRepository.save(
                new Vehicle(
                        UUID.randomUUID(),
                        "1234-ABC",              // plate ✅
                        "Toyota",                // brand
                        "Corolla",               // model
                        FuelType.GASOLINE,       // fuelType ✅
                        45000,                   // initialOdometer ✅
                        householdA               // household ✅
                )
        );
    }

    private String tokenFor(User user) {
        return jwtTokenProvider.createTestToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    private String validFuelFillJson(int odometerKm) throws Exception {
        Map<String, Object> body = Map.of(
                "date", LocalDate.now().toString(),
                "odometerKm", odometerKm,
                "liters", 40.0,
                "pricePerLiter", 1.5,
                "station", "Shell"
        );
        return objectMapper.writeValueAsString(body);
    }
    /**
     * ✅ Member kendi vehicle'ına fuel fill ekleyebilir
     */
    @Test
    void memberCanCreateFuelFillForOwnVehicle() throws Exception {

        mockMvc.perform(
                        post("/api/vehicles/{vehicleId}/fuel-fills", vehicleA.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(memberA)
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validFuelFillJson(100_000))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalCost").value(60.0)); // 40 * 1.5
    }

    /**
     * ❌ Başka household kullanıcısı fuel fill ekleyemez
     */
    @Test
    void userCannotCreateFuelFillForOtherHouseholdVehicle() throws Exception {

        mockMvc.perform(
                        post("/api/vehicles/{vehicleId}/fuel-fills", vehicleA.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(otherUser)
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validFuelFillJson(100_100))
                )
                .andExpect(status().isForbidden());
    }

    /**
     * ❌ Odometer geriye gidemez
     */
    @Test
    void cannotCreateFuelFillWithLowerOdometer() throws Exception {

        // first fuel fill (valid)
        mockMvc.perform(
                        post("/api/vehicles/{vehicleId}/fuel-fills", vehicleA.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(memberA)
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validFuelFillJson(100_000))
                )
                .andExpect(status().isCreated());

        // second fuel fill with LOWER odometer
        mockMvc.perform(
                        post("/api/vehicles/{vehicleId}/fuel-fills", vehicleA.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(memberA)
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validFuelFillJson(99_000))
                )
                .andExpect(status().isBadRequest());
    }
}

