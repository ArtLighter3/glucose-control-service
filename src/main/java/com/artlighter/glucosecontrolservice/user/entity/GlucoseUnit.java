package com.artlighter.glucosecontrolservice.user.entity;

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

    public double convertFromMmolPerLiter(double glucoseInMmolPerLiter) {
        return (glucoseInMmolPerLiter * this.mmolPerLiterToThisUnitCoefficient);
    }

    public double convertToMmolPerLiter(double glucoseInThisUnit) {
        return (glucoseInThisUnit / this.mmolPerLiterToThisUnitCoefficient);
    }

    public double getMmolPerLiterToThisUnitCoefficient() {
        return mmolPerLiterToThisUnitCoefficient;
    }
}
