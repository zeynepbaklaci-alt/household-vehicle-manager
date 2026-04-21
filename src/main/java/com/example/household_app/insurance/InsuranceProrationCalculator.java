package com.example.household_app.insurance;

public class InsuranceProrationCalculator {


    public static double monthlyCost(InsurancePolicy policy) {

        return switch (policy.getPeriodicity()) {
            case "MONTHLY" -> policy.getPremium();
            case "QUARTERLY" -> policy.getPremium() / 3;
            case "ANNUAL" -> policy.getPremium() / 12;
            default -> 0;
        };
    }

}
