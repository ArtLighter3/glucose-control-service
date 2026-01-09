package com.artlighter.glucosecontrolservice.diary.repository.impl;

import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MealEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MedicationEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;

import java.util.*;
import java.util.stream.Collectors;

public class DelegateGeneralDiaryEntryRepositoryFactory {
    public static DelegateGeneralDiaryEntryRepository createInstance(List<DiaryEntryJpaRepository> repositoryList) {
        Map<String, DiaryEntryJpaRepository> repositoryMap = new HashMap<>();

        for (DiaryEntryJpaRepository repository : repositoryList) {
            String entryType = null;

            if (repository instanceof GlucoseEntryRepository) entryType = GlucoseEntry.class.getSimpleName();
            else if (repository instanceof InsulinEntryRepository) entryType = InsulinEntry.class.getSimpleName();
            else if (repository instanceof MealEntryRepository) entryType = MealEntry.class.getSimpleName();
            else if (repository instanceof MedicationEntryRepository) entryType = MedicationEntry.class.getSimpleName();

            if (entryType != null) repositoryMap.put(entryType, repository);
        }

        return new DelegateGeneralDiaryEntryRepository(new DiaryEntryRepositoryCollection(repositoryMap));
    }
}
