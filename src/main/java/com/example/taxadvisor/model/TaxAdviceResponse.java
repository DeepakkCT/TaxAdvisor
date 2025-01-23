package com.example.taxadvisor.model;

import java.util.List;

public class TaxAdviceResponse {
    private double taxBeforeInvestment;
    private double taxAfterInvestment;
    private double totalTaxSaved;
    private List<SchemeRecommendation> recommendations;
    private double leftOverAmount;
    private String buttonName;

    public TaxAdviceResponse() {
    }

    public TaxAdviceResponse(double taxBeforeInvestment, double taxAfterInvestment,
                             double totalTaxSaved, List<SchemeRecommendation> recommendations,
                             double leftOverAmount, String buttonName) {
        this.taxBeforeInvestment = taxBeforeInvestment;
        this.taxAfterInvestment = taxAfterInvestment;
        this.totalTaxSaved = totalTaxSaved;
        this.recommendations = recommendations;
        this.leftOverAmount = leftOverAmount;
        this.buttonName = buttonName;
    }

    // Getters and setters
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

    public double getLeftOverAmount() {
        return leftOverAmount;
    }

    public void setLeftOverAmount(double leftOverAmount) {
        this.leftOverAmount = leftOverAmount;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }
}