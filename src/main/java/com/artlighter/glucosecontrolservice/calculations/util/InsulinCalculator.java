package com.artlighter.glucosecontrolservice.calculations.util;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;

public interface InsulinCalculator {
    float calculateInsulin(float activeCompensatingInsulin, float activeCorrectiveInsulin,
                           float currentGlucose, float targetGlucose, float carbs,
                           float insulinSensitivityFactor, float insulinToCarbsRatio);
}
