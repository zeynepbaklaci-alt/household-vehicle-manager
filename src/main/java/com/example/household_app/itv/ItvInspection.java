package com.example.household_app.itv;

import com.example.household_app.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "itv_inspections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItvInspection {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDate validUntil;

    @Column(nullable = false)
    private double cost;

    @Column(nullable = false)
    private boolean passed;

    private String note;
}