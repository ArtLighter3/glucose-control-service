package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Общий репозиторий для всех типов записей дневника. Нужен для оптимизации запросов выборки и упрощения пагинации, ведь
 * только с отдельными репозиториями необходимо производить отдельный запрос для каждой таблицы каждого типа и собирать
 *  все записи уже внутри.
 */

@Repository
public interface CommonDiaryEntryRepository
        extends JpaRepository<DiaryEntry, DiaryEntry.DiaryEntryID>, DiaryEntryFetchMethods<DiaryEntry> {

}
