package com.artlighter.glucosecontrolservice.diary.util;

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
        if (repositories == null || entry == null) return null;

        return repositories.get(entry.getClass().getSimpleName());
    }

    @Override
    public Collection<ParticularDiaryEntryRepository> getAllRepositories() {
        return repositories.values();
    }
}
