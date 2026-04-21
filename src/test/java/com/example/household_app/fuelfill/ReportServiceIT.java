package com.example.household_app.fuelfill;

import com.example.household_app.report.ReportService;
import com.example.household_app.report.VehicleReportDto;
import com.example.household_app.vehicle.Vehicle;
import com.example.household_app.vehicle.VehicleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ReportServiceIT {

    @Autowired
    private ReportService reportService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private FuelFillRepository fuelFillRepository;

    @Test
    void monthlyReportShouldCalculateFuelCostsAndKm() {

        Vehicle vehicle = new Vehicle();
        vehicle.setPlate("34ABC123");
        vehicleRepository.save(vehicle);

        fuelFillRepository.save(
                new FuelFill(null, vehicle, LocalDate.of(2026,3,1),
                        10000, 40, 1.5, 60, null, null));

        fuelFillRepository.save(
                new FuelFill(null, vehicle, LocalDate.of(2026,3,20),
                        10500, 35, 1.6, 56, null, null));

        VehicleReportDto report =
                reportService.calculateReport(vehicle.getId(), "2026-03");

        assertEquals(500, report.getKmDriven());
        assertEquals(116, report.getTotalCost(), 0.01);
        assertTrue(report.getCostPerKm() > 0);
        assertTrue(report.getAvgConsumption() > 0);
    }
}
