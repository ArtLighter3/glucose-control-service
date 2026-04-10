package com.artlighter.glucosecontrolservice.calculations.dto;

import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;
import com.artlighter.glucosecontrolservice.user.entity.GlucoseUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "Результат расчета инсулина")
public class InsulinResult {
    @Schema(description = "Текущий уровень глюкозы, использовавшийся в расчетах (в единицах измерения, " +
            "выставленных пользователем и указанных в glucoseUnit)")
    private Float glucose;
    @Schema(description = "Единицы измерения глюкозы в этом объекте")
    private GlucoseUnit glucoseUnit;
    @Schema(description = "Фактор чувствительности к инсулину (ISF), использовавшийся в расчетах")
    private Float insulinSensitivityFactor;
    @Schema(description = "Инсулин, направленный на корректировку значения глюкозы glucose " +
            "до целевых значений (в единицах) и используемый в общей дозировке")
    private Float correctionInsulin;
    @Schema(description = "Активный инсулин, рассчитанный на основе недавних записей ввода инсулина и используемый в" +
            " общей дозировке")
    private Float activeInsulin;
    @Schema(description = "Количество принимаемых углеводов, использовавшееся в расчетах " +
            "(в единицах измерения, выставленных пользователем и указанных в carbsUnit)")
    private Float carbs;
    @Schema(description = "Единицы измерения углеводов в этом объекте")
    private CarbsUnit carbsUnit;
    @Schema(description = "Соотношение единицы инсулина к углеводам (ICR), использовавшееся в расчетах")
    private Float insulinToCarbsRatio;
    @Schema(description = "Инсулин, направленный на компенсацию принимаемых углеводов carbs (в единицах) и" +
            " используемый в общей дозировке")
    private Float carbsInsulin;
    @Schema(description = "Значение корректировки, использовавшееся в расчетах")
    private Float correction;
    @Schema(description = "Итоговая дозировка инсулина (в единицах)")
    private Float result;

    public InsulinResult(Float glucose, GlucoseUnit glucoseUnit, Float insulinSensitivityFactor,
                         Float correctionInsulin, Float activeInsulin,
                         Float carbs, CarbsUnit carbsUnit,
                         Float insulinToCarbsRatio, Float carbsInsulin,
                         Float correction,
                         Float result) {
        this.glucose = glucose;
        this.glucoseUnit = glucoseUnit;
        this.insulinSensitivityFactor = insulinSensitivityFactor;
        this.correctionInsulin = correctionInsulin;
        this.activeInsulin = activeInsulin;
        this.carbs = carbs;
        this.carbsUnit = carbsUnit;
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

    public GlucoseUnit getGlucoseUnit() {
        return glucoseUnit;
    }

    public CarbsUnit getCarbsUnit() {
        return carbsUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InsulinResult that = (InsulinResult) o;
        return Objects.equals(glucose, that.glucose) &&
                glucoseUnit == that.glucoseUnit &&
                Objects.equals(insulinSensitivityFactor, that.insulinSensitivityFactor) &&
                Objects.equals(correctionInsulin, that.correctionInsulin) &&
                Objects.equals(activeInsulin, that.activeInsulin) &&
                Objects.equals(carbs, that.carbs) && carbsUnit == that.carbsUnit &&
                Objects.equals(insulinToCarbsRatio, that.insulinToCarbsRatio) &&
                Objects.equals(carbsInsulin, that.carbsInsulin) &&
                Objects.equals(correction, that.correction) &&
                Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(glucose,
                glucoseUnit,
                insulinSensitivityFactor,
                correctionInsulin,
                activeInsulin,
                carbs,
                carbsUnit,
                insulinToCarbsRatio,
                carbsInsulin,
                correction,
                result);
    }

    @Override
    public String toString() {
        return "InsulinResult{" +
                "glucose=" + glucose +
                ", glucoseUnit=" + glucoseUnit +
                ", insulinSensitivityFactor=" + insulinSensitivityFactor +
                ", correctionInsulin=" + correctionInsulin +
                ", activeInsulin=" + activeInsulin +
                ", carbs=" + carbs +
                ", carbsUnit=" + carbsUnit +
                ", insulinToCarbsRatio=" + insulinToCarbsRatio +
                ", carbsInsulin=" + carbsInsulin +
                ", correction=" + correction +
                ", result=" + result +
                '}';
    }
}
