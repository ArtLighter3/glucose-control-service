package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, DiaryEntry.DiaryEntryID> {

    List<DiaryEntry> getDiaryEntriesByPatientProfileOrderByCommitedAtDesc(PatientProfile patientProfile);
}
