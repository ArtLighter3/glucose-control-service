package com.artlighter.glucosecontrolservice.statistics.service;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.statistics.dto.GlucoseDistributionDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс, обрабатывающий входные данные для получения различной статистики и аналитики по ним.
 */

@Service
public class StatisticsHandler {

    /**
     * Вычислить распределение уровней глюкозы по целевым уровням, заданным пользователем.
     * @param patientProfile профиль больного с его целевыми уровнями;
     * @param entries записи дневника, из которых будет собираться статистика; принимается список записей любого типа,
     *                но учитываются только записи о глюкозе (GlucoseEntry);
     * @return объект с информацией о распределении уровней глюкозы;
     * @throws IllegalArgumentException если patientProfile или entries являются null;
     */
    public GlucoseDistributionDTO getGlucoseLevelsDistribution(PatientProfile patientProfile,
                                                               List<DiaryEntry> entries) {
        if (patientProfile == null) throw new IllegalArgumentException("patientProfile cannot be null");
        if (entries == null) throw new IllegalArgumentException("entries cannot be null");

        double hyperLevel = patientProfile.getHyperGlucose();
        double highLevel = patientProfile.getHighGlucose();
        double lowLevel = patientProfile.getLowGlucose();
        double hypoLevel = patientProfile.getHypoGlucose();

        int totalMeasurements = 0;
        //в массиве хранится количество измерений для вычисляемых диапазонов, начиная от диапазона (+беск., ур.гипер]
        int[] measurementsInRange = new int[5];
        for (DiaryEntry entry : entries) {
            if (entry instanceof GlucoseEntry glucoseEntry) {
                int rangeIndex = getRangeIndex(glucoseEntry.getValue(), hyperLevel, highLevel, lowLevel, hypoLevel);
                if (rangeIndex >= 0 && rangeIndex < measurementsInRange.length) {
                    measurementsInRange[rangeIndex]++;
                    totalMeasurements++;
                }
            }
        }

        return new GlucoseDistributionDTO(
                (double) measurementsInRange[0] / totalMeasurements,
                (double) measurementsInRange[1] / totalMeasurements,
                (double) measurementsInRange[2] / totalMeasurements,
                (double) measurementsInRange[3] / totalMeasurements,
                (double) measurementsInRange[4] / totalMeasurements
        );
    }

    private int getRangeIndex(double value, double hyperLevel, double highLevel, double lowLevel, double hypoLevel) {
        if (value >= hyperLevel) return 0;
        else if (value >= highLevel) return 1;
        else if (value > lowLevel) return 2;
        else if (value > hypoLevel) return 3;
        else return 4;
    }

}
