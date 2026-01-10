package com.artlighter.glucosecontrolservice.diary.repository.impl;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryRepositoryCollection;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DelegatingCommonDiaryEntryCollector implements CommonDiaryEntryDAO {
    private DiaryEntryRepositoryCollection repositories;

   // private EntityManager entityManager;

//    private GlucoseEntryRepository glucoseEntryRepository;
//    private InsulinEntryRepository insulinEntryRepository;
//    private MealEntryRepository mealEntryRepository;
//    private MedicationEntryRepository medicationEntryRepository;

    public DelegatingCommonDiaryEntryCollector(DiaryEntryRepositoryCollection repositories) {
        this.repositories = repositories;
    }

    @Override
    public List<DiaryEntry> getAllByPatientProfileOrderByCommitedAtDesc(PatientProfile patientProfile) {
        List<DiaryEntry> diaryEntries = new ArrayList<>();

        for (ParticularDiaryEntryRepository repository : repositories.getAllRepositories()) {
            diaryEntries.addAll(repository.getAllByPatientProfileOrderByCommitedAtDesc(patientProfile));
        }

        diaryEntries.sort((entry1, entry2) ->
                entry2.getCommitedAt().compareTo(entry1.getCommitedAt()));
        return diaryEntries;
    }

    @Override
    public DiaryEntry save(DiaryEntry entry) {
        return (DiaryEntry) repositories.getRepositoryForEntity(entry).save(entry);
    }
}
