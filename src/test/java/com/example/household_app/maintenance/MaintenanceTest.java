package com.example.household_app.maintenance;

import com.example.household_app.maintenance.dto.MaintenanceDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaintenanceTest {

    @Test
    void shouldMapMaintenanceToDtoCorrectly() {

        Maintenance maintenance = new Maintenance(
                UUID.randomUUID(),
                null,
                LocalDate.of(2026, 3, 10),
                45000,
                120,
                "OIL",
                "Local Garage",
                "Oil + filter"
        );

        MaintenanceDto dto = new MaintenanceDto(
                maintenance.getId(),
                maintenance.getDate(),
                maintenance.getOdometerKm(),
                maintenance.getCost(),
                maintenance.getType(),
                maintenance.getWorkshop(),
                maintenance.getNote()
        );

        assertEquals(120, dto.cost());
    }

}
