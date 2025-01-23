package com.example.taxadvisor.util;

import com.example.taxadvisor.model.InvestmentScheme;

import java.util.ArrayList;
import java.util.List;

public class MockSchemeDatabase {

    public static List<InvestmentScheme> getAllSchemes() {
        List<InvestmentScheme> schemes = new ArrayList<>();

        // 80C: limit = 1,50,000
        schemes.add(new InvestmentScheme("PPF", "Public Provident Fund", 150000, "80C",
                "LOW", "LONG", 7.1));
        schemes.add(new InvestmentScheme("ELSS", "Equity Linked Savings Scheme", 150000, "80C",
                "HIGH", "MEDIUM", 12.0));
        schemes.add(new InvestmentScheme("EPF", "Employee Provident Fund", 150000, "80C",
                "LOW", "LONG", 8.25));
        schemes.add(new InvestmentScheme("ULIP", "Unit Linked Insurance Plan", 150000, "80C",
                "MEDIUM", "LONG", 10.0));
        schemes.add(new InvestmentScheme("NSC", "National Savings Certificate", 150000, "80C",
                "LOW", "MEDIUM", 7.7));
        schemes.add(new InvestmentScheme("Tax-Saver FD", "5-Year Tax Saving Fixed Deposit", 150000, "80C",
                "LOW", "MEDIUM", 7.0));
        schemes.add(new InvestmentScheme("SCSS", "Senior Citizen Savings Scheme", 150000, "80C",
                "LOW", "MEDIUM", 8.0));
        schemes.add(new InvestmentScheme("Life Insurance Premium", "Life Insurance Policies", 150000, "80C",
                "LOW", "LONG", 5.0));
        schemes.add(new InvestmentScheme("Sukanya Samriddhi Yojana", "Girl Child Savings Scheme", 150000, "80C",
                "LOW", "LONG", 8.2));

        //80C -Tuitions fees for children
        schemes.add(new InvestmentScheme("Tuition Fees", "Educational tuition fees", 150000, "80C",
                "LOW", "SHORT", 0.0));

        //80C- VPF (Voluntary Provident Fund)
        schemes.add(new InvestmentScheme("VPF", "Voluntary Provident Fund", 150000, "80C",
                "LOW", "LONG", 8.15));

        //80D- Health Insurance
        schemes.add(new InvestmentScheme("Health Insurance ", "Medical Insurance Premium", 25000, "80D",
                "MEDIUM", "SHORT", 0.0));


        // 80CCD(1)
        schemes.add(new InvestmentScheme("NPS Tier I", "National Pension System - Tier I", 50000, "80CCD(1B)",
                "MEDIUM", "LONG", 10.0));

        // 80CCD(1B)
        schemes.add(new InvestmentScheme("NPS Tier I (Additional)", "National Pension System - Additional Deduction", 50000, "80CCD(1B)",
                "MEDIUM", "LONG", 10.0));

        return schemes;
    }
}
