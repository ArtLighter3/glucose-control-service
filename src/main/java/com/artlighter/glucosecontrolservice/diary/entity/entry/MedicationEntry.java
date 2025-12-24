package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.MeasurementType;
import jakarta.persistence.*;

@Entity
@Table(name = "medication_entry")
public class MedicationEntry extends DiaryEntry {
    private Float value;
    @Column(name = "name")
    private String medicationName;

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Number value) {
        this.value = value.floatValue();
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }
}
