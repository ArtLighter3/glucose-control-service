package com.artlighter.glucosecontrolservice.calculations.service;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinResult;
import com.artlighter.glucosecontrolservice.calculations.util.calc.BilinearInsulinDecayCurveStrategy;
import com.artlighter.glucosecontrolservice.calculations.util.calc.InsulinCalculatorImpl;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.CarbsUnit;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.GlucoseUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import({InsulinCalculationService.class, BilinearInsulinDecayCurveStrategy.class, InsulinCalculatorImpl.class})
public class InsulinCalculationServiceTests {
    @Autowired
    private InsulinCalculationService insulinCalculationService;

    @Test
    public void calculateInsulinDose_NoInsulinEntriesProvided_ReturnsCorrectResultWithOnlyCarbsInsulinDose() {
        testCarbsDose(10f, 50f, 0f, 5);
        testCarbsDose(10f, 65.4f, 0f, 6.54f);
        testCarbsDose(5f, 101f, 0f, 20.2f);
        testCarbsDose(5f, 78.2f, 0f, 15.64f);
        testCarbsDose(7f, 85f, 0f, 12.14f);
        testCarbsDose(7f, 45.55f, 0f, 6.51f);
    }

    private void testCarbsDose(float icr, float carbs, float correction, float expectedCarbsDose) {
        PatientProfile patientProfile = new PatientProfile(0, GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS,
                1, 0, 40f, 5.5f, 0.1f, 0f);
        InsulinProfile insulinProfile = new InsulinProfile(0, icr, 1f, 3, null, null);
        InsulinResult expected = new InsulinResult(6f, 1f, 0.0f, 0.0f,
                carbs, icr, expectedCarbsDose, correction, expectedCarbsDose);

        InsulinResult actual = insulinCalculationService.calculateInsulinDose(patientProfile, insulinProfile, null,
                null, carbs, 6f, correction);

        assertEquals(expected, actual);
    }
}
