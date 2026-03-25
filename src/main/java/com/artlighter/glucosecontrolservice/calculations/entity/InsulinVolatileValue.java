package com.artlighter.glucosecontrolservice.calculations.entity;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Общий абстрактный класс для сущностей, представляющих параметры для инсулинового профиля, значения которых
 * зависят от времени суток.
 */
@MappedSuperclass
@IdClass(InsulinVolatileValue.InsulinVolatileValueID.class)
public abstract class InsulinVolatileValue {
    @Id
    protected LocalTime timeOfDay;
    @Id
    @ManyToOne
    @JoinColumn(name = "insulin_profile_id", referencedColumnName = "patient_profile_id")
    protected InsulinProfile insulinProfile;

    public abstract float getValue();

    public abstract void setValue(float value);

    public LocalTime getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(LocalTime timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public InsulinProfile getInsulinProfile() {
        return insulinProfile;
    }

    public void setInsulinProfile(InsulinProfile insulinProfile) {
        this.insulinProfile = insulinProfile;
    }

    static class InsulinVolatileValueID {
        private LocalTime timeOfDay;
        private InsulinProfile insulinProfile;

        public LocalTime getTimeOfDay() {
            return timeOfDay;
        }

        public void setTimeOfDay(LocalTime timeOfDay) {
            this.timeOfDay = timeOfDay;
        }

        public InsulinProfile getInsulinProfile() {
            return insulinProfile;
        }

        public void setInsulinProfile(InsulinProfile insulinProfile) {
            this.insulinProfile = insulinProfile;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            InsulinVolatileValueID that = (InsulinVolatileValueID) o;
            return Objects.equals(timeOfDay, that.timeOfDay) && Objects.equals(insulinProfile, that.insulinProfile);
        }

        @Override
        public int hashCode() {
            return Objects.hash(timeOfDay, insulinProfile);
        }
    }

}
