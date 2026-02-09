package com.artlighter.glucosecontrolservice.templates.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

/**
 * Общий интерфейс для сущностей, являющихся заготовками для больных,
 * которые они могут использовать для быстрого заполнения записей дневника самоконтроля, не запоминая значения.
 * (по типу сохраненных блюд с заданными значениями углеводов на 100г, которые можно использовать, чтобы подставить
 * количество углеводов при записи принятых углеводов без надобности смотреть их количество на упаковках, так как блюдо
 * сохранено как заготовка)
 */
@MappedSuperclass
public abstract class PatientTemplateEntity {
    @EmbeddedId
    protected PatientTemplateEntityID id;

    public PatientTemplateEntity() {

    }

    public PatientTemplateEntity(PatientTemplateEntityID id) {
        this.id = id;
    }

    public PatientTemplateEntityID getId() {
        return id;
    }

    public void setId(PatientTemplateEntityID id) {
        this.id = id;
    }

    @Embeddable
    public static class PatientTemplateEntityID {
        @Column(name = "profile_id")
        private int patientProfileId;
        private String name;

        public PatientTemplateEntityID(int patientProfileId, String name) {
            this.patientProfileId = patientProfileId;
            this.name = name;
        }

        public PatientTemplateEntityID() {

        }

        public int getPatientProfileId() {
            return patientProfileId;
        }

        public void setPatientProfileId(int patientProfileId) {
            this.patientProfileId = patientProfileId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PatientTemplateEntityID that = (PatientTemplateEntityID) o;
            return patientProfileId == that.patientProfileId && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(patientProfileId, name);
        }
    }
}
