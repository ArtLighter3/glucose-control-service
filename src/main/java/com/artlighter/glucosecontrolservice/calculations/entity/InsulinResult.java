package com.artlighter.glucosecontrolservice.calculations.entity;

import java.util.Objects;

public class InsulinResult {
    private Float glucose;
    private Float insulinSensitivityFactor;
    private Float correctionInsulin;

    private Float activeInsulin;

    private Float carbs;
    private Float insulinToCarbsRatio;
    private Float carbsInsulin;

    private Float correction;

    private Float result;

    public InsulinResult(Float glucose, Float insulinSensitivityFactor, Float correctionInsulin,
                         Float activeInsulin,
                         Float carbs, Float insulinToCarbsRatio, Float carbsInsulin,
                         Float correction,
                         Float result) {
        this.glucose = glucose;
        this.insulinSensitivityFactor = insulinSensitivityFactor;
        this.correctionInsulin = correctionInsulin;
        this.activeInsulin = activeInsulin;
        this.carbs = carbs;
        this.insulinToCarbsRatio = insulinToCarbsRatio;
        this.carbsInsulin = carbsInsulin;
        this.correction = correction;
        this.result = result;
    }

    public Float getGlucose() {
        return glucose;
    }

    public Float getInsulinSensitivityFactor() {
        return insulinSensitivityFactor;
    }

    public Float getCorrectionInsulin() {
        return correctionInsulin;
    }

    public Float getActiveInsulin() {
        return activeInsulin;
    }

    public Float getCarbs() {
        return carbs;
    }

    public Float getInsulinToCarbsRatio() {
        return insulinToCarbsRatio;
    }

    public Float getCarbsInsulin() {
        return carbsInsulin;
    }

    public Float getCorrection() {
        return correction;
    }

    public Float getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InsulinResult that = (InsulinResult) o;
        return Objects.equals(glucose, that.glucose) &&
                Objects.equals(insulinSensitivityFactor, that.insulinSensitivityFactor) &&
                Objects.equals(correctionInsulin, that.correctionInsulin) &&
                Objects.equals(activeInsulin, that.activeInsulin) &&
                Objects.equals(carbs, that.carbs) &&
                Objects.equals(insulinToCarbsRatio, that.insulinToCarbsRatio) &&
                Objects.equals(carbsInsulin, that.carbsInsulin) &&
                Objects.equals(correction, that.correction) &&
                Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(glucose,
                insulinSensitivityFactor,
                correctionInsulin,
                activeInsulin,
                carbs,
                insulinToCarbsRatio,
                carbsInsulin,
                correction,
                result);
    }
}
