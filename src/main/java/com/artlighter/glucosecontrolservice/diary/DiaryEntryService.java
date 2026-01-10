package com.artlighter.glucosecontrolservice.diary;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiaryEntryService {
   // private Map<DiaryEntryType, DiaryEntryJpaRepository<? extends DiaryEntry>> repositories;
    private CommonDiaryEntryDAO commonDiaryEntryDAO;

    @Autowired
    public DiaryEntryService(CommonDiaryEntryDAO commonDiaryEntryDAO) {
        this.commonDiaryEntryDAO = commonDiaryEntryDAO;
    }

    public DiaryEntry addDiaryEntry(DiaryEntry diaryEntry) {
        //TODO как-то обозначать, что уже существует
        return commonDiaryEntryDAO.save(diaryEntry);
    }

    public List<DiaryEntry> getAllDiaryEntries(PatientProfile patientProfile,
                                                            Instant from, Instant to) {
        return getDiaryEntriesOfType(null, patientProfile, from, to);
    }

    public List<DiaryEntry> getDiaryEntriesOfType(DiaryEntryType entryType, PatientProfile patientProfile,
                                                  Instant from, Instant to) {
        if (patientProfile == null) return Collections.emptyList();

        List<? extends DiaryEntry> entries = commonDiaryEntryDAO.getAllOfTypeBetweenDates(entryType,
                patientProfile, from, to, Sort.by("commitedAt").descending());

        if (entries == null) return Collections.emptyList();

        return entries.stream().map((entry) -> (DiaryEntry) entry)
                .collect(Collectors.toList());
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
