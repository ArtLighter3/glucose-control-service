package com.artlighter.glucosecontrolservice.diary.repository.impl;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.DiaryEntryJpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DiaryEntryRepositoryCollection {
    private Map<String, DiaryEntryJpaRepository> repositories;

    public DiaryEntryRepositoryCollection(Map<String, DiaryEntryJpaRepository> repositories) {
        this.repositories = repositories;
    }

    public DiaryEntryJpaRepository getRepositoryForEntity(DiaryEntry entry) {
        if (repositories == null || entry == null) return null;

        return repositories.get(entry.getClass().getSimpleName());
    }

    public Collection<DiaryEntryJpaRepository> getAllRepositories() {
        return repositories.values();
    }
}
