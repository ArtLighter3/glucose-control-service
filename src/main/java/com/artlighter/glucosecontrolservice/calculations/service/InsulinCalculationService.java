package com.artlighter.glucosecontrolservice.calculations.service;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinResult;
import com.artlighter.glucosecontrolservice.calculations.util.calc.InsulinCalculator;
import com.artlighter.glucosecontrolservice.calculations.util.calc.InsulinDecayCurveStrategy;
import com.artlighter.glucosecontrolservice.calculations.util.calc.VolatileValueExtractor;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.InsulinType;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@Service
public class InsulinCalculationService {
    private InsulinCalculator insulinCalculator;
    private InsulinDecayCurveStrategy decayCurveStrategy;
    private VolatileValueExtractor volatileValueExtractor;
    private PatientProfileService patientProfileService;
    private InsulinProfileService insulinProfileService;
    private DiaryEntryService diaryEntryService;

    private DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
    //private DiaryEntryService diaryEntryService;

    @Autowired
    public InsulinCalculationService(InsulinCalculator insulinCalculator,
                                     InsulinDecayCurveStrategy decayCurveStrategy,
                                     VolatileValueExtractor volatileValueExtractor,
                                     PatientProfileService patientProfileService,
                                     InsulinProfileService insulinProfileService,
                                     DiaryEntryService diaryEntryService) {
        this.insulinCalculator = insulinCalculator;
        this.diaryEntryService = diaryEntryService;
        this.decayCurveStrategy = decayCurveStrategy;
        this.volatileValueExtractor = volatileValueExtractor;
        this.patientProfileService = patientProfileService;
        this.insulinProfileService = insulinProfileService;
    }
//    public InsulinResult calculateInsulinWithoutGlucoseCorrection(PatientProfile patientProfile,
//                                                                  InsulinProfile insulinProfile,
//                                                                  List<InsulinEntry> insulinEntries,
//                                                                  LocalTime timeOfDay,
//                                                                  float carbs, float correction) {
//
//    }

    /**
     * Высчитывает дозу инсулина для компенсации принятых углеводов и/или снижения текущей глюкозы glucose до
     * целевого значения targetGlucose c учетом активного инсулина.
     * Активный инсулин определяется на основе последних записей дневника.
     * Расчеты ведутся в соответствии с настройками в инсулиновом профиле.
     * @param patientId ID пользователя-больного;
     * @param timeOfDay текущее время дня у больного (точное время дня, как у него, БЕЗ учета смещений и зон!), не null;
     * @param considerActiveInsulin учитывать ли активный инсулин (будет рассчитываться исходя
     *                              из недавних записей ввода инсулина);
     * @param correctGlucoseLevel рассчитывать ли дополнительную дозу для корректировки уровня глюкозы
     *                            до целевого значения (целевое значение определяется как уровень высокой глюкозы по
     *                            настройкам в профиле больного);
     * @param carbs количество принимаемых углеводов, для которых производится расчет компенсации;
     * @param glucose текущий уровень глюкозы для коррекции сахара в крови;
     * @param correction процент коррекции, если необходимо добавить из-за каких-то внешних факторов;
     * @return InsulinResult, содержащий как результат, так и каждый элемент, участвовавший в расчете;
     * @throws IllegalArgumentException если insulinProfile или timeOfDay равны null;
     */
    public InsulinResult calculateInsulinDose(int patientId, LocalTime timeOfDay,
                                              boolean considerActiveInsulin, boolean correctGlucoseLevel,
                                              float carbs, float glucose, float correction) {
        if (timeOfDay == null) throw new IllegalArgumentException("timeOfDay cannot be null");

        PatientProfile patientProfile = patientProfileService.getByUserId(patientId);
        InsulinProfile insulinProfile = insulinProfileService.getByPatientProfileId(patientProfile.getId());

        Instant now = Instant.now();
        List<DiaryEntry> entriesToConsider = considerActiveInsulin ?
                diaryEntryService.getDiaryEntriesOfType(DiaryEntryType.INSULIN_ENTRY,
                        patientProfile, now.minus(Duration.ofHours(12)), now) : null;

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
                    extractActiveInsulin(insulinEntries, insulinProfile.getDurationOfInsulinAction(), Instant.now());
            correctionInsulin = insulinCalculator.calculateCorrectionDose(glucose,
                    patientProfile.getHighGlucose(), currentIsf,
                    activeInsulinPair.getFirst(), activeInsulinPair.getSecond());
            activeInsulin = activeInsulinPair.getFirst() + activeInsulinPair.getSecond();
        }

        double result = carbsInsulin + correctionInsulin;

        //TODO лучше округлять уже в слое выше при конвертации в DTO
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

    public Float calculateActiveInsulin(int patientId, Instant patientTimestamp) {
        if (patientTimestamp == null) patientTimestamp = Instant.now();

        PatientProfile patientProfile = patientProfileService.getByUserId(patientId);
        InsulinProfile insulinProfile = insulinProfileService.getByPatientProfileId(patientProfile.getId());

        List<DiaryEntry> diaryEntries = diaryEntryService.getDiaryEntriesOfType(DiaryEntryType.INSULIN_ENTRY,
                patientProfile, patientTimestamp.minus(Duration.ofHours(12)), patientTimestamp);
        List<InsulinEntry> insulinEntries = diaryEntries.stream()
                .filter((entry) -> entry instanceof InsulinEntry)
                .map((entry -> (InsulinEntry) entry)).toList();

        Pair<Double, Double> activeInsulin =
                extractActiveInsulin(insulinEntries, insulinProfile.getDurationOfInsulinAction(), patientTimestamp);
        return (float) (activeInsulin.getFirst() + activeInsulin.getSecond());
    }

    //public float getActive
    private Pair<Double, Double> extractActiveInsulin(List<InsulinEntry> insulinEntries, int durationOfInsulinAction,
                                                      Instant timestamp) {
        if (insulinEntries == null || insulinEntries.isEmpty()) return Pair.of(0.0, 0.0);

        double correctionInsulin = 0.0, carbsInsulin = 0.0;
        //Map<Instant, Float> insulinByTimestamp = new TreeMap<>();

        for (InsulinEntry insulinEntry : insulinEntries) {
            if (insulinEntry.getInsulinType() == InsulinType.LONG)
                continue;

            Instant commitedAt = insulinEntry.getCommitedAt();
            if (commitedAt.isBefore(timestamp.minus(Duration.ofHours(durationOfInsulinAction))))
                continue;

            int minutesPassedFromAdministration = (int)
                (timestamp.minusMillis(commitedAt.toEpochMilli()).toEpochMilli() / 1000L / 60L);
            double activeInsulin = decayCurveStrategy.getCurrentActiveInsulin(insulinEntry.getValue(),
                    minutesPassedFromAdministration, durationOfInsulinAction);

            if (insulinEntry.getInsulinType() == InsulinType.SHORT_CARBS) carbsInsulin += activeInsulin;
            else if (insulinEntry.getInsulinType() == InsulinType.SHORT_CORRECTION) correctionInsulin += activeInsulin;
        }

        return Pair.of(carbsInsulin, correctionInsulin);
    }

}
