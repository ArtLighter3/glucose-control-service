package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
@IdClass(DiaryEntry.DiaryEntryID.class)
public abstract class DiaryEntry {
    //@EmbeddedId
    //protected DiaryEntryID id;
    private String notes;
    //private int profileId;
  //  @MapsId("profileId")
    @Id
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private PatientProfile patientProfile;

    @Id
    private Instant commitedAt;

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

//    public int getProfileId() {
//        return profileId;
//    }
//
//    public void setProfileId(int profileId) {
//        this.profileId = profileId;
//    }

    //    public Instant getCommitedAt() {
//        if (id == null) return null;
//        return id.getCommitedAt();
//    }
//
//    public void setCommitedAt(Instant commitedAt) {
//        if (id == null) return;
//        id.setCommitedAt(commitedAt);
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        DiaryEntry that = (DiaryEntry) o;
//        return Objects.equals(id, that.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(id);
//    }

    //@Embeddable
    public static class DiaryEntryID implements Serializable {
        //private int profileId;
        private Instant commitedAt;
        private PatientProfile patientProfile;

//        public int getProfileId() {
//            return profileId;
//        }
//
//        public void setProfileId(int profileId) {
//            this.profileId = profileId;
//        }


        public DiaryEntryID() {
        }

        public DiaryEntryID(PatientProfile patientProfile, Instant commitedAt) {
            this.patientProfile = patientProfile;
            this.commitedAt = commitedAt;
        }

        public Instant getCommitedAt() {
            return commitedAt;
        }

        public void setCommitedAt(Instant commitedAt) {
            this.commitedAt = commitedAt;
        }

        public PatientProfile getPatientProfile() {
            return patientProfile;
        }

        public void setPatientProfile(PatientProfile patientProfile) {
            this.patientProfile = patientProfile;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            DiaryEntryID that = (DiaryEntryID) o;
            return Objects.equals(commitedAt, that.commitedAt) && Objects.equals(patientProfile, that.patientProfile);
        }

        @Override
        public int hashCode() {
            return Objects.hash(commitedAt, patientProfile);
        }

//                @Override
//        public boolean equals(Object o) {
//            if (o == null || getClass() != o.getClass()) return false;
//            DiaryEntryID that = (DiaryEntryID) o;
//            return profileId == that.profileId && Objects.equals(commitedAt, that.commitedAt);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(profileId, commitedAt);
//        }
    }
}
