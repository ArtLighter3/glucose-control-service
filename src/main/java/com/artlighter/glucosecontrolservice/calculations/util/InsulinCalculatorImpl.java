package com.artlighter.glucosecontrolservice.calculations.util;

import org.springframework.stereotype.Component;

@Component
public class InsulinCalculatorImpl implements InsulinCalculator {

    @Override
    public float calculateCarbDose(float carbs, float insulinToCarbsRatio) {
        return carbs / insulinToCarbsRatio;
    }

    @Override
    public float calculateCorrectionDose(float currentGlucose, float targetGlucose, float insulinSensitivityFactor,
                                         float activeCompensatingInsulin, float activeCorrectiveInsulin) {
        float activeInsulin = activeCompensatingInsulin + activeCorrectiveInsulin;

        float correction = (currentGlucose - targetGlucose) / insulinSensitivityFactor;

        if (currentGlucose > targetGlucose) {
            if (correction <= activeCompensatingInsulin) correction = -activeCorrectiveInsulin;
            else correction -= activeInsulin;
        } else correction -= activeCorrectiveInsulin;

        return correction;
    }
}
