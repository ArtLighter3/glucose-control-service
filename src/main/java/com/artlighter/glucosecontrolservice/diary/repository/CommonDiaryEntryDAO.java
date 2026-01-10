package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Обший интерфейс для слоя доступа к записям дневника DiaryEntry ВСЕХ типов
 */

public interface CommonDiaryEntryDAO {
    List<? extends DiaryEntry> getAllOfTypeBetweenDates(DiaryEntryType entryType,
                                                        PatientProfile patientProfile, Instant from, Instant to,
                                                        Sort sort);
    DiaryEntry save(DiaryEntry entry);
    DiaryEntry remove(PatientProfile patientProfile, DiaryEntry entry);
}
