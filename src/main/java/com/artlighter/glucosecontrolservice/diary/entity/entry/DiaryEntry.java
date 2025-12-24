package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
public abstract class DiaryEntry {
    @EmbeddedId
    private DiaryEntryID id;
    private String notes;

//    public DiaryEntryID getId() {
//        return id;
//    }
//
//    public void setId(DiaryEntryID id) {
//        this.id = id;
//    }
    public abstract Number getValue();
    public abstract void setValue(Number value);

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PatientProfile getPatientProfile() {
        if (id == null) return null;
        return id.getPatientProfile();
    }

    public Instant getCommitedAt() {
        if (id == null) return null;
        return id.getCommitedAt();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DiaryEntry that = (DiaryEntry) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Embeddable
    public static class DiaryEntryID implements Serializable {
        @ManyToOne
        @JoinColumn(name = "profile_id", referencedColumnName = "id")
        private PatientProfile patientProfile;
        private Instant commitedAt;

        public PatientProfile getPatientProfile() {
            return patientProfile;
        }

        public void setPatientProfile(PatientProfile patientProfile) {
            this.patientProfile = patientProfile;
        }

        public Instant getCommitedAt() {
            return commitedAt;
        }

        public void setCommitedAt(Instant commitedAt) {
            this.commitedAt = commitedAt;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            DiaryEntryID that = (DiaryEntryID) o;
            return Objects.equals(patientProfile, that.patientProfile) && Objects.equals(commitedAt, that.commitedAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(patientProfile, commitedAt);
        }
    }
}
