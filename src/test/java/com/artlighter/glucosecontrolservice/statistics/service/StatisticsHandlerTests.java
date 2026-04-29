package com.artlighter.glucosecontrolservice.statistics.service;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.statistics.dto.GlucoseDistributionDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Import(StatisticsHandler.class)
public class StatisticsHandlerTests {
    @Autowired
    private StatisticsHandler statisticsHandler;

    @Test
    public void getGlucoseLevelsDistribution_ProfileOrEntriesAreNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            statisticsHandler.getGlucoseLevelsDistribution(null, List.of(new GlucoseEntry()));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            statisticsHandler.getGlucoseLevelsDistribution(new PatientProfile(), null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            statisticsHandler.getGlucoseLevelsDistribution(null, null);
        });
    }

    @Test
    public void getGlucoseLevelsDistribution_ReturnsCorrectDistribution() {
        testCorrectDistribution(new GlucoseDistributionDTO(
                0, 0.5, 0.5, 0, 0),
                15, 10, 5, 2,
                6.5, 9.8, 14.11, 10);
        testCorrectDistribution(new GlucoseDistributionDTO(
                0.2, 0.2, 0.2, 0.2, 0.2),
                15, 10, 5, 2,
                6.5, 9.8, 14.11, 10, 5.65, 7.9, 14, 12.25, 5, 4.5, 3, 2.7, 1.2, 1.5, 1, 1.9, 30, 25, 20, 15);
        testCorrectDistribution(new GlucoseDistributionDTO(
                        0.45, 0, 0.1, 0.45, 0),
                15, 10, 5, 2,
                16, 15, 17, 18.5, 19, 15.1, 16.4, 16, 17, 4.5, 3, 2.7, 3, 4.5, 2.5, 2.9, 3, 3.3, 9, 6.5);
        testCorrectDistribution(new GlucoseDistributionDTO(
                        0, 0.2, 0.4, 0.4, 0),
                15, 10, 5, 2,
                6.5, 9.8, 5.65, 7.9, 3.5, 3, 2.3, 5, 10, 13.25);
    }

    private void testCorrectDistribution(GlucoseDistributionDTO expected,
                                         double hyperGlucose,
                                         double highGlucose,
                                         double lowGlucose,
                                         double hypoGlucose,
                                         double... measurements) {
        PatientProfile patientProfile = createPatientProfile(hyperGlucose, highGlucose, lowGlucose, hypoGlucose);
        List<DiaryEntry> entries = createMeasurements(measurements);

        GlucoseDistributionDTO actual = statisticsHandler.getGlucoseLevelsDistribution(patientProfile, entries);
        assertEquals(1, actual.aboveHighGlucose() + actual.aboveHyperGlucose() +
                actual.belowLowGlucose() + actual.belowHypoGlucose() + actual.inTargetRange());
        assertEquals(expected, actual);
    }

    private PatientProfile createPatientProfile(double hyperGlucose,
                                                double highGlucose,
                                                double lowGlucose,
                                                double hypoGlucose) {
        PatientProfile patientProfile = new PatientProfile();

        patientProfile.setHyperGlucose((float) hyperGlucose);
        patientProfile.setHighGlucose((float) highGlucose);
        patientProfile.setLowGlucose((float) lowGlucose);
        patientProfile.setHypoGlucose((float) hypoGlucose);

        return patientProfile;
    }

    private List<DiaryEntry> createMeasurements(double... values) {
        List<DiaryEntry> entries = new ArrayList<>();

        for (double value : values) {
            GlucoseEntry glucoseEntry = new GlucoseEntry();
            glucoseEntry.setValue(value);
            entries.add(glucoseEntry);
        }

        return entries;
    }
}
