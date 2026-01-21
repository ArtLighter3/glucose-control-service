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
    private Float hyperGlucose;
    private Float highGlucose;
    private Float lowGlucose;
    private Float hypoGlucose;

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

    public Float getHyperGlucose() {
        return hyperGlucose;
    }

    public void setHyperGlucose(Float hyperGlucose) {
        this.hyperGlucose = hyperGlucose;
    }

    public Float getHighGlucose() {
        return highGlucose;
    }

    public void setHighGlucose(Float highGlucose) {
        this.highGlucose = highGlucose;
    }

    public Float getLowGlucose() {
        return lowGlucose;
    }

    public void setLowGlucose(Float lowGlucose) {
        this.lowGlucose = lowGlucose;
    }

    public Float getHypoGlucose() {
        return hypoGlucose;
    }

    public void setHypoGlucose(Float hypoGlucose) {
        this.hypoGlucose = hypoGlucose;
    }
}
