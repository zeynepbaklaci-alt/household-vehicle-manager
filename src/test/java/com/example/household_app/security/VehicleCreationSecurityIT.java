package com.example.household_app.security;

import com.example.household_app.household.Household;
import com.example.household_app.household.HouseholdRepository;
import com.example.household_app.membership.Membership;
import com.example.household_app.membership.MembershipRepository;
import com.example.household_app.user.Role;
import com.example.household_app.user.User;
import com.example.household_app.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class VehicleCreationSecurityIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HouseholdRepository householdRepository;

    @Autowired
    MembershipRepository membershipRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ObjectMapper objectMapper;


    Household householdA;
    Household householdB;
    User adminA;
    User adminB;
    User memberA;

    @BeforeEach
    void setup() {

        householdA = householdRepository.save(new Household("Household A"));
        householdB = householdRepository.save(new Household("Household B"));

        adminA = userRepository.save(
                new User(
                        UUID.randomUUID(),
                        "adminA@email.com",
                        passwordEncoder.encode("123456"),
                        "Admin A",
                        Role.USER,
                        Instant.now()
                )
        );

        memberA = userRepository.save(
                new User(
                        UUID.randomUUID(),
                        "memberA@email.com",
                        passwordEncoder.encode("123456"),
                        "Member A",
                        Role.USER,
                        Instant.now()
                )
        );

        adminB = userRepository.save(
                new User(
                        UUID.randomUUID(),
                        "adminB@email.com",
                        passwordEncoder.encode("123456"),
                        "Admin B",
                        Role.USER,
                        Instant.now()
                )
        );

        membershipRepository.save(new Membership(adminA, householdA, Role.ADMIN));
        membershipRepository.save(new Membership(memberA, householdA, Role.USER));
        membershipRepository.save(new Membership(adminB, householdB, Role.ADMIN));
    }

    private String newVehicleJson() throws Exception {
        Map<String, Object> body = Map.of(
                "plate", "34-POST-123",
                "brand", "Toyota",
                "model", "Corolla"
        );

        return objectMapper.writeValueAsString(body);
    }

    private String tokenFor(User user) {
        return jwtTokenProvider.createTestToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Test
    void adminCanCreateVehicleInOwnHousehold() throws Exception {
        mockMvc.perform(
                        post("/api/households/{householdId}/vehicles", householdA.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(adminA)
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newVehicleJson())
                )
                .andExpect(status().isCreated());
    }

    @Test
    void memberCannotCreateVehicleInHousehold() throws Exception {

        mockMvc.perform(
                        post("/api/households/{householdId}/vehicles", householdA.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(memberA)
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newVehicleJson())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCannotCreateVehicleInOtherHousehold() throws Exception {

        mockMvc.perform(
                        post("/api/households/{householdId}/vehicles", householdA.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + tokenFor(adminB)
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newVehicleJson())
                )
                .andExpect(status().isForbidden());
    }
}


