package com.artlighter.glucosecontrolservice.templates.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient_meal")
public class Meal extends PatientTemplateEntity {
    @Column(name = "carbs_per_100_grams")
    private float carbsPer100Grams;

    public Meal() {
        super();
    }

    public Meal(PatientTemplateEntityID id, float carbsPer100Grams) {
        super(id);
        this.carbsPer100Grams = carbsPer100Grams;
    }

    public float getCarbsPer100Grams() {
        return carbsPer100Grams;
    }

    public void setCarbsPer100Grams(float carbsPer100Grams) {
        this.carbsPer100Grams = carbsPer100Grams;
    }
}
