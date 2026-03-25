package com.artlighter.glucosecontrolservice.calculations.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "insulin_profile")
public class InsulinProfile {
    @Id
    @Column(name = "patient_profile_id")
    private int patientProfileId;
    @Column(name = "default_icr")
    private float defaultInsulinToCarbsRatio;
    @Column(name = "default_isf")
    private float defaultInsulinSensitivityFactor;
    @Column(name = "dia")
    private int durationOfInsulinAction;
    @OneToMany(mappedBy = "insulinProfile", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timeOfDay")
    private List<InsulinSensitivityFactor> factorsByTime;
    @OneToMany(mappedBy = "insulinProfile", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timeOfDay")
    private List<InsulinToCarbsRatio> ratiosByTime;

    public InsulinProfile() {
    }

    public InsulinProfile(int patientProfileId, float defaultInsulinToCarbsRatio, float defaultInsulinSensitivityFactor,
                          int durationOfInsulinAction, List<InsulinSensitivityFactor> factorsByTime,
                          List<InsulinToCarbsRatio> ratiosByTime) {
        this.patientProfileId = patientProfileId;
        this.defaultInsulinToCarbsRatio = defaultInsulinToCarbsRatio;
        this.defaultInsulinSensitivityFactor = defaultInsulinSensitivityFactor;
        this.durationOfInsulinAction = durationOfInsulinAction;
        this.factorsByTime = factorsByTime;
        this.ratiosByTime = ratiosByTime;
    }

    public int getPatientProfileId() {
        return patientProfileId;
    }

    public void setPatientProfileId(int profileId) {
        this.patientProfileId = profileId;
    }

    public float getDefaultInsulinToCarbsRatio() {
        return defaultInsulinToCarbsRatio;
    }

    public void setDefaultInsulinToCarbsRatio(float defaultInsulinToCarbsRatio) {
        this.defaultInsulinToCarbsRatio = defaultInsulinToCarbsRatio;
    }

    public float getDefaultInsulinSensitivityFactor() {
        return defaultInsulinSensitivityFactor;
    }

    public void setDefaultInsulinSensitivityFactor(float defaultInsulinSensitivityFactor) {
        this.defaultInsulinSensitivityFactor = defaultInsulinSensitivityFactor;
    }

    public int getDurationOfInsulinAction() {
        return durationOfInsulinAction;
    }

    public void setDurationOfInsulinAction(int durationOfInsulinAction) {
        this.durationOfInsulinAction = durationOfInsulinAction;
    }

    public List<InsulinSensitivityFactor> getFactorsByTime() {
        return factorsByTime;
    }

    public void setFactorsByTime(List<InsulinSensitivityFactor> factorsByTime) {
        this.factorsByTime = factorsByTime;
    }

    public List<InsulinToCarbsRatio> getRatiosByTime() {
        return ratiosByTime;
    }

    public void setRatiosByTime(List<InsulinToCarbsRatio> ratiosByTime) {
        this.ratiosByTime = ratiosByTime;
    }


}
