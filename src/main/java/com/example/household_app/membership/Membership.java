package com.example.household_app.membership;

import com.example.household_app.household.Household;
import com.example.household_app.user.Role;
import com.example.household_app.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "memberships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    @EmbeddedId
    private MembershipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("householdId")
    @JoinColumn(name = "household_id")
    private Household household;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public Membership(User user, Household household, Role role) {
        this.user = user;
        this.household = household;
        this.role = role;
        this.id = new MembershipId(user.getId(), household.getId());
    }
}
