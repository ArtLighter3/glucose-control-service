package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.MeasurementType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "glucose_entry")
public class GlucoseEntry extends DiaryEntry {
    private Float value;
    @Enumerated(EnumType.STRING)
    private MeasurementType measurementType;

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Number value) {
        this.value = value.floatValue();
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }
}
