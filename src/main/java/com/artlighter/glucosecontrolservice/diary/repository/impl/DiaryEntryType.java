package com.artlighter.glucosecontrolservice.diary.repository.impl;

import com.artlighter.glucosecontrolservice.diary.repository.*;

enum DiaryEntryType {
        GLUCOSE_ENTRY(GlucoseEntryRepository.class),
        INSULIN_ENTRY(InsulinEntryRepository.class),
        MEAL_ENTRY(MealEntryRepository.class),
        MEDICATION_ENTRY(MedicationEntryRepository.class);

        private Class<? extends ParticularDiaryEntryRepository> repositoryClass;

        DiaryEntryType(Class<? extends ParticularDiaryEntryRepository> repositoryClass) {
            this.repositoryClass = repositoryClass;
        }

        Class<? extends ParticularDiaryEntryRepository> getRepositoryClass() {
            return repositoryClass;
        }
}
