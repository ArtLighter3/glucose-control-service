package com.artlighter.glucosecontrolservice.diary.repository.impl;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryRepositoryCollection;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * Реализация CommonDiaryEntryDAO, работающая как прослойка между
 * нагромождением репозиториев для разных типов записей дневника и логикой
 * и делегирующая доступ к репозиториям соответствующего типа записи, либо к общему
 * репозиторию в зависимости от того, какой из вариантов считается более оптимизированным.
 */

@Component
public class DelegatingCommonDiaryEntryDAO implements CommonDiaryEntryDAO {
    private DiaryEntryRepositoryCollection repositories;
    private CommonDiaryEntryRepository commonDiaryEntryRepository;

   // private EntityManager entityManager;

    public DelegatingCommonDiaryEntryDAO(DiaryEntryRepositoryCollection repositories,
                                         CommonDiaryEntryRepository commonDiaryEntryRepository) {
        this.repositories = repositories;
        this.commonDiaryEntryRepository = commonDiaryEntryRepository;
    }

    @Override
    public List<DiaryEntry> getAllOfTypeBetweenDates(DiaryEntryType entryType, int patientProfileId,
                                                     Instant from, Instant to) {
        if (entryType == null)
            return commonDiaryEntryRepository.getAllByProfileIdAndCommitedAtBetween(patientProfileId, from, to);

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForType(entryType);
        return repository.getAllByProfileIdAndCommitedAtBetween(patientProfileId, from, to);
    }

    @Override
    public Slice<DiaryEntry> getAllOfTypeBetweenDates(DiaryEntryType entryType,
                                                      int patientProfileId, Instant from, Instant to,
                                                      Pageable pageable) {
        if (entryType == null)
            return commonDiaryEntryRepository
                    .getAllByProfileIdAndCommitedAtBetween(patientProfileId, from, to, pageable);

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForType(entryType);
        return repository.getAllByProfileIdAndCommitedAtBetween(patientProfileId, from, to, pageable);
    }

    @Override
    public Slice<DiaryEntry> getAllOfType(DiaryEntryType entryType, int patientProfileId, Pageable pageable) {
        if (entryType == null)
            return commonDiaryEntryRepository.getAllByProfileId(patientProfileId, pageable);

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForType(entryType);
        return repository.getAllByProfileId(patientProfileId, pageable);
    }

    @Override
    public Slice<DiaryEntry> getAllOfTypeBefore(DiaryEntryType entryType, int patientProfileId, Instant before,
                                                Pageable pageable) {
        if (entryType == null)
            return commonDiaryEntryRepository.getAllByProfileIdAndCommitedAtBefore(patientProfileId, before, pageable);

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForType(entryType);
        return repository.getAllByProfileIdAndCommitedAtBefore(patientProfileId, before, pageable);
    }

    /**
     * @throws IllegalArgumentException если entryType равен null;
     */
    @Override
    public DiaryEntry findFirstEntryOfType(DiaryEntryType entryType, int patientProfileId, Sort sort) {
        if (entryType == null) throw new IllegalArgumentException("entryType cannot be null");

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForType(entryType);
        return repository.findFirstByProfileIdAndCommitedAtBefore(patientProfileId, Instant.now(), sort);
    }

    /**
     * @throws IllegalArgumentException в случае, если DiaryEntry равен null, либо не содержит
     * внутри идентифицирующего его поля commitedAt;
     */
    @Override
    public DiaryEntry saveOrUpdate(DiaryEntry entry) {
        checkArguments(entry);

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForEntity(entry);
        return (DiaryEntry) repository.save(entry);
    }

    /**
     * @throws IllegalArgumentException в случае, если id равен null, либо не содержит
     * внутри идентифицирующих полей;
     */
    @Override
    public void deleteById(DiaryEntry.DiaryEntryID id) {
        if (id == null || id.getCommitedAt() == null || id.getType() == null)
            throw new IllegalArgumentException("id cannot be null and must contain id data");

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForType(id.getType());
        repository.deleteById(id);
    }

    /**
     * @throws IllegalArgumentException в случае, если DiaryEntry равен null, либо не содержит
     * внутри идентифицирующего его поля commitedAt;
     */
    @Override
    public boolean exists(DiaryEntry entry) {
        checkArguments(entry);
       // if (entry == null || entry.getPatientProfile() == null) return false;

        ParticularDiaryEntryRepository repository = repositories.getRepositoryForEntity(entry);
        return repository.existsById(new DiaryEntry.DiaryEntryID(entry.getProfileId(),
                entry.getCommitedAt(),
                entry.getType()));
    }

//    private Slice<DiaryEntry> collectAllBetweenDates(int patientProfileId, Instant from, Instant to,
//                                                    Pageable pageable) {
//
//        return commonDiaryEntryRepository.getAllByProfileIdAndCommitedAtBetween(patientProfileId, from, to, pageable);
////        List<DiaryEntry> diaryEntries = new ArrayList<>();
////        //TODO можно просто добавить отдельный DAO, в котором будет один запрос с UNION для получения всех записей
////        for (ParticularDiaryEntryRepository repository : repositories.getAllRepositories()) {
////            diaryEntries.addAll(repository.getAllByProfileIdAndCommitedAtBetween(patientProfileId, from, to));
////        }
////        //TODO реализовать сортировку по Sort
////        diaryEntries.sort((entry1, entry2) ->
////                entry2.getCommitedAt().compareTo(entry1.getCommitedAt()));
////        return diaryEntries;
//    }
//
//    private Slice<DiaryEntry> collectAll(int patientProfileId, Pageable pageable) {
//        return commonDiaryEntryRepository.getAllByProfileId(patientProfileId, pageable);
//    }
//
//    private Slice<DiaryEntry> collectAllBeforeDate(int patientProfileId, Instant before, Pageable pageable) {
//        return commonDiaryEntryRepository.getAllByProfileIdAndCommitedAtBefore(patientProfileId, before, pageable);
//    }

    private void checkArguments(DiaryEntry entry) {
        if (entry == null) throw new IllegalArgumentException("DiaryEntry cannot be null");
        if (entry.getCommitedAt() == null)
            throw new IllegalArgumentException("DiaryEntry must have an identification " +
                    "fields patientProfile and commitedAt");
    }

}
