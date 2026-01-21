package com.artlighter.glucosecontrolservice.diary.entity;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.CarbsUnit;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.GlucoseUnit;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "patient_profile")
public class PatientProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private GlucoseUnit glucoseUnit;
    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private CarbsUnit carbsUnit;
    private int diabetesType;
    private int userId;
    private float hyperGlucose;
    private float highGlucose;
    private float lowGlucose;
    private float hypoGlucose;

    public PatientProfile(int id, GlucoseUnit glucoseUnit, CarbsUnit carbsUnit, int diabetesType, int userId,
                          float hyperGlucose, float highGlucose, float lowGlucose, float hypoGlucose) {
        this.id = id;
        this.glucoseUnit = glucoseUnit;
        this.carbsUnit = carbsUnit;
        this.diabetesType = diabetesType;
        this.userId = userId;
        this.hyperGlucose = hyperGlucose;
        this.highGlucose = highGlucose;
        this.lowGlucose = lowGlucose;
        this.hypoGlucose = hypoGlucose;
    }

    public PatientProfile() {
    }

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

    public float getHyperGlucose() {
        return hyperGlucose;
    }

    public void setHyperGlucose(float hyperGlucose) {
        this.hyperGlucose = hyperGlucose;
    }

    public float getHighGlucose() {
        return highGlucose;
    }

    public void setHighGlucose(float highGlucose) {
        this.highGlucose = highGlucose;
    }

    public float getLowGlucose() {
        return lowGlucose;
    }

    public void setLowGlucose(float lowGlucose) {
        this.lowGlucose = lowGlucose;
    }

    public float getHypoGlucose() {
        return hypoGlucose;
    }

    public void setHypoGlucose(float hypoGlucose) {
        this.hypoGlucose = hypoGlucose;
    }
}
