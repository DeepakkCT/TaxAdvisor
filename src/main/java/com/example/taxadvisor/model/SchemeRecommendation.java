package com.example.taxadvisor.model;

public class SchemeRecommendation {
    private String schemeName;
    private double amountAllocated;
    private String section;
    private double expectedReturnRate;

    public SchemeRecommendation() {
    }

    public SchemeRecommendation(String schemeName, double amountAllocated, String section, double expectedReturnRate) {
        this.schemeName = schemeName;
        this.amountAllocated = amountAllocated;
        this.section = section;
        this.expectedReturnRate = expectedReturnRate;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public double getAmountAllocated() {
        return amountAllocated;
    }

    public void setAmountAllocated(double amountAllocated) {
        this.amountAllocated = amountAllocated;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getExpectedReturnRate() {
        return expectedReturnRate;
    }

    public void setExpectedReturnRate(double expectedReturnRate) {
        this.expectedReturnRate = expectedReturnRate;
    }
}
