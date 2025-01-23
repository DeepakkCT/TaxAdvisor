package com.example.taxadvisor.service;

public class TaxCalculator {

    /**
     * Calculates tax based on Indian Old Regime Tax slabs (for demonstration).
     * Adjust as needed for real-world use.
     */
    public static double calculateTax(double taxableIncome) {
        // Simplified Slab:
        // 0 - 2.5L : 0%
        // 2.5L - 5L : 5%
        // 5L - 10L : 20%
        // Above 10L : 30%

        double tax = 0;
        double remaining = taxableIncome;

        // Slab 1: 0 - 2.5L
        double slab1 = 250000;
        if (remaining > slab1) {
            remaining -= slab1;
        } else {
            return 0;
        }

        // Slab 2: 2.5L - 5L (5%)
        double slab2 = 250000;
        if (remaining > slab2) {
            tax += slab2 * 0.05;
            remaining -= slab2;
        } else {
            tax += remaining * 0.05;
            return tax;
        }

        // Slab 3: 5L - 10L (20%)
        double slab3 = 500000;
        if (remaining > slab3) {
            tax += slab3 * 0.20;
            remaining -= slab3;
        } else {
            tax += remaining * 0.20;
            return tax;
        }

        // Slab 4: Above 10L (30%)
        tax += remaining * 0.30;

        return tax;
    }
}
