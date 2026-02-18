package com.artlighter.glucosecontrolservice.diary.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@Transactional
public class DiaryEntryService {
    private CommonDiaryEntryDAO commonDiaryEntryDAO;

    @Autowired
    public DiaryEntryService(CommonDiaryEntryDAO commonDiaryEntryDAO) {
        this.commonDiaryEntryDAO = commonDiaryEntryDAO;
    }

    public DiaryEntry addDiaryEntry(DiaryEntry diaryEntry, PatientProfile patientProfile, Instant commitedAt) {
        fill(diaryEntry, patientProfile, commitedAt);

        if (commonDiaryEntryDAO.exists(diaryEntry))
            throw new ResourceAlreadyExistsException(diaryEntry,
                    "Diary entry for this user and this timestamp already exists");

        return commonDiaryEntryDAO.saveOrUpdate(diaryEntry);
    }

    public List<DiaryEntry> addDiaryEntries(List<DiaryEntry> entries, PatientProfile patientProfile,
                                            boolean updateIfExists) {
        List<DiaryEntry> savedEntries = new ArrayList<>();

        for (DiaryEntry entry : entries) {
            entry.setPatientProfile(patientProfile);

            try {
                if (updateIfExists || !commonDiaryEntryDAO.exists(entry)) {
                    commonDiaryEntryDAO.saveOrUpdate(entry);
                    savedEntries.add(entry);
                }
            } catch (Exception ignored) {}
        }

        return savedEntries;
    }

    public DiaryEntry updateDiaryEntry(DiaryEntry diaryEntry, PatientProfile patientProfile, Instant commitedAt) {
        fill(diaryEntry, patientProfile, commitedAt);

        if (!commonDiaryEntryDAO.exists(diaryEntry))
            throw new ResourceNotFoundException("Diary entry for this user and this timestamp not found");

        return commonDiaryEntryDAO.saveOrUpdate(diaryEntry);
    }

    public void deleteDiaryEntry(DiaryEntry diaryEntry, PatientProfile patientProfile, Instant commitedAt) {
        fill(diaryEntry, patientProfile, commitedAt);

        commonDiaryEntryDAO.remove(diaryEntry);
    }

    @Transactional(readOnly = true)
    public List<DiaryEntry> getAllDiaryEntries(PatientProfile patientProfile,
                                                            Instant from, Instant to) {
        return getDiaryEntriesOfType(null, patientProfile, from, to);
    }

    @Transactional(readOnly = true)
    public List<DiaryEntry> getDiaryEntriesOfType(DiaryEntryType entryType, PatientProfile patientProfile,
                                                  Instant from, Instant to) {
        if (patientProfile == null) return Collections.emptyList();
        if (to == null) to = Instant.now();
        if (from == null) from = to.minus(Duration.ofDays(30));

        List<DiaryEntry> entries = commonDiaryEntryDAO.getAllOfTypeBetweenDates(entryType,
                patientProfile, from, to, Sort.by("commitedAt").descending());

        if (entries == null) return Collections.emptyList();

        return entries;
//        return entries.stream().map((entry) -> (DiaryEntry) entry)
//                .collect(Collectors.toList());
    }

    private void fill(DiaryEntry diaryEntry, PatientProfile patientProfile, Instant commitedAt) {
        if (diaryEntry != null) {
            diaryEntry.setPatientProfile(patientProfile);
            diaryEntry.setCommitedAt(commitedAt);
        }
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
