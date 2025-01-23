package com.example.taxadvisor.service;

import com.example.taxadvisor.model.*;
import com.example.taxadvisor.util.MockSchemeDatabase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxAdvisorService {

    public TaxAdviceResponse getTaxAdvice(TaxAdviceRequest request) {
        double income = request.getIncome();
        double amountToInvest = request.getAmountToInvest();
        double currentEPF = request.getCurrentEPFContribution();

        // 1. Calculate tax BEFORE any new investment
        double taxBefore = TaxCalculator.calculateTax(income);

        // 2. Filter & score schemes (and add a Health Insurance scheme if user doesn't have one)
        List<SchemeWithScore> allScored = filterAndScoreSchemes(MockSchemeDatabase.getAllSchemes(), request);

        // 3. Sort by score desc, pick up to 5
        List<SchemeWithScore> topSchemes = allScored.stream()
                .sorted(Comparator.comparingDouble(SchemeWithScore::score).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // 4. Allocate (with correct grouping by section)
        List<SchemeRecommendation> recommendations = allocateBySection(topSchemes, amountToInvest, currentEPF, request.getAge());

        // 5. Calculate how much got invested
        double totalInvested = recommendations.stream()
                .mapToDouble(SchemeRecommendation::getAmountAllocated)
                .sum();

        // 6. Calculate tax AFTER
        double newTaxableIncome = income - totalInvested;
        if (newTaxableIncome < 0) newTaxableIncome = 0;
        double taxAfter = TaxCalculator.calculateTax(newTaxableIncome);

        // Extra: how much money remains unallocated
        double leftMoney = leftAmount(amountToInvest, recommendations);
        String buttonName = "Remaining investable amount is " + leftMoney + ". Go to Our Personal Finance Calculator";

        double taxSaved = taxBefore - taxAfter;
        return new TaxAdviceResponse(taxBefore, taxAfter, taxSaved, recommendations, leftMoney, buttonName);
    }

    /**
     * Filter out schemes that do not match the user's risk/lock-in/age requirements,
     * and compute a multi-criteria score for each valid scheme.
     * If user does NOT have health insurance, we add a synthetic "Health Insurance (80D)" scheme.
     */
    private List<SchemeWithScore> filterAndScoreSchemes(List<InvestmentScheme> allSchemes, TaxAdviceRequest request) {
        List<SchemeWithScore> result = new ArrayList<>();

        // 1) Normal filtering + scoring
        for (InvestmentScheme scheme : allSchemes) {
            // Risk-based filter
            if (!scheme.getRiskProfile().equalsIgnoreCase(request.getRiskProfile())
                    && !request.getRiskProfile().equalsIgnoreCase("HIGH")) {
                if (request.getRiskProfile().equalsIgnoreCase("LOW")
                        && !scheme.getRiskProfile().equalsIgnoreCase("LOW")) {
                    continue;
                }
                if (request.getRiskProfile().equalsIgnoreCase("MEDIUM")
                        && scheme.getRiskProfile().equalsIgnoreCase("HIGH")) {
                    continue;
                }
            }

            // Lock-in filter
            if (request.getLockInPeriod().equalsIgnoreCase("SHORT")
                    && scheme.getLockInPeriod().equalsIgnoreCase("LONG")) {
                continue;
            }

            // Example age-based skip
            if (request.getAge() < 60 && scheme.getName().toLowerCase().contains("senior")) {
                continue;
            }

            // Passed filters -> compute a multi-criteria score
            double score = computeSchemeScore(scheme, request);
            result.add(new SchemeWithScore(scheme, score));
        }

        // 2) If user does NOT have health insurance, add a synthetic "Health Insurance" scheme (section 80D).
        if (!request.isHasHealthInsurance()) {
            // For demonstration, let's add one "Health Insurance" plan with:
            // - maxLimit 25000
            // - riskProfile "LOW"
            // - lockInPeriod "SHORT"
            // - expectedReturns = 0 (since it's insurance, not an investment per se)
            // You can adjust these details as you see fit.
            InvestmentScheme healthInsuranceScheme = new InvestmentScheme(
                    "Health Insurance (80D)",
                    "Get comprehensive health coverage and tax deduction under 80D",
                    25000,           // maxLimit
                    "80D",           // tax section
                    "LOW",           // risk profile
                    "SHORT",         // lock-in
                    0.0              // expected returns
            );
            double hiScore = computeSchemeScore(healthInsuranceScheme, request);
            result.add(new SchemeWithScore(healthInsuranceScheme, hiScore));
        }

        return result;
    }

    /**
     * A multi-criteria scoring that rewards:
     * - higher expectedReturns
     * - exact risk/lock-in match
     * - some age/marital considerations
     */
    private double computeSchemeScore(InvestmentScheme scheme, TaxAdviceRequest request) {
        double score = 0.0;

        // 1) returns
        score += scheme.getExpectedReturns();

        // 2) risk alignment
        if (scheme.getRiskProfile().equalsIgnoreCase(request.getRiskProfile())) {
            score += 2.0;
        } else if ("HIGH".equalsIgnoreCase(request.getRiskProfile())) {
            // user high risk -> partial acceptance
            score += 1.0;
        }

        // 3) lock-in alignment
        if (scheme.getLockInPeriod().equalsIgnoreCase(request.getLockInPeriod())) {
            score += 2.0;
        } else {
            score += 1.0;
        }

        // 4) age-based
        if (request.getAge() >= 60 && scheme.getName().toLowerCase().contains("senior")) {
            score += 2.0;
        }

        // 5) marital status
        if ("MARRIED".equalsIgnoreCase(request.getMaritalStatus())) {
            score += 1.0;
        }

        return score;
    }

    /**
     * Group the top schemes by their tax section and allocate to each group
     * without exceeding that section's leftover limit (80C, 80CCD, 80D, etc.).
     */
    private List<SchemeRecommendation> allocateBySection(List<SchemeWithScore> topSchemes,
                                                         double amountToInvest,
                                                         double currentEPF,
                                                         int userAge) {
        List<SchemeRecommendation> recommendations = new ArrayList<>();
        double remainingAmount = amountToInvest;

        // --- 80C leftover after EPF ---
        double used80C = Math.min(currentEPF, 150000);
        double avail80C = Math.max(0, 150000 - used80C);

        // --- 80CCD(1), 80CCD(1B) hard-coded limits ---
        double availCCD1 = 50000;
        double availCCD1B = 50000;

        // --- 80D limit: let's assume 25,000 for demonstration
        // (If user is senior, you might do 50,000, etc.)
        double avail80D = (userAge >= 60) ? 50000 : 25000;

        // 1) Group the top schemes by section
        List<SchemeWithScore> group80C = new ArrayList<>();
        List<SchemeWithScore> groupCCD1 = new ArrayList<>();
        List<SchemeWithScore> groupCCD1B = new ArrayList<>();
        List<SchemeWithScore> group80D = new ArrayList<>();

        for (SchemeWithScore sws : topSchemes) {
            String section = sws.scheme().getSection().toUpperCase();
            switch (section) {
                case "80C"       -> group80C.add(sws);
                case "80CCD(1)"  -> groupCCD1.add(sws);
                case "80CCD(1B)" -> groupCCD1B.add(sws);
                case "80D"       -> group80D.add(sws);
                default -> {
                    // If you have other sections, handle similarly or ignore
                }
            }
        }

        // 2) Allocate to each group proportionally
        remainingAmount = allocateGroup(group80C,   avail80C,   remainingAmount, recommendations);
        remainingAmount = allocateGroup(groupCCD1,  availCCD1,  remainingAmount, recommendations);
        remainingAmount = allocateGroup(groupCCD1B, availCCD1B, remainingAmount, recommendations);
        remainingAmount = allocateGroup(group80D,   avail80D,   remainingAmount, recommendations);

        return recommendations;
    }

    /**
     * Proportional distribution among all schemes in the same section.
     */
    private double allocateGroup(List<SchemeWithScore> group, double sectionLimit, double remaining,
                                 List<SchemeRecommendation> recommendations) {
        if (group.isEmpty() || sectionLimit <= 0 || remaining <= 0) {
            return remaining;
        }

        double totalScore = group.stream().mapToDouble(SchemeWithScore::score).sum();
        if (totalScore <= 0) {
            return remaining;
        }

        // The amount available to allocate in this section
        double groupAllocation = Math.min(sectionLimit, remaining);

        double leftoverGroupAllocation = groupAllocation;

        for (SchemeWithScore sws : group) {
            if (leftoverGroupAllocation <= 0) break;

            double fraction = sws.score() / totalScore;
            double rawAmount = fraction * groupAllocation;

            double toInvest = Math.min(rawAmount, Math.min(sws.scheme().getMaxLimit(), leftoverGroupAllocation));
            if (toInvest > 0) {
                recommendations.add(
                        new SchemeRecommendation(
                                sws.scheme().getName(),
                                toInvest,
                                sws.scheme().getSection(),
                                sws.scheme().getExpectedReturns()
                        )
                );
                leftoverGroupAllocation -= toInvest;
            }
        }

        double allocated = groupAllocation - leftoverGroupAllocation;
        return remaining - allocated;
    }

    /**
     * Helper to compute how much money remains unallocated.
     */
    private double leftAmount(double amountToInvest, List<SchemeRecommendation> recommendations) {
        double totalInvested = recommendations.stream().mapToDouble(SchemeRecommendation::getAmountAllocated).sum();
        return amountToInvest - totalInvested;
    }

    /**
     * Internal class to store a scheme + computed score.
     */
    private static class SchemeWithScore {
        private final InvestmentScheme scheme;
        private final double score;

        public SchemeWithScore(InvestmentScheme scheme, double score) {
            this.scheme = scheme;
            this.score = score;
        }

        public InvestmentScheme scheme() { return scheme; }
        public double score() { return score; }
    }
}