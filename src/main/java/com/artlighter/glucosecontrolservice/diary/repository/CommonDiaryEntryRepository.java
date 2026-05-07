package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonDiaryEntryRepository extends ParticularDiaryEntryRepository<DiaryEntry> {

}
