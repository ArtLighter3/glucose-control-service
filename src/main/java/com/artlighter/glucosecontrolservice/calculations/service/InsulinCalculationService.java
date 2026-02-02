package com.artlighter.glucosecontrolservice.calculations.service;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinResult;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinVolatileValue;
import com.artlighter.glucosecontrolservice.calculations.util.calc.InsulinCalculator;
import com.artlighter.glucosecontrolservice.calculations.util.calc.InsulinDecayCurveStrategy;
import com.artlighter.glucosecontrolservice.calculations.util.calc.VolatileValueExtractor;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.InsulinType;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Component
public class InsulinCalculationService {
    private InsulinCalculator insulinCalculator;
    private InsulinDecayCurveStrategy decayCurveStrategy;
    private VolatileValueExtractor volatileValueExtractor;

    private DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
    //private DiaryEntryService diaryEntryService;

    public InsulinCalculationService(InsulinCalculator insulinCalculator,
                                     InsulinDecayCurveStrategy decayCurveStrategy,
                                     VolatileValueExtractor volatileValueExtractor) {
        this.insulinCalculator = insulinCalculator;
        //this.diaryEntryService = diaryEntryService;
        this.decayCurveStrategy = decayCurveStrategy;
        this.volatileValueExtractor = volatileValueExtractor;
    }
//    public InsulinResult calculateInsulinWithoutGlucoseCorrection(PatientProfile patientProfile,
//                                                                  InsulinProfile insulinProfile,
//                                                                  List<InsulinEntry> insulinEntries,
//                                                                  LocalTime timeOfDay,
//                                                                  float carbs, float correction) {
//
//    }

    public InsulinResult calculateInsulinDose(PatientProfile patientProfile, InsulinProfile insulinProfile,
                                              List<? extends DiaryEntry> entriesToConsider, LocalTime timeOfDay,
                                              float carbs, float glucose, float correction) {
        float currentIsf = volatileValueExtractor.extractVolatileValue(insulinProfile.getFactorsByTime(), timeOfDay,
                insulinProfile.getDefaultInsulinSensitivityFactor());
        float currentIcr = volatileValueExtractor.extractVolatileValue(insulinProfile.getRatiosByTime(), timeOfDay,
                insulinProfile.getDefaultInsulinToCarbsRatio());

        double carbsInsulin = insulinCalculator.calculateCarbDose(carbs, currentIcr);
        double correctionInsulin = 0.0;

        double activeInsulin = 0.0;
        if (entriesToConsider != null) {
            List<InsulinEntry> insulinEntries = entriesToConsider.stream()
                    .filter((entry) -> entry instanceof InsulinEntry)
                    .map((entry -> (InsulinEntry) entry)).toList();

            Pair<Double, Double> activeInsulinPair =
                    extractActiveInsulin(insulinEntries, insulinProfile.getDurationOfInsulinAction());
            correctionInsulin = insulinCalculator.calculateCorrectionDose(glucose,
                    patientProfile.getHighGlucose(), currentIsf,
                    activeInsulinPair.getFirst(), activeInsulinPair.getSecond());
            activeInsulin = activeInsulinPair.getFirst() + activeInsulinPair.getSecond();
        }

        double result = carbsInsulin + correctionInsulin;

        return new InsulinResult(glucose,
                currentIsf,
                Float.valueOf(df.format(correctionInsulin)),
                Float.valueOf(df.format(activeInsulin)),
                carbs,
                currentIcr,
                Float.valueOf(df.format(carbsInsulin)),
                correction,
                Float.valueOf(df.format(result)));
    }

    //public float getActive
    private Pair<Double, Double> extractActiveInsulin(List<InsulinEntry> insulinEntries, int durationOfInsulinAction) {
        if (insulinEntries == null) return Pair.of(0.0, 0.0);

        double correctionInsulin = 0.0, carbsInsulin = 0.0;
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
