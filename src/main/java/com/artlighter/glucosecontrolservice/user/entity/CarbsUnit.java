package com.artlighter.glucosecontrolservice.user.entity;

public enum CarbsUnit {
    GRAMS("grams", 1.0),
    BREAD_UNITS_10("BU (10)", 10.0),
    BREAD_UNITS_12 ("BU (12)", 12.0),
    BREAD_UNITS_15 ("BU (15)", 15.0);

    private final String stringRepresentation;
    private final double gramToThisUnitCoefficient;

    CarbsUnit(String stringRepresentation, double gramToThisUnitCoefficient) {
        this.stringRepresentation = stringRepresentation;
        this.gramToThisUnitCoefficient = gramToThisUnitCoefficient;
    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public double convertToGrams(double carbsInUnit) {
        return (carbsInUnit * this.gramToThisUnitCoefficient);
    }

    public double convertFromGrams(double carbsInGrams) {
        return (carbsInGrams / this.gramToThisUnitCoefficient);
    }

    public double getGramToThisUnitCoefficient() {
        return gramToThisUnitCoefficient;
    }
}
