package com.example.household_app.membership;

import com.example.household_app.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MembershipRepository
        extends JpaRepository<Membership, MembershipId> {

    @Query("""
    select count(m) > 0
    from Membership m
    where m.household.id = :householdId
      and m.user.id = :userId
""")
    boolean isMember(UUID householdId, UUID userId);


    boolean existsByUser_IdAndHousehold_IdAndRole(
            UUID userId,
            UUID householdId,
            Role role
    );

    @Query("""
        select m.household.id
        from Membership m
        where m.user.id = :userId
    """)
    List<UUID> findAllHouseholdIdsByUserId(UUID userId);
}
