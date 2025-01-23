package com.example.taxadvisor.servicetest;


    import com.example.taxadvisor.model.InvestmentScheme;
    import com.example.taxadvisor.model.SchemeRecommendation;
    import com.example.taxadvisor.model.TaxAdviceRequest;
    import com.example.taxadvisor.model.TaxAdviceResponse;
    import com.example.taxadvisor.service.TaxAdvisorService;
    import com.example.taxadvisor.util.MockSchemeDatabase;
    import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

    class TaxAdvisorServiceTest {

        private TaxAdvisorService service;

        @BeforeEach
        void setUp() {
            service = new TaxAdvisorService();
        }

        @Test
        void testGetTaxAdvice_LowRiskProfile() {
            TaxAdviceRequest request = new TaxAdviceRequest(
                    1000000, // income
                    200000,  // amountToInvest
                    50000,   // currentEPF
                    "LOW",   // riskProfilea
                    "SHORT"  // lockInPeriod
            );

            TaxAdviceResponse response = service.getTaxAdvice(request);

            assertNotNull(response);
            assertTrue(response.getTaxBeforeInvestment() > response.getTaxAfterInvestment());
            assertTrue(response.getTaxBeforeInvestment() > 0);
            assertFalse(response.getRecommendations().isEmpty());

            // Verify only LOW risk schemes are recommended
            response.getRecommendations().forEach(rec -> {
                InvestmentScheme scheme = MockSchemeDatabase.getAllSchemes().stream()
                        .filter(s -> s.getName().equals(rec.getSchemeName()))
                        .findFirst()
                        .orElseThrow();
                assertEquals("LOW", scheme.getRiskProfile());
            });
        }

        @Test
        void testGetTaxAdvice_80CLimitCheck() {
            TaxAdviceRequest request = new TaxAdviceRequest(
                    1000000,  // income
                    200000,   // amountToInvest
                    140000,   // currentEPF (near 80C limit)
                    "HIGH",   // riskProfile
                    "LONG"    // lockInPeriod
            );

            TaxAdviceResponse response = service.getTaxAdvice(request);

            // Check 80C limit isn't exceeded
            double total80C = request.getCurrentEPFContribution() +
                    response.getRecommendations().stream()
                            .filter(r -> r.getSection().equals("80C"))
                            .mapToDouble(SchemeRecommendation::getAmountAllocated)
                            .sum();

            assertTrue(total80C <= 150000);
        }

        @Test
        void testGetTaxAdvice_MaxInvestmentLimits() {
            TaxAdviceRequest request = new TaxAdviceRequest(
                    1000000, // income
                    500000,  // amountToInvest (high amount)
                    0,       // currentEPF
                    "HIGH",  // riskProfile
                    "LONG"   // lockInPeriod
            );

            TaxAdviceResponse response = service.getTaxAdvice(request);

            // Verify section-wise limits
            double total80CCD1 = response.getRecommendations().stream()
                    .filter(r -> r.getSection().equals("80CCD(1)"))
                    .mapToDouble(SchemeRecommendation::getAmountAllocated)
                    .sum();
            assertTrue(total80CCD1 <= 50000);

            double total80CCD1B = response.getRecommendations().stream()
                    .filter(r -> r.getSection().equals("80CCD(1B)"))
                    .mapToDouble(SchemeRecommendation::getAmountAllocated)
                    .sum();
            assertTrue(total80CCD1B <= 50000);
        }

}
