package com.artlighter.glucosecontrolservice.diary;

import com.artlighter.glucosecontrolservice.diary.entity.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.DiaryRepository;
import com.artlighter.glucosecontrolservice.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class DiaryService {
    private DiaryRepository diaryRepository;

    @Autowired
    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public DiaryEntry saveMeasurement(DiaryEntry diaryEntry) {
        return diaryRepository.saveMeasurement(diaryEntry);
    }

    public List<DiaryEntry> getAllUserMeasurements(UserDTO user) {
        if (user == null) return Collections.emptyList();

        List<DiaryEntry> measurements = diaryRepository.getMeasurementsByUsername(user.username());

        if (measurements == null) return Collections.emptyList();
        return measurements;
    }

    public List<DiaryEntry> getUserMeasurementsFromPeriod(UserDTO user, Date from, Date to) {
        List<DiaryEntry> measurements = getAllUserMeasurements(user);

        return measurements.stream()
                .filter((entry) -> entry.getDate().after(from) && entry.getDate().before(to))
                .toList();
    }

    public List<DiaryEntry> getAllMeasurements() {
        return diaryRepository.getAllMeasurements();
    }
}
