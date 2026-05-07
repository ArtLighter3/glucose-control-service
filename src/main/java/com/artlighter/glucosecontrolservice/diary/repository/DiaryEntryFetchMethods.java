package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.List;

/**
 * Различные методы для выборки записей дневника. Выделены в отдельный интерфейс для того,
 * чтобы как отдельные репозитории для разных типов записей ParticularDiaryEntryRepository, так и общий репозиторий
 * CommonDiaryEntryRepository могли перенять эти одинаковые методы
 * @param <T> Тип записи дневника для методов выборки (наследник DiaryEntry)
 */

public interface DiaryEntryFetchMethods<T extends DiaryEntry> {
    Slice<T> getAllByProfileId(int profileId, Pageable pageable);
    List<T> getAllByProfileIdAndCommitedAtBetween(int profileId, Instant from, Instant to);
    Slice<T> getAllByProfileIdAndCommitedAtBetween(int profileId, Instant from, Instant to, Pageable pageable);
    Slice<T> getAllByProfileIdAndCommitedAtBefore(int profileId, Instant before, Pageable pageable);
}
