package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.MedicationEntry;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationEntryRepository extends DiaryEntryJpaRepository<MedicationEntry> {
}
