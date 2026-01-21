package com.artlighter.glucosecontrolservice.diary.config;

import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.CarbsEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MedicationEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryRepositoryCollection;
import com.artlighter.glucosecontrolservice.diary.util.InMapByTypeNameDiaryEntryRepositoryCollection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class DiaryConfiguration {
    @Bean
    public DiaryEntryRepositoryCollection diaryEntryRepositoryCollection(List<ParticularDiaryEntryRepository>
                                                                                     repositoryList) {
        Map<String, ParticularDiaryEntryRepository> repositoryMap = new HashMap<>();

        for (ParticularDiaryEntryRepository repository : repositoryList) {
            String entryType = null;

            if (repository instanceof GlucoseEntryRepository) entryType = GlucoseEntry.class.getSimpleName();
            else if (repository instanceof InsulinEntryRepository) entryType = InsulinEntry.class.getSimpleName();
            else if (repository instanceof CarbsEntryRepository) entryType = CarbsEntry.class.getSimpleName();
            else if (repository instanceof MedicationEntryRepository) entryType = MedicationEntry.class.getSimpleName();

            if (entryType != null) repositoryMap.put(entryType, repository);
        }

        return new InMapByTypeNameDiaryEntryRepositoryCollection(repositoryMap);
    }
}
