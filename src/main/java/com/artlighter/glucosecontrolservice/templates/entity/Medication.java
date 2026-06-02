package com.artlighter.glucosecontrolservice.templates.entity;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.PortionType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "patient_medication")
public class Medication extends PatientTemplateEntity {
    private float milligramsInPortion;
    private float defaultPortions;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "portiontype")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private PortionType portionType;

    public Medication() {
        super();
    }

    public Medication(PatientTemplateEntityID id, float milligramsInPortion, float defaultPortions,
                      PortionType portionType) {
        super(id);
        this.milligramsInPortion = milligramsInPortion;
        this.defaultPortions = defaultPortions;
        this.portionType = portionType;
    }

    public float getMilligramsInPortion() {
        return milligramsInPortion;
    }

    public void setMilligramsInPortion(float milligramsInPortion) {
        this.milligramsInPortion = milligramsInPortion;
    }

    public float getDefaultPortions() {
        return defaultPortions;
    }

    public void setDefaultPortions(float defaultPortions) {
        this.defaultPortions = defaultPortions;
    }

    public PortionType getPortionType() {
        return portionType;
    }

    public void setPortionType(PortionType portionType) {
        this.portionType = portionType;
    }
}
