package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import org.springframework.stereotype.Repository;

@Repository
public interface InsulinEntryRepository extends ParticularDiaryEntryRepository<InsulinEntry> {
}
