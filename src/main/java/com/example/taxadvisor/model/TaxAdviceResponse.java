package com.example.taxadvisor.model;

import java.util.List;

public class TaxAdviceResponse {
    private double taxBeforeInvestment;
    private double taxAfterInvestment;
    private double totalTaxSaved;
    private List<SchemeRecommendation> recommendations;

    public TaxAdviceResponse() {
    }

    public TaxAdviceResponse(double taxBeforeInvestment, double taxAfterInvestment,
                             double totalTaxSaved, List<SchemeRecommendation> recommendations) {
        this.taxBeforeInvestment = taxBeforeInvestment;
        this.taxAfterInvestment = taxAfterInvestment;
        this.totalTaxSaved = totalTaxSaved;
        this.recommendations = recommendations;
    }

    public double getTaxBeforeInvestment() {
        return taxBeforeInvestment;
    }

    public void setTaxBeforeInvestment(double taxBeforeInvestment) {
        this.taxBeforeInvestment = taxBeforeInvestment;
    }

    public double getTaxAfterInvestment() {
        return taxAfterInvestment;
    }

    public void setTaxAfterInvestment(double taxAfterInvestment) {
        this.taxAfterInvestment = taxAfterInvestment;
    }

    public double getTotalTaxSaved() {
        return totalTaxSaved;
    }

    public void setTotalTaxSaved(double totalTaxSaved) {
        this.totalTaxSaved = totalTaxSaved;
    }

    public List<SchemeRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<SchemeRecommendation> recommendations) {
        this.recommendations = recommendations;
    }
}
