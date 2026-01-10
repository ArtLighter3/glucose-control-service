package com.artlighter.glucosecontrolservice.diary.util;

import com.artlighter.glucosecontrolservice.auth.util.exception.NoRepositoryForEntryTypeException;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.ParticularDiaryEntryRepository;

import java.util.Collection;
import java.util.Map;

/**
 * Реализация коллекции репозиториев для записей дневника разных типов, использующая в качестве хранилища словарь,
 * в котором ключами являются строковые имена классов для типов записей дневника (наследники DiaryEntry).
 */
public class InMapByTypeNameDiaryEntryRepositoryCollection implements DiaryEntryRepositoryCollection {
    private Map<String, ParticularDiaryEntryRepository> repositories;

    public InMapByTypeNameDiaryEntryRepositoryCollection(Map<String, ParticularDiaryEntryRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public ParticularDiaryEntryRepository getRepositoryForEntity(DiaryEntry entry) {
        if (entry == null)
            throw new IllegalArgumentException("DiaryEntry cannot be null");

        ParticularDiaryEntryRepository repository = repositories.get(entry.getClass().getSimpleName());
        if (repository == null) {
            throw new NoRepositoryForEntryTypeException(entry.getClass().getSimpleName(),
                    String.format("No repository for entry type %s", entry.getClass().getSimpleName()));
        }

        return repository;
    }

    @Override
    public ParticularDiaryEntryRepository getRepositoryForType(DiaryEntryType entryType) {
        if (entryType == null)
            throw new IllegalArgumentException("DiaryEntryType cannot be null");

        ParticularDiaryEntryRepository repository = repositories.get(entryType.getEntryClass().getSimpleName());
        if (repository == null) {
            throw new NoRepositoryForEntryTypeException(entryType.name(),
                    String.format("No repository for entry type %s", entryType.name()));
        }

        return repository;
    }

    @Override
    public Collection<ParticularDiaryEntryRepository> getAllRepositories() {
        return repositories.values();
    }
}
