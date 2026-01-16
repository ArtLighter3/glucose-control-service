package com.artlighter.glucosecontrolservice.calculations.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "insulin_sensitivity_factor")
public class InsulinSensitivityFactor extends InsulinVolatileValue {
    @Column(name = "isf")
    private float value;

    public InsulinSensitivityFactor() {
    }

    public InsulinSensitivityFactor(float value, LocalTime timeOfDay, InsulinProfile insulinProfile) {
        this.value = value;
        this.timeOfDay = timeOfDay;
        this.insulinProfile = insulinProfile;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setValue(float value) {
        this.value = value;
    }

}
