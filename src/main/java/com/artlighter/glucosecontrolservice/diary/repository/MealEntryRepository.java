package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.MealEntry;
import org.springframework.stereotype.Repository;

@Repository
public interface MealEntryRepository extends DiaryEntryJpaRepository<MealEntry> {
}
