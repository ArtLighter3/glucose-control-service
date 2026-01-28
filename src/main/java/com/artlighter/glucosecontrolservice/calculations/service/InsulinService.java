package com.artlighter.glucosecontrolservice.calculations.service;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinResult;
import com.artlighter.glucosecontrolservice.calculations.util.calc.InsulinCalculator;
import com.artlighter.glucosecontrolservice.calculations.util.calc.InsulinDecayCurveStrategy;
import com.artlighter.glucosecontrolservice.diary.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.InsulinType;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class InsulinService {
    private InsulinCalculator insulinCalculator;
    private InsulinDecayCurveStrategy decayCurveStrategy;
    //private DiaryEntryService diaryEntryService;

    public InsulinService(InsulinCalculator insulinCalculator, InsulinDecayCurveStrategy decayCurveStrategy) {
        this.insulinCalculator = insulinCalculator;
        //this.diaryEntryService = diaryEntryService;
        this.decayCurveStrategy = decayCurveStrategy;
    }

    public InsulinResult calculateInsulinDose(PatientProfile patientProfile, InsulinProfile insulinProfile,
                                              List<InsulinEntry> insulinEntries, LocalTime timeOfDay,
                                              float carbs, float glucose, float correction) {
        float currentIsf = insulinProfile.getDefaultInsulinSensitivityFactor();
        float currentIcr = insulinProfile.getDefaultInsulinToCarbsRatio();

        double carbsInsulin = insulinCalculator.calculateCarbDose(carbs, currentIcr);
        double correctionInsulin = 0.0;

        double activeInsulin = 0.0;
        if (insulinEntries != null) {
            Pair<Double, Double> activeInsulinPair =
                    extractActiveInsulin(insulinEntries, insulinProfile.getDurationOfInsulinAction());
            correctionInsulin = insulinCalculator.calculateCorrectionDose(glucose,
                    patientProfile.getHighGlucose(), currentIsf,
                    activeInsulinPair.getFirst(), activeInsulinPair.getSecond());
            activeInsulin = activeInsulinPair.getFirst() + activeInsulinPair.getSecond();
        }

        double result = carbsInsulin + correctionInsulin;

        return new InsulinResult(glucose, currentIsf, correctionInsulin, activeInsulin, carbs, currentIcr,
                carbsInsulin, correction, result);
    }

    //public float getActive
    private Pair<Double, Double> extractActiveInsulin(List<InsulinEntry> insulinEntries, int durationOfInsulinAction) {
        if (insulinEntries == null) return Pair.of(0.0, 0.0);

        double correctionInsulin = 0f, carbsInsulin = 0f;
        Instant now = Instant.now();
        //Map<Instant, Float> insulinByTimestamp = new TreeMap<>();

        for (InsulinEntry insulinEntry : insulinEntries) {
            if (insulinEntry.getInsulinType() == InsulinType.LONG)
                continue;

            Instant commitedAt = insulinEntry.getCommitedAt();
            if (commitedAt.isBefore(now.minus(Duration.ofHours(durationOfInsulinAction))))
                continue;

            int minutesPassedFromAdministration = (int)
                (now.minusMillis(commitedAt.toEpochMilli()).toEpochMilli() / 1000L / 60L);
            double activeInsulin = decayCurveStrategy.getCurrentActiveInsulin(insulinEntry.getValue(),
                    minutesPassedFromAdministration, durationOfInsulinAction);

            if (insulinEntry.getInsulinType() == InsulinType.SHORT_CARBS) carbsInsulin += activeInsulin;
            else if (insulinEntry.getInsulinType() == InsulinType.SHORT_CORRECTION) correctionInsulin += activeInsulin;
        }

        return Pair.of(carbsInsulin, correctionInsulin);
    }

}
