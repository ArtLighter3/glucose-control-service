package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.PortionType;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "medication_entry")
public class MedicationEntry extends DiaryEntry {
    private Float value;
    @Column(name = "name")
    private String medicationName;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "portiontype")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private PortionType portionType;
    private Float milligramsInPortion;

    public MedicationEntry() {
        this.type = DiaryEntryType.MEDICATION_ENTRY;
    }

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

    public PortionType getPortionType() {
        return portionType;
    }

    public void setPortionType(PortionType portionType) {
        this.portionType = portionType;
    }

    public Float getMilligramsInPortion() {
        return milligramsInPortion;
    }

    public void setMilligramsInPortion(Float milligramsInPortion) {
        this.milligramsInPortion = milligramsInPortion;
    }
}
