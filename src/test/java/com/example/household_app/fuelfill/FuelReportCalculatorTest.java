package com.example.household_app.fuelfill;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FuelReportCalculatorTest {

    @Test
    void shouldCalculateMonthlyFuelReportCorrectly() {

        FuelFill f1 = new FuelFill(
                null,
                null,
                LocalDate.of(2026, 1, 1),
                10000,
                40,
                1.5,
                60,
                null,
                null
        );

        FuelFill f2 = new FuelFill(
                null,
                null,
                LocalDate.of(2026, 1, 20),
                10500,
                35,
                1.6,
                56,
                null,
                null
        );

        FuelReportResult result =
                FuelReportCalculator.calculate(List.of(f1, f2));

        assertEquals(500, result.kmDriven());
        assertEquals(116.0, result.totalFuelCost(), 0.01);
        assertEquals(0.232, result.costPerKm(), 0.01);
        assertEquals(15.0, result.avgConsumptionPer100Km(), 0.01);
    }


    @Test
    void shouldReturnZeroWhenFuelFillListIsEmpty() {
        FuelReportResult result =
                FuelReportCalculator.calculate(List.of());

        assertEquals(0, result.kmDriven());
        assertEquals(0, result.totalFuelCost());
        assertEquals(0, result.costPerKm());
        assertEquals(0, result.avgConsumptionPer100Km());
    }


    @Test
    void shouldReturnZeroWhenOnlyOneFuelFillExists() {

        FuelFill f1 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 10),
                10000,
                40,
                1.6,
                64,
                null, null
        );

        FuelReportResult result =
                FuelReportCalculator.calculate(List.of(f1));

        assertEquals(0, result.kmDriven());
        assertEquals(0, result.costPerKm());
        assertEquals(0, result.avgConsumptionPer100Km());
    }

    @Test
    void shouldHandleSameOdometerValuesSafely() {

        FuelFill f1 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 1),
                10000,
                30,
                1.5,
                45,
                null, null
        );

        FuelFill f2 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 15),
                10000,   // aynı km
                25,
                1.6,
                40,
                null, null
        );

        FuelReportResult result =
                FuelReportCalculator.calculate(List.of(f1, f2));

        assertEquals(0, result.kmDriven());
        assertEquals(0, result.costPerKm());
        assertEquals(0, result.avgConsumptionPer100Km());
    }

    @Test
    void shouldSortFuelFillsByDateBeforeCalculation() {

        FuelFill f1 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 20),
                10500,
                35,
                1.6,
                56,
                null, null
        );

        FuelFill f2 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 1),
                10000,
                40,
                1.5,
                60,
                null, null
        );

        FuelReportResult result =
                FuelReportCalculator.calculate(List.of(f1, f2));

        assertEquals(500, result.kmDriven());
        assertEquals(116.0, result.totalFuelCost(), 0.01);
    }

    @Test
    void shouldReturnZeroConsumptionWhenTotalLitersIsZero() {

        FuelFill f1 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 1),
                10000,
                0,
                1.5,
                0,
                null, null
        );

        FuelFill f2 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 15),
                10500,
                0,
                1.5,
                0,
                null, null
        );

        FuelReportResult result =
                FuelReportCalculator.calculate(List.of(f1, f2));

        assertEquals(500, result.kmDriven());
        assertEquals(0, result.avgConsumptionPer100Km());
    }

    @Test
    void shouldCalculateRealisticMonthlyFuelReport() {

        FuelFill f1 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 5),
                20000,
                45,
                1.7,
                76.5,
                null, null
        );

        FuelFill f2 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 15),
                20500,
                40,
                1.65,
                66,
                null, null
        );

        FuelFill f3 = new FuelFill(
                null, null,
                LocalDate.of(2026, 1, 28),
                21000,
                42,
                1.68,
                70.56,
                null, null
        );

        FuelReportResult result =
                FuelReportCalculator.calculate(List.of(f1, f2, f3));

        assertEquals(1000, result.kmDriven());
        assertEquals(213.06, result.totalFuelCost(), 0.01);
        assertEquals(0.213, result.costPerKm(), 0.01);
        assertEquals(12.7, result.avgConsumptionPer100Km(), 0.1);
    }

}