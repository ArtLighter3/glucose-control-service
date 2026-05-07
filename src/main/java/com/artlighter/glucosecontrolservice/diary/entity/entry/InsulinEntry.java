package com.artlighter.glucosecontrolservice.diary.entity.entry;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.InsulinType;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "insulin_entry")
public class InsulinEntry extends DiaryEntry {
    private Float value;
    @Enumerated
    @Column(columnDefinition = "insulintype")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private InsulinType insulinType;

    public InsulinEntry() {
        this.type = DiaryEntryType.INSULIN_ENTRY;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Number value) {
        this.value = value.floatValue();
    }

    public InsulinType getInsulinType() {
        return insulinType;
    }

    public void setInsulinType(InsulinType insulinType) {
        this.insulinType = insulinType;
    }
}
