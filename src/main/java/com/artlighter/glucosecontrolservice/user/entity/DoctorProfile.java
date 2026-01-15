package com.artlighter.glucosecontrolservice.user.entity;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "doctor_profile")
public class DoctorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "patient_doctor",
            joinColumns = @JoinColumn(name = "doctor_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_profile_id"))
    @MapKey(name = "userId")
    private Map<Integer, PatientProfile> attachedPatients;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Map<Integer, PatientProfile> getAttachedPatients() {
        return attachedPatients;
    }

    public void setAttachedPatients(Map<Integer, PatientProfile> attachedPatients) {
        this.attachedPatients = attachedPatients;
    }
}
