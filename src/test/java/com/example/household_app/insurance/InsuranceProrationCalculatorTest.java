package com.example.household_app.insurance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InsuranceProrationCalculatorTest {

    @Test
    void shouldReturnFullPremiumForMonthlyPolicy() {

        InsurancePolicy policy = new InsurancePolicy();
        policy.setPeriodicity("MONTHLY");
        policy.setPremium(120);

        double monthlyCost =
                InsuranceProrationCalculator.monthlyCost(policy);

        assertEquals(120, monthlyCost);
    }

    @Test
    void shouldDividePremiumByThreeForQuarterlyPolicy() {

        InsurancePolicy policy = new InsurancePolicy();
        policy.setPeriodicity("QUARTERLY");
        policy.setPremium(300);

        double monthlyCost =
                InsuranceProrationCalculator.monthlyCost(policy);

        assertEquals(100, monthlyCost);
    }

    @Test
    void shouldDividePremiumByTwelveForAnnualPolicy() {

        InsurancePolicy policy = new InsurancePolicy();
        policy.setPeriodicity("ANNUAL");
        policy.setPremium(1200);

        double monthlyCost =
                InsuranceProrationCalculator.monthlyCost(policy);

        assertEquals(100, monthlyCost);
    }

    @Test
    void shouldReturnZeroForUnknownPeriodicity() {

        InsurancePolicy policy = new InsurancePolicy();
        policy.setPeriodicity("UNKNOWN");
        policy.setPremium(500);

        double monthlyCost =
                InsuranceProrationCalculator.monthlyCost(policy);

        assertEquals(0, monthlyCost);
    }
}
