package com.artlighter.glucosecontrolservice.calculations.entity;

import java.util.Objects;

public class InsulinResult {
    private Float glucose;
    private Float insulinSensitivityFactor;
    private Double correctionInsulin;

    private Double activeInsulin;

    private Float carbs;
    private Float insulinToCarbsRatio;
    private Double carbsInsulin;

    private Float correction;

    private Double result;

    public InsulinResult(Float glucose, Float insulinSensitivityFactor, Double correctionInsulin,
                         Double activeInsulin,
                         Float carbs, Float insulinToCarbsRatio, Double carbsInsulin,
                         Float correction,
                         Double result) {
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

    public Double getCorrectionInsulin() {
        return correctionInsulin;
    }

    public Double getActiveInsulin() {
        return activeInsulin;
    }

    public Float getCarbs() {
        return carbs;
    }

    public Float getInsulinToCarbsRatio() {
        return insulinToCarbsRatio;
    }

    public Double getCarbsInsulin() {
        return carbsInsulin;
    }

    public Float getCorrection() {
        return correction;
    }

    public Double getResult() {
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
