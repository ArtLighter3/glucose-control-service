package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import jakarta.persistence.*;

//TODO ух ты, а с чего у нас в расчетах общей дозировки можно выбрать неск. препаратов, а тут ввести имя только одного?
@Entity
@Table(name = "medication_entry")
public class MedicationEntry extends DiaryEntry {
    private Float value;
    @Column(name = "name")
    private String medicationName;

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
}
