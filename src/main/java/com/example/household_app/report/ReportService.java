package com.example.household_app.report;

import com.example.household_app.fuelfill.FuelFill;
import com.example.household_app.fuelfill.FuelFillRepository;
import com.example.household_app.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final FuelFillRepository fuelFillRepository;

    public VehicleReportDto calculateReport(UUID vehicleId, String period) {

        LocalDate from = LocalDate.parse(period + "-01");
        LocalDate to = from.plusMonths(1);

        // sadece ID set edilmiş vehicle (proxy gibi)
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        List<FuelFill> fills =
                fuelFillRepository.findByVehicleAndDateBetween(
                        vehicle, from, to
                );

        VehicleReportDto dto = new VehicleReportDto();
        dto.setPeriod(period);

        if (fills.isEmpty()) {
            dto.setKmDriven(0);
            dto.setTotalCost(0);
            dto.setCostPerKm(0);
            dto.setAvgConsumption(0);
            return dto;
        }

        int minKm = fills.stream()
                .mapToInt(FuelFill::getOdometerKm)
                .min().orElse(0);

        int maxKm = fills.stream()
                .mapToInt(FuelFill::getOdometerKm)
                .max().orElse(0);

        int km = maxKm - minKm;

        double totalCost =
                fills.stream().mapToDouble(FuelFill::getTotalCost).sum();

        double totalLiters =
                fills.stream().mapToDouble(FuelFill::getLiters).sum();

        dto.setKmDriven(km);
        dto.setTotalCost(totalCost);

        if (km > 0) {
            dto.setCostPerKm(totalCost / km);
            dto.setAvgConsumption((totalLiters / km) * 100);
        }

        return dto;
    }
}
