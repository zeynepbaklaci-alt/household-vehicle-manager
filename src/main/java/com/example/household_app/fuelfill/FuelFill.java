package com.example.household_app.fuelfill;

import com.example.household_app.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "fuel_fills",
        indexes = {
                @Index(name = "idx_fuel_fill_vehicle_odometer",
                        columnList = "vehicle_id, odometer_km")
        }
        )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelFill {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int odometerKm;

    @Column(nullable = false)
    private double liters;

    @Column(nullable = false)
    private double pricePerLiter;

    @Column(nullable = false)
    private double totalCost;

    private String station;
    private String note;

}

