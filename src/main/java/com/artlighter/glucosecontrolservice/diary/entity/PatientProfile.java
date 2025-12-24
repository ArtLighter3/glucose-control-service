package com.artlighter.glucosecontrolservice.diary.entity;

import com.artlighter.glucosecontrolservice.auth.entity.User;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.CarbsUnit;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.GlucoseUnit;
import jakarta.persistence.*;

@Entity
@Table(name = "patient_profile")
public class PatientProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private GlucoseUnit glucoseUnit;
    @Enumerated(EnumType.STRING)
    private CarbsUnit carbsUnit;
    private int diabetesType;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GlucoseUnit getGlucoseUnit() {
        return glucoseUnit;
    }

    public void setGlucoseUnit(GlucoseUnit glucoseUnit) {
        this.glucoseUnit = glucoseUnit;
    }

    public CarbsUnit getCarbsUnit() {
        return carbsUnit;
    }

    public void setCarbsUnit(CarbsUnit carbsUnit) {
        this.carbsUnit = carbsUnit;
    }

    public int getDiabetesType() {
        return diabetesType;
    }

    public void setDiabetesType(int diabetesType) {
        this.diabetesType = diabetesType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
