package com.artlighter.glucosecontrolservice.diary.entity.entry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "meal_entry")
public class MealEntry extends DiaryEntry {
    private Float value;

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Number value) {
        this.value = value.floatValue();
    }
}
