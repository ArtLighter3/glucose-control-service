package com.artlighter.glucosecontrolservice.calculations.util.calc;

/**
 * Общий интерфейс для калькуляторов инсулиновых доз, вычисляющих результат только из необходимых числовых данных
 * (без классов профилей и т.д.)
 */
public interface InsulinCalculator {
    double calculateCarbDose(float carbs, float insulinToCarbsRatio);

    double calculateCorrectionDose(float currentGlucose, float targetGlucose, float insulinSensitivityFactor,
                                  double activeCompensatingInsulin, double activeCorrectiveInsulin);
}
