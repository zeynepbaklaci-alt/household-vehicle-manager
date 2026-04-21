package com.example.household_app.report;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/costs")
    public VehicleReportDto getVehicleReport(
            @RequestParam UUID vehicleId,
            @RequestParam String period  // "2026-01"
    ) {
        return reportService.calculateReport(vehicleId, period);
    }
}
