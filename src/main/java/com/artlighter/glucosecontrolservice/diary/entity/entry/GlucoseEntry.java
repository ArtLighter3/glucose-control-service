package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.MeasurementType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "glucose_entry")
public class GlucoseEntry extends DiaryEntry {
    private Float value;
    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private MeasurementType measurementType;

//    public GlucoseEntry() {
//        super();
//    }
//
//    public GlucoseEntry(Float value, MeasurementType measurementType, Instant co) {
//        this.value = value;
//    }

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
