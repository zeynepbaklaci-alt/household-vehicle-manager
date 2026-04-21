package com.example.household_app.security;

import com.example.household_app.membership.Membership;
import com.example.household_app.membership.MembershipRepository;
import com.example.household_app.user.Role;
import com.example.household_app.user.User;
import com.example.household_app.user.UserRepository;
import com.example.household_app.household.Household;
import com.example.household_app.household.HouseholdRepository;

import com.example.household_app.vehicle.Vehicle;
import com.example.household_app.vehicle.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static com.example.household_app.testutil.TestVehicleFactory.vehicleFor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class HouseholdIsolationIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HouseholdRepository householdRepository;

    @Autowired
    MembershipRepository membershipRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    Household householdA;
    Household householdB;
    User userA;
    User userB;
    Vehicle vehicleA;
    Vehicle vehicleB;

    @BeforeEach
    void setup() {

        householdA = householdRepository.save(
                new Household("Household A")
        );

        householdB = householdRepository.save(
                new Household("Household B")
        );

        userA = userRepository.save(
                new User(
                        UUID.randomUUID(),                    // id
                        "userA@email.com",                    // email
                        passwordEncoder.encode("123456"),     // passwordHash
                        "Test User A",                        // fullName
                        Role.USER,                            // role
                        Instant.now()                         // createdAt
                )
        );

        userB = userRepository.save(
                new User(
                        UUID.randomUUID(),
                        "userB@email.com",
                        passwordEncoder.encode("123456"),
                        "Test User B",
                        Role.USER,
                        Instant.now()
                )
        );

        membershipRepository.save(
                new Membership(userA, householdA, Role.ADMIN)
        );

        vehicleA = vehicleRepository.save(
                vehicleFor(householdA)
        );
        vehicleB = vehicleRepository.save(
                vehicleFor(householdB)
        );
    }

    private String tokenFor(User user) {
        return jwtTokenProvider.createTestToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Test
    void userACannotListVehiclesFromOtherHousehold() throws Exception {
        mockMvc.perform(
                        get("/api/households/{householdId}/vehicles", householdB.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(userA)
                                )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void userACanListVehiclesFromOwnHousehold() throws Exception {
        mockMvc.perform(
                        get("/api/households/{householdId}/vehicles", householdA.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(userA)
                                )
                )
                .andExpect(status().isOk());
    }

    @Test
    void userWithoutMembershipCannotAccessAnyHouseholdVehicle() throws Exception {
        mockMvc.perform(
                        get("/api/households/{householdId}/vehicles", householdA.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(userB)
                                )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void requestWithoutTokenIsForbidden() throws Exception {

        mockMvc.perform(
                        get("/api/households/{householdId}/vehicles", householdA.getId())
                )
                .andExpect(status().isForbidden());
    }

}

