package com.artlighter.glucosecontrolservice.user.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "doctor_profile")
public class DoctorProfile {
    @Id
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    @MapsId
    private User user;
    private String personalSecret;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "patient_doctor",
            joinColumns = @JoinColumn(name = "doctor_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_profile_id"))
//    @MapKey(name = "userId")
//    private Map<Integer, PatientProfile> attachedPatients;
    private Set<PatientProfile> attachedPatients;

    public DoctorProfile() {
    }

    public DoctorProfile(User user) {
        this.user = user;
    }

    public DoctorProfile(int id) {
        this.id = id;
    }

    public DoctorProfile(int id, Set<PatientProfile> attachedPatients) {
        this.id = id;
        this.attachedPatients = attachedPatients;
    }

    public DoctorProfile(int id, String personalSecret, Set<PatientProfile> attachedPatients) {
        this.id = id;
        this.personalSecret = personalSecret;
        this.attachedPatients = attachedPatients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //    public Map<Integer, PatientProfile> getAttachedPatients() {
//        return attachedPatients;
//    }
//
//    public void setAttachedPatients(Map<Integer, PatientProfile> attachedPatients) {
//        this.attachedPatients = attachedPatients;
//    }


    public String getPersonalSecret() {
        return personalSecret;
    }

    public void setPersonalSecret(String personalSecret) {
        this.personalSecret = personalSecret;
    }

    public Set<PatientProfile> getAttachedPatients() {
        return attachedPatients;
    }

    public void setAttachedPatients(Set<PatientProfile> attachedPatients) {
        this.attachedPatients = attachedPatients;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DoctorProfile that = (DoctorProfile) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
