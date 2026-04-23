package com.example.household_app.reminder;

import com.example.household_app.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface ReminderRepository extends JpaRepository<Reminder, UUID> {


    @Modifying
    @Query("""
    update Reminder r
    set r.sent = true,
        r.sentAt = :now
    where r.sent = false
      and r.remindAt <= :today
""")
    void markAllPendingAsSent(
            @Param("today") LocalDate today,
            @Param("now") LocalDateTime now
    );


boolean existsByVehicleAndTypeAndRemindAt(
            Vehicle vehicle,
            ReminderType type,
            LocalDate remindAt
    );

    List<Reminder> findBySentFalseAndRemindAtLessThanEqual(LocalDate date);

    List<Reminder> findByVehicleOrderByRemindAtDesc(Vehicle vehicle);
}
