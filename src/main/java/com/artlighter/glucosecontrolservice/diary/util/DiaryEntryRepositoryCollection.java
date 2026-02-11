package com.artlighter.glucosecontrolservice.diary.util;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.ParticularDiaryEntryRepository;
import com.artlighter.glucosecontrolservice.diary.util.exception.NoRepositoryForEntryTypeException;

import java.util.Collection;
/**
 * Интерфейс для класса, инкапсулирующего коллекцию репозиториев для записей дневника разных типов.
 * Предоставляет методы для получения репозитория для нужного типа записи по экземпляру DiaryEntry, с которым
 * репозиторий будет проводить операции.
 */

public interface DiaryEntryRepositoryCollection {
    /**
     * Функция находит репозиторий для записи дневника определенного типа, соответствующий типу передаваемого экземпляра
     * записи
     * @param entry DiaryEntry, объект записи дневника
     * @return объект репозитория, подходящий переданному объекту DiaryEntry
     * @throws IllegalArgumentException в случае, если передаваемый DiaryEntry равен null
     * @throws NoRepositoryForEntryTypeException в случае,
     * если для передаваемого объекта репозиторий не был найден
     */
    ParticularDiaryEntryRepository getRepositoryForEntity(DiaryEntry entry);
    /**
     * Функция находит репозиторий для записи дневника определенного типа
     * @param entryType тип записи дневника из перечисления
     * @return объект репозитория, подходящий переданному типу записи дневника DiaryEntryType
     * @throws IllegalArgumentException в случае, если передаваемый DiaryEntryType равен null
     * @throws NoRepositoryForEntryTypeException в случае,
     * если для передаваемого типа репозиторий не был найден
     */
    ParticularDiaryEntryRepository getRepositoryForType(DiaryEntryType entryType);

    /**
     * Функция собирает все репозитории для всех типов записей дневника
     * @return коллекция репозиториев для всех типов записей дневника
     */
    Collection<ParticularDiaryEntryRepository> getAllRepositories();

}
