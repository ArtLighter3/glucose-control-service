package com.artlighter.glucosecontrolservice.calculations.util.calc;

import org.springframework.stereotype.Component;

@Component
public class InsulinCalculatorImpl implements InsulinCalculator {

    @Override
    public double calculateCarbDose(float carbs, float insulinToCarbsRatio) {
        return carbs / (double) insulinToCarbsRatio;
    }

    @Override
    public double calculateCorrectionDose(float currentGlucose, float targetGlucose, float insulinSensitivityFactor,
                                         double activeCompensatingInsulin, double activeCorrectiveInsulin) {
        double activeInsulin = activeCompensatingInsulin + activeCorrectiveInsulin;

        double correction = (currentGlucose - targetGlucose) / insulinSensitivityFactor;

        if (currentGlucose > targetGlucose) {
            if (correction <= activeCompensatingInsulin) correction = -activeCorrectiveInsulin;
            else correction -= activeInsulin;
        } else correction -= activeCorrectiveInsulin;

        return correction;
    }
}
