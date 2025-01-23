package com.example.taxadvisor.model;

public class InvestmentScheme {
    private String name;
    private String description;
    private double maxLimit;
    private String section;       // e.g. "80C", "80CCD(1)", "80CCD(1B)"
    private String riskProfile;   // LOW, MEDIUM, HIGH
    private String lockInPeriod;  // SHORT, MEDIUM, LONG
    private double expectedReturns;

    public InvestmentScheme() {
    }

    public InvestmentScheme(String name, String description, double maxLimit, String section,
                            String riskProfile, String lockInPeriod, double expectedReturns) {
        this.name = name;
        this.description = description;
        this.maxLimit = maxLimit;
        this.section = section;
        this.riskProfile = riskProfile;
        this.lockInPeriod = lockInPeriod;
        this.expectedReturns = expectedReturns;
    }

    // Getters and Setters
    // ...
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRiskProfile() {
        return riskProfile;
    }

    public void setRiskProfile(String riskProfile) {
        this.riskProfile = riskProfile;
    }

    public String getLockInPeriod() {
        return lockInPeriod;
    }

    public void setLockInPeriod(String lockInPeriod) {
        this.lockInPeriod = lockInPeriod;
    }

    public double getExpectedReturns() {
        return expectedReturns;
    }

    public void setExpectedReturns(double expectedReturns) {
        this.expectedReturns = expectedReturns;
    }
}
