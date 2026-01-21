package com.artlighter.glucosecontrolservice.calculations.util;

/**
 * Общий интерфейс для калькуляторов инсулиновых доз, вычисляющих результат только из необходимых числовых данных
 * (без классов профилей и т.д.)
 */
public interface InsulinCalculator {
    float calculateCarbDose(float carbs, float insulinToCarbsRatio);

    float calculateCorrectionDose(float currentGlucose, float targetGlucose, float insulinSensitivityFactor,
                                  float activeCompensatingInsulin, float activeCorrectiveInsulin);
}
