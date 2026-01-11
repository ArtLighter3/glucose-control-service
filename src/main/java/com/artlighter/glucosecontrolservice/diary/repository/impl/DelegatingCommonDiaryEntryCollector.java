package com.artlighter.glucosecontrolservice.diary.repository.impl;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryRepositoryCollection;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация CommonDiaryEntryDAO, делегирующая доступ к данным записей дневника
 * репозиториям соответствующего типа записи дневника. В случае доступа ко всем типам записей пользователя
 * собирает данные из разных репозиториев, делая отдельные запросы к каждому типу, что может сказываться
 * на производительности.
 */

@Repository
public class DelegatingCommonDiaryEntryCollector implements CommonDiaryEntryDAO {
    private DiaryEntryRepositoryCollection repositories;

   // private EntityManager entityManager;

    public DelegatingCommonDiaryEntryCollector(DiaryEntryRepositoryCollection repositories) {
        this.repositories = repositories;
    }

    @Override
    public List<? extends DiaryEntry> getAllOfTypeBetweenDates(DiaryEntryType entryType,
                                                               PatientProfile patientProfile, Instant from, Instant to,
                                                               Sort sort) {
        if (entryType == null) return collectAllBetweenDates(patientProfile, from, to, sort);

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForType(entryType);
        return repository.getAllByPatientProfileIdAndCommitedAtBetween(patientProfile.getId(), from, to, sort);
    }

    @Override
    public DiaryEntry saveOrUpdate(DiaryEntry entry) {
        checkArguments(entry);

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForEntity(entry);
        return (DiaryEntry) repository.save(entry);
    }

    @Override
    public DiaryEntry remove(DiaryEntry entry) {
        checkArguments(entry);

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForEntity(entry);
        repository.deleteById(new DiaryEntry.DiaryEntryID(entry.getPatientProfile(), entry.getCommitedAt()));
        return entry;
    }

    @Override
    public boolean exists(DiaryEntry entry) {
        checkArguments(entry);

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForEntity(entry);
        return repository.existsById(new DiaryEntry.DiaryEntryID(entry.getPatientProfile(), entry.getCommitedAt()));
    }

    private List<DiaryEntry> collectAllBetweenDates(PatientProfile patientProfile, Instant from, Instant to,
                                                    Sort sort) {
        List<DiaryEntry> diaryEntries = new ArrayList<>();

        for (ParticularDiaryEntryRepository repository : repositories.getAllRepositories()) {
            diaryEntries.addAll(repository.getAllByPatientProfileIdAndCommitedAtBetween(patientProfile.getId(), from, to));
        }
        //TODO реализовать сортировку по Sort
        diaryEntries.sort((entry1, entry2) ->
                entry2.getCommitedAt().compareTo(entry1.getCommitedAt()));
        return diaryEntries;
    }

    private void checkArguments(DiaryEntry entry) {
        if (entry == null) throw new IllegalArgumentException("DiaryEntry must not be null");
        if (entry.getPatientProfile() == null)
            throw new IllegalArgumentException("DiaryEntry must have a patientProfile");
    }

}
