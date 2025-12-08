package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.DiaryEntry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiaryRepository {
    private List<DiaryEntry> measurements = new ArrayList<>();

    public List<DiaryEntry> getMeasurementsByUsername(String username) {
        if (measurements == null) return null;
        return measurements.stream()
                .filter((entry) -> entry.getUser().username().equals(username))
                .sorted((entry1, entry2) -> entry1.getDate().compareTo(entry2.getDate()))
                .toList();
    }

    public DiaryEntry saveMeasurement(DiaryEntry diaryEntry) {
        if (measurements != null) return measurements.add(diaryEntry) ? diaryEntry : null;
        return null;
    }

    public List<DiaryEntry> getAllMeasurements() {
        return measurements;
    }

}
