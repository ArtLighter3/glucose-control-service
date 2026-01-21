package com.artlighter.glucosecontrolservice.diary.util;

import com.artlighter.glucosecontrolservice.diary.entity.entry.*;

public enum DiaryEntryType {
        GLUCOSE_ENTRY(GlucoseEntry.class),
        INSULIN_ENTRY(InsulinEntry.class),
        CARBS_ENTRY(CarbsEntry.class),
        MEDICATION_ENTRY(MedicationEntry.class);

        private final Class<? extends DiaryEntry> entryClass;

        DiaryEntryType(Class<? extends DiaryEntry> entryClass) {
            this.entryClass = entryClass;
        }

        public Class<? extends DiaryEntry> getEntryClass() {
            return entryClass;
        }
}
