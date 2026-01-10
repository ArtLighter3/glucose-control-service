package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;

import java.util.List;

/**
 * Обший интерфейс для слоя доступа к записям дневника всех типов
 */

public interface CommonDiaryEntryDAO {
    List<DiaryEntry> getAllByPatientProfileOrderByCommitedAtDesc(PatientProfile patientProfile);
    DiaryEntry save(DiaryEntry entry);
}
