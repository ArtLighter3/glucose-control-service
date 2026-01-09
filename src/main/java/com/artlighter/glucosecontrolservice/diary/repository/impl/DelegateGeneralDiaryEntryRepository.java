package com.artlighter.glucosecontrolservice.diary.repository.impl;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.ArrayList;
import java.util.List;

public class DelegateGeneralDiaryEntryRepository implements DiaryEntryRepository {
    private DiaryEntryRepositoryCollection repositories;

   // private EntityManager entityManager;

//    private GlucoseEntryRepository glucoseEntryRepository;
//    private InsulinEntryRepository insulinEntryRepository;
//    private MealEntryRepository mealEntryRepository;
//    private MedicationEntryRepository medicationEntryRepository;

    public DelegateGeneralDiaryEntryRepository(DiaryEntryRepositoryCollection repositories) {
        this.repositories = repositories;
    }

    @Override
    public List<? extends DiaryEntry> getAllByPatientProfileOrderByCommitedAtDesc(PatientProfile patientProfile) {
        List<DiaryEntry> diaryEntries = new ArrayList<>();

        for (DiaryEntryJpaRepository repository : repositories.getAllRepositories()) {
            diaryEntries.addAll(repository.getAllByPatientProfileOrderByCommitedAtDesc(patientProfile));
        }

        diaryEntries.sort((entry1, entry2) -> {
            return entry2.getCommitedAt().compareTo(entry1.getCommitedAt());
        });
        return diaryEntries;
    }
}
