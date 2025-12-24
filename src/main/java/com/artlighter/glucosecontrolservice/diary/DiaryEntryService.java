package com.artlighter.glucosecontrolservice.diary;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.DiaryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DiaryEntryService {
    private DiaryEntryRepository diaryEntryRepository;

    @Autowired
    public DiaryEntryService(DiaryEntryRepository diaryEntryRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
    }

    public DiaryEntry saveMeasurement(DiaryEntry diaryEntry) {
        return diaryEntryRepository.save(diaryEntry);
    }

    public List<DiaryEntry> getAllPatientEntries(PatientProfile patientProfile) {
        if (patientProfile == null) return Collections.emptyList();

        List<DiaryEntry> measurements =
                diaryEntryRepository.getDiaryEntriesByPatientProfileOrderByCommitedAtDesc(patientProfile);

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
}
