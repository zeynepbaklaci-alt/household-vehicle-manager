package com.example.household_app.reminder;

import com.example.household_app.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public interface ReminderRepository extends JpaRepository<Reminder, UUID> {

    boolean existsByVehicleAndTypeAndRemindAt(
            Vehicle vehicle,
            ReminderType type,
            LocalDate remindAt
    );

    List<Reminder> findBySentFalseAndRemindAtLessThanEqual(LocalDate date);
}
