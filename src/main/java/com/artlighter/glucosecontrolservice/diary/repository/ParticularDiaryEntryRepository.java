package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.Instant;
import java.util.List;

/**
 * Общий интерфейс для репозитория записей дневника одного определенного типа. Отдельные репозитории нужны
 * для оптимизации различных методов удаления, проверки существования, которые в общем репозитории делают запросы
 * с UNION директивами для каждой таблицы каждого типа записи.
 * @param <T> Тип-класс записи дневника (наследник DiaryEntry)
 */
@NoRepositoryBean
public interface ParticularDiaryEntryRepository<T extends DiaryEntry>
        extends JpaRepository<T, DiaryEntry.DiaryEntryID>, DiaryEntryFetchMethods<T> {

    T findFirstByProfileIdAndCommitedAtBefore(int patientProfileId, Instant before, Sort sort);

}
