package com.artlighter.glucosecontrolservice.user.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "patient_profile")
public class PatientProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private GlucoseUnit glucoseUnit;
    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private CarbsUnit carbsUnit;
    private int diabetesType;
//    @Column(name = "user_id")
//    private int userId;
    private float hyperGlucose;
    private float highGlucose;
    private float lowGlucose;
    private float hypoGlucose;
    @Column(name = "is_nightscout_enabled")
    private boolean nightscoutEnabled;
    private String nightscoutApiSecret;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "attachedPatients")
    Set<DoctorProfile> doctors;
//
//    public PatientProfile(int id, GlucoseUnit glucoseUnit, CarbsUnit carbsUnit, int diabetesType, int userId,
//                          float hyperGlucose, float highGlucose, float lowGlucose, float hypoGlucose) {
//        this.id = id;
//        this.glucoseUnit = glucoseUnit;
//        this.carbsUnit = carbsUnit;
//        this.diabetesType = diabetesType;
//        this.userId = userId;
//        this.hyperGlucose = hyperGlucose;
//        this.highGlucose = highGlucose;
//        this.lowGlucose = lowGlucose;
//        this.hypoGlucose = hypoGlucose;
//    }

    public PatientProfile(int id, User user, GlucoseUnit glucoseUnit, CarbsUnit carbsUnit, int diabetesType,
                          float hyperGlucose, float highGlucose, float lowGlucose, float hypoGlucose) {
        this.id = id;
        this.user = user;
        this.glucoseUnit = glucoseUnit;
        this.carbsUnit = carbsUnit;
        this.diabetesType = diabetesType;
        this.hyperGlucose = hyperGlucose;
        this.highGlucose = highGlucose;
        this.lowGlucose = lowGlucose;
        this.hypoGlucose = hypoGlucose;
    }

    public PatientProfile(int id, User user, GlucoseUnit glucoseUnit, CarbsUnit carbsUnit, int diabetesType,
                          float hyperGlucose, float highGlucose, float lowGlucose, float hypoGlucose,
                          boolean nightscoutEnabled, String nightscoutApiSecret) {
        this(id, user, glucoseUnit, carbsUnit, diabetesType, hyperGlucose, highGlucose, lowGlucose, hypoGlucose);
        this.nightscoutEnabled = nightscoutEnabled;
        this.nightscoutApiSecret = nightscoutApiSecret;
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

//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

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
//
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<DoctorProfile> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<DoctorProfile> doctors) {
        this.doctors = doctors;
    }

    public boolean isNightscoutEnabled() {
        return nightscoutEnabled;
    }

    public void setNightscoutEnabled(boolean nightscoutEnabled) {
        this.nightscoutEnabled = nightscoutEnabled;
    }

    public String getNightscoutApiSecret() {
        return nightscoutApiSecret;
    }

    public void setNightscoutApiSecret(String nightscoutApiSecret) {
        this.nightscoutApiSecret = nightscoutApiSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PatientProfile that = (PatientProfile) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
