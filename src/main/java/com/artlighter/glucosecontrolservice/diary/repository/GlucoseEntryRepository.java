package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import org.springframework.stereotype.Repository;

@Repository
public interface GlucoseEntryRepository extends DiaryEntryJpaRepository<GlucoseEntry> {
}
