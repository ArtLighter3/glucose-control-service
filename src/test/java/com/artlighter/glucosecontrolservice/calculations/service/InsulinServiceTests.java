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
@Import({InsulinService.class, BilinearInsulinDecayCurveStrategy.class, InsulinCalculatorImpl.class})
public class InsulinServiceTests {
    @Autowired
    private InsulinService insulinService;

    @Test
    public void calculateInsulinDose_NoInsulinEntriesProvided_ReturnsCorrectResultWithOnlyCarbsInsulinDose() {
        testCarbsDose(10f, 0.7f, 50f, 0f, 5);
        testCarbsDose(10f, 0.7f, 65.4f, 0f, 6.54);
    }

    private void testCarbsDose(float icr, float isf,
                      float carbs, float correction, double expectedCarbsDose) {
        PatientProfile patientProfile = new PatientProfile(0, GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS,
                1, 0, 40f, 5.5f, 0.1f, 0f);
        InsulinProfile insulinProfile = new InsulinProfile(0, icr, isf, 3, null, null);
        InsulinResult expected = new InsulinResult(6f, isf, 0.0, 0.0,
                carbs, icr, expectedCarbsDose, correction, expectedCarbsDose);

        InsulinResult actual = insulinService.calculateInsulinDose(patientProfile, insulinProfile, null,
                null, carbs, 6f, correction);

        assertEquals(expected, actual);
    }
}
