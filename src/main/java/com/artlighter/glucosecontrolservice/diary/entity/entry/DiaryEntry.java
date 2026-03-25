package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

//TODO МБ добавить поле DiaryEntryType?

/**
 * Общий класс для записи дневника самоконтроля. Определенные типы (измерение глюкозы, ввод инсулина и т.д.)
 * должны быть реализациями. Содержит общие для любого типа поля, а именно численное значение, временную отметку
 * совершения, профиль пользователя, к которому относится, и текстовый комментарий.
 */
@MappedSuperclass
@IdClass(DiaryEntry.DiaryEntryID.class)
public abstract class DiaryEntry {
    //@EmbeddedId
    //protected DiaryEntryID id;
    private String notes;
    //private int profileId;
  //  @MapsId("profileId")
    @Id
    private int profileId;
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

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DiaryEntry that = (DiaryEntry) o;
        return Objects.equals(profileId, that.profileId) && Objects.equals(commitedAt, that.commitedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, commitedAt);
    }

    //@Embeddable
    public static class DiaryEntryID implements Serializable {
        //private int profileId;
        private Instant commitedAt;
        private int profileId;

//        public int getProfileId() {
//            return profileId;
//        }
//
//        public void setProfileId(int profileId) {
//            this.profileId = profileId;
//        }


        public DiaryEntryID() {
        }

        public DiaryEntryID(int profileId, Instant commitedAt) {
            this.profileId = profileId;
            this.commitedAt = commitedAt;
        }

        public Instant getCommitedAt() {
            return commitedAt;
        }

        public void setCommitedAt(Instant commitedAt) {
            this.commitedAt = commitedAt;
        }

        public int getProfileId() {
            return profileId;
        }

        public void setProfileId(int profileId) {
            this.profileId = profileId;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            DiaryEntryID that = (DiaryEntryID) o;
            return Objects.equals(commitedAt, that.commitedAt) && Objects.equals(profileId, that.profileId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(commitedAt, profileId);
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
