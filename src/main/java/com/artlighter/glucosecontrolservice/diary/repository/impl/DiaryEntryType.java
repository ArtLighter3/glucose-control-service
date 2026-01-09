package com.artlighter.glucosecontrolservice.diary.repository.impl;

import com.artlighter.glucosecontrolservice.diary.repository.*;

enum DiaryEntryType {
        GLUCOSE_ENTRY(GlucoseEntryRepository.class),
        INSULIN_ENTRY(InsulinEntryRepository.class),
        MEAL_ENTRY(MealEntryRepository.class),
        MEDICATION_ENTRY(MedicationEntryRepository.class);

        private Class<? extends DiaryEntryJpaRepository> repositoryClass;

        DiaryEntryType(Class<? extends DiaryEntryJpaRepository> repositoryClass) {
            this.repositoryClass = repositoryClass;
        }

        Class<? extends DiaryEntryJpaRepository> getRepositoryClass() {
            return repositoryClass;
        }
}
