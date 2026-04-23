package com.example.household_app.household;

import com.example.household_app.household.dto.HouseholdDto;
import com.example.household_app.membership.MembershipRepository;
import com.example.household_app.user.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/households")
@RequiredArgsConstructor
public class HouseholdController {

    private final HouseholdRepository householdRepository;
    private final MembershipRepository membershipRepository;
    private final CurrentUserService currentUserService;

    @GetMapping
    public List<HouseholdDto> myHouseholds() {

        UUID userId = currentUserService.getCurrentUserId();

        List<UUID> householdIds =
                membershipRepository.findAllHouseholdIdsByUserId(userId);

        return householdRepository.findAllById(householdIds)
                .stream()
                .map(h -> new HouseholdDto(
                        h.getId(),
                        h.getName()
                ))
                .toList();
    }
}
