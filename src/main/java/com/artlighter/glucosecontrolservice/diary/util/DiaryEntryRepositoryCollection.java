package com.artlighter.glucosecontrolservice.diary.util;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.ParticularDiaryEntryRepository;

import java.util.Collection;
/**
 * Интерфейс для класса, инкапсулирующего коллекцию репозиториев для записей дневника разных типов.
 * Предоставляет методы для получения репозитория для нужного типа записи по экземпляру DiaryEntry, с которым
 * репозиторий будет проводить операции.
 */

public interface DiaryEntryRepositoryCollection {
    ParticularDiaryEntryRepository getRepositoryForEntity(DiaryEntry entry);

    Collection<ParticularDiaryEntryRepository> getAllRepositories();
}
