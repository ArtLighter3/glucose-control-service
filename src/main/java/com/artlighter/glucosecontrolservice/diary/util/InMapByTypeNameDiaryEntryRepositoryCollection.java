package com.artlighter.glucosecontrolservice.diary.util;

import com.artlighter.glucosecontrolservice.diary.entity.entry.*;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import com.artlighter.glucosecontrolservice.diary.util.exception.NoRepositoryForEntryTypeException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация коллекции репозиториев для записей дневника разных типов, использующая в качестве хранилища словарь,
 * в котором ключами являются строковые имена классов для типов записей дневника (наследники DiaryEntry).
 */
@Component
public class InMapByTypeNameDiaryEntryRepositoryCollection implements DiaryEntryRepositoryCollection {
    private Map<String, ParticularDiaryEntryRepository<? extends DiaryEntry>> repositories;

    public InMapByTypeNameDiaryEntryRepositoryCollection(List<ParticularDiaryEntryRepository<? extends DiaryEntry>>
                                                                 repositoryList) {
        repositories = new HashMap<>();

        for (ParticularDiaryEntryRepository<? extends DiaryEntry> repository : repositoryList) {
            String entryType = null;

            if (repository instanceof GlucoseEntryRepository) entryType = GlucoseEntry.class.getSimpleName();
            else if (repository instanceof InsulinEntryRepository) entryType = InsulinEntry.class.getSimpleName();
            else if (repository instanceof CarbsEntryRepository) entryType = CarbsEntry.class.getSimpleName();
            else if (repository instanceof MedicationEntryRepository) entryType = MedicationEntry.class.getSimpleName();

            if (entryType != null) repositories.put(entryType, repository);
        }
    }

    /**
     * @throws IllegalArgumentException в случае, если передаваемый DiaryEntry равен null;
     * @throws NoRepositoryForEntryTypeException в случае,
     * если для передаваемого объекта репозиторий не был найден;
     */
    @Override
    public ParticularDiaryEntryRepository<? extends DiaryEntry> getRepositoryForEntity(DiaryEntry entry) {
        if (entry == null)
            throw new IllegalArgumentException("DiaryEntry cannot be null");

        ParticularDiaryEntryRepository<? extends DiaryEntry> repository =
                repositories.get(entry.getClass().getSimpleName());
        if (repository == null) {
            throw new NoRepositoryForEntryTypeException(entry.getClass().getSimpleName(),
                    String.format("No repository for entry type %s", entry.getClass().getSimpleName()));
        }

        return repository;
    }

    /**
     * @throws IllegalArgumentException в случае, если передаваемый DiaryEntryType равен null;
     * @throws NoRepositoryForEntryTypeException в случае, если для передаваемого типа репозиторий не был найден;
     */
    @Override
    public ParticularDiaryEntryRepository<? extends DiaryEntry> getRepositoryForType(DiaryEntryType entryType) {
        if (entryType == null)
            throw new IllegalArgumentException("DiaryEntryType cannot be null");

        ParticularDiaryEntryRepository<? extends DiaryEntry> repository =
                repositories.get(entryType.getEntryClass().getSimpleName());
        if (repository == null) {
            throw new NoRepositoryForEntryTypeException(entryType.name(),
                    String.format("No repository for entry type %s", entryType.name()));
        }

        return repository;
    }

    @Override
    public Collection<ParticularDiaryEntryRepository<? extends DiaryEntry>> getAllRepositories() {
        return repositories.values();
    }
}
