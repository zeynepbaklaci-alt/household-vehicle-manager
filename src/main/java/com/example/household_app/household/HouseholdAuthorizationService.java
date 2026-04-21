package com.example.household_app.household;


import com.example.household_app.membership.MembershipRepository;
import com.example.household_app.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HouseholdAuthorizationService {

    private final MembershipRepository membershipRepository;

    public boolean isMember(UUID householdId, UUID userId){
        return membershipRepository.isMember(householdId, userId);
               // existsByHousehold_IdAndUser_Id(householdId, userId);
    }

    public boolean isAdmin(UUID householdId, UUID userId) {
        if (householdId == null || userId == null) {
            return false; // 🔒 FAIL CLOSED
        }
        return membershipRepository
                .existsByUser_IdAndHousehold_IdAndRole(
                        userId,
                        householdId,
                        Role.ADMIN
                );
    }
}
