package com.artlighter.glucosecontrolservice.calculations.service;

import com.artlighter.glucosecontrolservice.calculations.entity.*;
import com.artlighter.glucosecontrolservice.calculations.util.calc.BilinearInsulinDecayCurveStrategy;
import com.artlighter.glucosecontrolservice.calculations.util.calc.BySortVolatileValueExtractor;
import com.artlighter.glucosecontrolservice.calculations.util.calc.InsulinCalculatorImpl;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.CarbsUnit;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.GlucoseUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import({InsulinCalculationService.class, BilinearInsulinDecayCurveStrategy.class, InsulinCalculatorImpl.class,
         BySortVolatileValueExtractor.class})
public class InsulinCalculationServiceTests {
    @Autowired
    private InsulinCalculationService insulinCalculationService;

    @Test
    public void calculateInsulinDose_NoInsulinEntriesProvidedAndNoDifferentICRs_ReturnsCorrectResultWithOnlyCarbsInsulinDose() {
        testCarbsDose(10f, 50f, 0f, 5);
        testCarbsDose(10f, 65.4f, 0f, 6.54f);
        testCarbsDose(5f, 101f, 0f, 20.2f);
        testCarbsDose(5f, 78.2f, 0f, 15.64f);
        testCarbsDose(7f, 85f, 0f, 12.14f);
        testCarbsDose(7f, 45.55f, 0f, 6.51f);
    }

    @Test
    public void calculateInsulinDose_NoInsulinEntriesAndDifferentICRsButTimeOfDayIsNotAfterAnyOfThem_ReturnsCorrectResultWithOnlyCarbsInsulinDose() {
        testCarbsDose(10f,
                createValuesByTime(List.of(
                                LocalTime.of(15, 30),
                                LocalTime.of(18, 0),
                                LocalTime.of(12, 0)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(11, 0), 50f, 0f, 5, 10f);

        testCarbsDose(5f,
                createValuesByTime(List.of(
                                LocalTime.of(13, 30),
                                LocalTime.of(11, 0),
                                LocalTime.of(16, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(10, 30), 78.2f, 0f, 15.64f, 5f);

        testCarbsDose(7f,
                createValuesByTime(List.of(
                                LocalTime.of(17, 0),
                                LocalTime.of(7, 0),
                                LocalTime.of(8, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(5, 0), 85f, 0f, 12.14f, 7f);
    }

    @Test
    public void calculateInsulinDose_NoInsulinEntriesAndDifferentICRs_ReturnsCorrectResultWithOnlyCarbsInsulinDose() {
        testCarbsDose(10f,
                createValuesByTime(List.of(
                                LocalTime.of(15, 30),
                                LocalTime.of(18, 0),
                                LocalTime.of(12, 0)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(16, 0), 50f, 0f, 3.85f, 13f);

        testCarbsDose(10f,
                createValuesByTime(List.of(
                                LocalTime.of(15, 30),
                                LocalTime.of(18, 0),
                                LocalTime.of(12, 0)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(13, 0), 50f, 0f, 8.33f, 6f);

        testCarbsDose(5f,
                createValuesByTime(List.of(
                                LocalTime.of(13, 30),
                                LocalTime.of(11, 0),
                                LocalTime.of(16, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(17, 30), 78.2f, 0f, 13.03f, 6f);

        testCarbsDose(5f,
                createValuesByTime(List.of(
                                LocalTime.of(13, 30),
                                LocalTime.of(11, 0),
                                LocalTime.of(16, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(13, 0), 78.2f, 0f, 9.77f, 8f);

        testCarbsDose(7f,
                createValuesByTime(List.of(
                                LocalTime.of(17, 0),
                                LocalTime.of(7, 0),
                                LocalTime.of(8, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(8, 30), 85f, 0f, 14.17f, 6f);

        testCarbsDose(7f,
                createValuesByTime(List.of(
                                LocalTime.of(17, 0),
                                LocalTime.of(7, 0),
                                LocalTime.of(8, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(17, 0), 85f, 0f, 6.54f, 13f);
    }

    private void testCarbsDose(float icr, float carbs, float correction, float expectedCarbsDose) {
        PatientProfile patientProfile = new PatientProfile(0, GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS,
                1, 0, 40f, 5.5f, 0.1f, 0f);
        InsulinProfile insulinProfile = new InsulinProfile(0, icr, 1f,
                3, null, null);
        InsulinResult expected = new InsulinResult(6f, 1f, 0.0f, 0.0f,
                carbs, icr, expectedCarbsDose, correction, expectedCarbsDose);

        InsulinResult actual = insulinCalculationService.calculateInsulinDose(insulinProfile,
                null, null, carbs, 6f, correction, patientProfile.getHighGlucose());

        assertEquals(expected, actual);
    }

    private void testCarbsDose(float defaultIcr, List<InsulinToCarbsRatio> ratios, LocalTime timeOfDay,
                               float carbs, float correction, float expectedCarbsDose, float expectedIcr) {
        PatientProfile patientProfile = new PatientProfile(0, GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS,
                1, 0, 40f, 5.5f, 0.1f, 0f);
        InsulinProfile insulinProfile = new InsulinProfile(0, defaultIcr, 1f,
                3, null, ratios);
        InsulinResult expected = new InsulinResult(6f, 1f, 0.0f, 0.0f,
                carbs, expectedIcr, expectedCarbsDose, correction, expectedCarbsDose);

        InsulinResult actual = insulinCalculationService.calculateInsulinDose(insulinProfile,
                null, timeOfDay, carbs, 6f, correction, patientProfile.getHighGlucose());

        assertEquals(expected, actual);
    }

    private <T extends InsulinVolatileValue> List<T> createValuesByTime(List<LocalTime> times,
                                                                        List<Float> values,
                                                                        Class<T> type) {
        if (times == null || values == null || times.size() != values.size())
            return Collections.emptyList();

        List<T> result = new ArrayList<>(times.size());
        for (int i = 0; i < times.size(); i++) {
            try {
                result.add(type.getDeclaredConstructor(float.class, LocalTime.class, InsulinProfile.class)
                        .newInstance(values.get(i), times.get(i), null));
            } catch (Exception ignored) {}
        }

        return result;
    }
}
