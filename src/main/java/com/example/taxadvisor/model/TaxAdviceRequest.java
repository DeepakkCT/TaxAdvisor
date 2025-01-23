package com.example.taxadvisor.model;

import lombok.Data;

@Data
public class TaxAdviceRequest {
    private double income;
    private double amountToInvest;
    private int age;
    private String maritalStatus; // e.g., "SINGLE", "MARRIED"
    private double currentEPFContribution;
    private String riskProfile;  // e.g., "LOW", "MEDIUM", "HIGH"
    private String lockInPeriod; // e.g., "SHORT", "MEDIUM", "LONG"
    private boolean hasHealthInsurance;


    public TaxAdviceRequest( double income ,double amountToInvest, double currentEPFContribution,String riskProfile,String lockInPeriod) {
        this.income = income;
        this.amountToInvest = amountToInvest;
        this.currentEPFContribution = currentEPFContribution;
        this.riskProfile = riskProfile;
        this.lockInPeriod = lockInPeriod;
    }


    // Getters and Setters
    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getAmountToInvest() {
        return amountToInvest;
    }

    public void setAmountToInvest(double amountToInvest) {
        this.amountToInvest = amountToInvest;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public double getCurrentEPFContribution() {
        return currentEPFContribution;
    }

    public void setCurrentEPFContribution(double currentEPFContribution) {
        this.currentEPFContribution = currentEPFContribution;
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

    public boolean isHasHealthInsurance() {
        return hasHealthInsurance;
    }

    public void setHasHealthInsurance(boolean hasHealthInsurance) {
        this.hasHealthInsurance = hasHealthInsurance;
    }


}
