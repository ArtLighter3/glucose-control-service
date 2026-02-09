package com.artlighter.glucosecontrolservice.templates.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient_medication")
public class Medication extends PatientTemplateEntity {
    private float milligramsInPortion;
    private int defaultPortions;

    public Medication() {
        super();
    }

    public Medication(PatientTemplateEntityID id, float milligramsInPortion, int defaultPortions) {
        super(id);
        this.milligramsInPortion = milligramsInPortion;
        this.defaultPortions = defaultPortions;
    }

    public float getMilligramsInPortion() {
        return milligramsInPortion;
    }

    public void setMilligramsInPortion(float milligramsInPortion) {
        this.milligramsInPortion = milligramsInPortion;
    }

    public int getDefaultPortions() {
        return defaultPortions;
    }

    public void setDefaultPortions(int defaultPortions) {
        this.defaultPortions = defaultPortions;
    }
}
