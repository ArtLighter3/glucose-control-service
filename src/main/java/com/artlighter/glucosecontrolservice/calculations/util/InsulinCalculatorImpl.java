package com.artlighter.glucosecontrolservice.calculations.util;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import org.springframework.stereotype.Component;

@Component
public class InsulinCalculatorImpl implements InsulinCalculator {

    @Override
    public float calculateInsulin(float activeCompensatingInsulin, float activeCorrectiveInsulin,
                                  float currentGlucose, float targetGlucose, float carbs,
                                  float insulinSensitivityFactor, float insulinToCarbsRatio) {
//        if (insulinProfile == null) throw new IllegalArgumentException("insulinProfile cannot be null");
//        if (patientProfile == null) throw new IllegalArgumentException("patientProfile cannot be null");
        float activeInsulin = activeCompensatingInsulin + activeCorrectiveInsulin;
        float correction = (currentGlucose - targetGlucose) / insulinSensitivityFactor;

        if (currentGlucose > targetGlucose) {
            if (correction <= activeCompensatingInsulin) correction = -activeCorrectiveInsulin;
            else correction -= activeInsulin;
        } else correction -= activeCorrectiveInsulin;

        float result = (carbs / insulinToCarbsRatio) + correction;
        return result;
    }
}
