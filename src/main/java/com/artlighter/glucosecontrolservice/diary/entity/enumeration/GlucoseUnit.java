package com.artlighter.glucosecontrolservice.diary.entity.enumeration;

public enum GlucoseUnit {
    MILLIMOLES_PER_LITER("mmol/L", 1.0),
    MILLIGRAMS_PER_DECILITER("mg/dL", 18.0);

    private final String stringRepresentation;
    private final double mmolPerLiterToThisUnitCoefficient;

    GlucoseUnit(String stringRepresentation, double mmolPerLiterToThisUnitCoefficient) {
        this.stringRepresentation = stringRepresentation;
        this.mmolPerLiterToThisUnitCoefficient = mmolPerLiterToThisUnitCoefficient;
    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public float convertFromMmolPerLiter(float glucoseInMmolPerLiter) {
        return (float) (glucoseInMmolPerLiter * this.mmolPerLiterToThisUnitCoefficient);
    }

    public float convertToMmolPerLiter(float glucoseInThisUnit) {
        return (float) (glucoseInThisUnit / this.mmolPerLiterToThisUnitCoefficient);
    }
}
