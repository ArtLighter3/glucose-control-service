package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Общий класс для записи дневника самоконтроля. Определенные типы (измерение глюкозы, ввод инсулина и т.д.)
 * должны быть реализациями. Содержит общие для любого типа поля, а именно численное значение, временную отметку
 * совершения, ID профиля пользователя, к которому относится, и текстовый комментарий.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(DiaryEntry.DiaryEntryID.class)
public abstract class DiaryEntry {
    private String notes;
    @Id
    private int profileId;
    @Id
    private Instant commitedAt;
    //Пришлось добавить поле type для исправления слияния записей разных типов в одну сущность при UNION выборке с БД
    @Id
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "diaryentrytype")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    protected DiaryEntryType type;

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

    public DiaryEntryType getType() {
        return type;
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
        private DiaryEntryType type;

//        public int getProfileId() {
//            return profileId;
//        }
//
//        public void setProfileId(int profileId) {
//            this.profileId = profileId;
//        }


        public DiaryEntryID() {
        }

        public DiaryEntryID(int profileId, Instant commitedAt, DiaryEntryType type) {
            this.profileId = profileId;
            this.commitedAt = commitedAt;
            this.type = type;
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

        public DiaryEntryType getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            DiaryEntryID that = (DiaryEntryID) o;
            return Objects.equals(commitedAt, that.commitedAt) && Objects.equals(profileId, that.profileId) &&
                    Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(commitedAt, profileId, type);
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
