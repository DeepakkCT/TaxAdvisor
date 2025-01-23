package com.example.taxadvisor.service;

import com.example.taxadvisor.model.*;
import com.example.taxadvisor.util.MockSchemeDatabase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaxAdvisorService {

    public TaxAdviceResponse getTaxAdvice(TaxAdviceRequest request) {
        double income = request.getIncome();
        double amountToInvest = request.getAmountToInvest();
        double currentEPF = request.getCurrentEPFContribution();

        // 1. Calculate the tax before any new investment
        double taxBefore = TaxCalculator.calculateTax(income);

        // 2. Based on user’s risk profile, lock-in preference, etc., pick top schemes
        List<InvestmentScheme> allSchemes = MockSchemeDatabase.getAllSchemes();
        List<InvestmentScheme> filteredSchemes = filterSchemes(allSchemes, request);

        // 3. Distribute amountToInvest among the filtered schemes while respecting max limits
        List<SchemeRecommendation> recommendations = allocateInvestments(filteredSchemes, amountToInvest, currentEPF);

        // 4. Calculate how much was invested in total
        double totalInvested = recommendations.stream().mapToDouble(SchemeRecommendation::getAmountAllocated).sum();

        // 5. Calculate tax after the investment
        //    For simplicity, we assume the entire recommended investment is deducted from taxable income
        //    up to the max limit set by the scheme's section.
        double totalDeduction = totalInvested; // If you want more nuance, track 80C vs 80CCD(1) vs 80CCD(1B)
        double newTaxableIncome = income - totalDeduction;
        if (newTaxableIncome < 0) newTaxableIncome = 0; // can't go below zero

        double taxAfter = TaxCalculator.calculateTax(newTaxableIncome);
        double taxSaved = taxBefore - taxAfter;

        return new TaxAdviceResponse(taxBefore, taxAfter, taxSaved, recommendations);
    }

    private List<InvestmentScheme> filterSchemes(List<InvestmentScheme> schemes, TaxAdviceRequest request) {
        List<InvestmentScheme> result = new ArrayList<>();

        for (InvestmentScheme scheme : schemes) {
            // Filter by risk
            if (!scheme.getRiskProfile().equalsIgnoreCase(request.getRiskProfile())
                    && !request.getRiskProfile().equalsIgnoreCase("HIGH")) {
                // If user is LOW risk, we only pick LOW risk schemes
                // If user is MEDIUM risk, we pick LOW or MEDIUM (some business logic)
                // If user is HIGH risk, we pick all
                if (request.getRiskProfile().equalsIgnoreCase("LOW")
                        && !scheme.getRiskProfile().equalsIgnoreCase("LOW")) {
                    continue;
                }
                if (request.getRiskProfile().equalsIgnoreCase("MEDIUM")
                        && scheme.getRiskProfile().equalsIgnoreCase("HIGH")) {
                    continue;
                }
            }

            // Filter by lock-in preference (similar logic, or skip if you want simpler)
            // For example, if user says SHORT, we skip LONG
            if (request.getLockInPeriod().equalsIgnoreCase("SHORT")
                    && scheme.getLockInPeriod().equalsIgnoreCase("LONG")) {
                continue;
            }

            // We can also consider age or marital status for certain scheme preferences
            // e.g. If user is older, maybe SCSS is good if age >= 60
            // If user has a girl child, Sukanya might be recommended, etc.
            // ... (Add your own rules) ...

            result.add(scheme);
        }

        return result;
    }

    private List<SchemeRecommendation> allocateInvestments(List<InvestmentScheme> schemes, double amountToInvest, double currentEPF) {
        List<SchemeRecommendation> recommendations = new ArrayList<>();

        double remainingAmount = amountToInvest;

        // A simple approach:
        // 1) Sort by expected return descending (just as a naive approach)
        // 2) Allocate until max limit is reached or until we exhaust amount

        // But consider existing EPF in 80C limit
        double used80CLimit = currentEPF > 150000 ? 150000 : currentEPF;
        // If current EPF is 50k, that means we have 100k left in 80C limit

        // Let's just do a naive approach:
        schemes.sort((a, b) -> Double.compare(b.getExpectedReturns(), a.getExpectedReturns()));

        for (InvestmentScheme scheme : schemes) {
            if (remainingAmount <= 0) break;

            double maxSectionLimit = scheme.getMaxLimit();
            if (scheme.getSection().equalsIgnoreCase("80C")) {
                // Adjust 80C limit for existing EPF usage
                double available80C = 150000 - used80CLimit;
                if (available80C <= 0) {
                    continue; // no room in 80C
                }
                // The actual amount we can put in
                double toInvest = Math.min(remainingAmount, Math.min(available80C, maxSectionLimit));
                if (toInvest > 0) {
                    // Create recommendation
                    recommendations.add(new SchemeRecommendation(
                            scheme.getName(),
                            toInvest,
                            scheme.getSection(),
                            scheme.getExpectedReturns()
                    ));
                    // Update counters
                    remainingAmount -= toInvest;
                    used80CLimit += toInvest;
                }
            } else if (scheme.getSection().equalsIgnoreCase("80CCD(1)")) {
                double availableCCD1 = 50000; // Hard-coded, can refine if user invests in NPS
                double toInvest = Math.min(remainingAmount, Math.min(availableCCD1, maxSectionLimit));
                if (toInvest > 0) {
                    recommendations.add(new SchemeRecommendation(
                            scheme.getName(),
                            toInvest,
                            scheme.getSection(),
                            scheme.getExpectedReturns()
                    ));
                    remainingAmount -= toInvest;
                    // we don't track used portion here in detail, but we could
                }
            } else if (scheme.getSection().equalsIgnoreCase("80CCD(1B)")) {
                double availableCCD1B = 50000;
                double toInvest = Math.min(remainingAmount, Math.min(availableCCD1B, maxSectionLimit));
                if (toInvest > 0) {
                    recommendations.add(new SchemeRecommendation(
                            scheme.getName(),
                            toInvest,
                            scheme.getSection(),
                            scheme.getExpectedReturns()
                    ));
                    remainingAmount -= toInvest;
                }
            }
        }

        return recommendations;
    }
}
