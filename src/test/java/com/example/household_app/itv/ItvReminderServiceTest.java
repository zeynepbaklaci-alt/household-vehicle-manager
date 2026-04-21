package com.example.household_app.itv;

import com.example.household_app.reminder.Reminder;
import com.example.household_app.reminder.ReminderRepository;
import com.example.household_app.reminder.ReminderType;
import com.example.household_app.vehicle.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItvReminderServiceTest {

    @Mock
    private ItvInspectionRepository itvInspectionRepository;

    @Mock
    private ReminderRepository reminderRepository;

    @InjectMocks
    private ItvReminderService itvReminderService;

    @Test
    void shouldCreateReminderWhenItvExpiresIn30Days() {

        // GIVEN
        Vehicle vehicle = new Vehicle();

        ItvInspection itv = new ItvInspection(
                null,
                vehicle,
                LocalDate.now().minusYears(1),
                LocalDate.now().plusDays(30),
                35.0,
                true,
                "Passed"
        );

        when(itvInspectionRepository.findAll())
                .thenReturn(List.of(itv));

        when(reminderRepository.existsByVehicleAndTypeAndRemindAt(
                any(Vehicle.class),
                eq(ReminderType.ITV),
                any(LocalDate.class)
        )).thenReturn(false);

        // WHEN
        itvReminderService.createUpcomingItvReminders();

        // THEN
        verify(reminderRepository, times(1))
                .save(any(Reminder.class));
    }
}