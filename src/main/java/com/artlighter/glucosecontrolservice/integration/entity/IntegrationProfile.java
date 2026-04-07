package com.artlighter.glucosecontrolservice.integration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class IntegrationProfile {
    @Id
    private int patientProfileId;
    @Column(name = "is_nightscout_enabled")
    private boolean nightscoutEnabled;
    private String nightscoutApiSecret;

    public IntegrationProfile(int patientProfileId, boolean nightscoutEnabled, String nightscoutApiSecret) {
        this.patientProfileId = patientProfileId;
        this.nightscoutEnabled = nightscoutEnabled;
        this.nightscoutApiSecret = nightscoutApiSecret;
    }

    public IntegrationProfile() {

    }

    public int getPatientProfileId() {
        return patientProfileId;
    }

    public void setPatientProfileId(int patientProfileId) {
        this.patientProfileId = patientProfileId;
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
}
