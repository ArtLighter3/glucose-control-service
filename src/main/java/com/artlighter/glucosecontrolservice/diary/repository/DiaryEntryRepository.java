package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;

import java.util.List;

public interface DiaryEntryRepository<T extends DiaryEntry> {
    List<T> getAllByPatientProfileOrderByCommitedAtDesc(PatientProfile patientProfile);
}
