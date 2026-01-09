package com.artlighter.glucosecontrolservice.diary.config;

import com.artlighter.glucosecontrolservice.diary.repository.DiaryEntryJpaRepository;
import com.artlighter.glucosecontrolservice.diary.repository.impl.DelegateGeneralDiaryEntryRepository;
import com.artlighter.glucosecontrolservice.diary.repository.impl.DelegateGeneralDiaryEntryRepositoryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class DiaryConfiguration {
    @Bean
    public DelegateGeneralDiaryEntryRepository diaryEntryRepository(List<DiaryEntryJpaRepository> repositoryList) {
        return DelegateGeneralDiaryEntryRepositoryFactory.createInstance(repositoryList);
    }
}
