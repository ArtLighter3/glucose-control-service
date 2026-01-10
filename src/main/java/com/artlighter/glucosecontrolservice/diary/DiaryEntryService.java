package com.artlighter.glucosecontrolservice.diary;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiaryEntryService {
   // private Map<DiaryEntryType, DiaryEntryJpaRepository<? extends DiaryEntry>> repositories;
    private CommonDiaryEntryDAO commonDiaryEntryDAO;

    @Autowired
    public DiaryEntryService(CommonDiaryEntryDAO commonDiaryEntryDAO) {
        this.commonDiaryEntryDAO = commonDiaryEntryDAO;
    }

    public DiaryEntry addDiaryEntry(DiaryEntry diaryEntry) {
        return commonDiaryEntryDAO.save(diaryEntry);
    }

    public List<DiaryEntry> getAllPatientEntries(PatientProfile patientProfile) {
        if (patientProfile == null) return Collections.emptyList();

        List<DiaryEntry> measurements =
                commonDiaryEntryDAO.getAllByPatientProfileOrderByCommitedAtDesc(patientProfile);

        if (measurements == null) return Collections.emptyList();
        return measurements;
    }

//    public List<DiaryEntry> getUserMeasurementsFromPeriod(UserDTO user, Date from, Date to) {
//        List<DiaryEntry> measurements = getAllUserMeasurements(user);
//
//        return measurements.stream()
//                .filter((entry) -> entry.getDate().after(from) && entry.getDate().before(to))
//                .toList();
//    }

//    public List<DiaryEntry> getAllMeasurements() {
//        return diaryEntryRepository.getAllMeasurements();
//    }

//    private enum DiaryEntryType {
//        GLUCOSE_ENTRY(GlucoseEntryRepository.class),
//        INSULIN_ENTRY(InsulinEntryRepository.class),
//        MEAL_ENTRY(MealEntryRepository.class),
//        MEDICATION_ENTRY(MedicationEntryRepository.class);
//
//        private Class<? extends DiaryEntryRepository> repositoryClass;
//
//        DiaryEntryType(Class<? extends DiaryEntryRepository> repositoryClass) {
//            this.repositoryClass = repositoryClass;
//        }
//
//        Class<? extends DiaryEntryRepository> getRepositoryClass() {
//            return repositoryClass;
//        }
//    }
}
