package com.artlighter.glucosecontrolservice.diary.repository.impl;

import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.*;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.MeasurementType;
import com.artlighter.glucosecontrolservice.diary.repository.ParticularDiaryEntryRepository;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryRepositoryCollection;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(DelegatingCommonDiaryEntryCollector.class)
public class DelegatingCommonDiaryEntryCollectorTests {
    @MockitoBean
    private DiaryEntryRepositoryCollection repositories;
    @MockitoBean
    private ParticularDiaryEntryRepository<GlucoseEntry> glucoseRepository;
    @MockitoBean
    private ParticularDiaryEntryRepository<InsulinEntry> insulinRepository;

    @Autowired
    private DelegatingCommonDiaryEntryCollector collector;

    @BeforeEach
    public void setUp() {
        when(repositories.getRepositoryForEntity(argThat((arg) -> arg instanceof GlucoseEntry)))
                .thenReturn(glucoseRepository);
        when(repositories.getRepositoryForEntity(argThat((arg) -> arg instanceof InsulinEntry)))
                .thenReturn(insulinRepository);
        when(repositories.getRepositoryForType(DiaryEntryType.GLUCOSE_ENTRY)).thenReturn(glucoseRepository);
        when(repositories.getRepositoryForType(DiaryEntryType.INSULIN_ENTRY)).thenReturn(insulinRepository);
        when(repositories.getAllRepositories()).thenReturn(List.of(glucoseRepository, insulinRepository));


    }

    @Test
    public void getAllOfTypeBetweenDates_GetsCorrectType_ReturnsListOfEntriesOfType() {
        List<DiaryEntry> storage = generateList();
        Instant to = Instant.now();
        Instant from = Instant.now().minus(Duration.ofDays(20));
        List<GlucoseEntry> expected = filter(storage, from, to, DiaryEntryType.GLUCOSE_ENTRY)
                .stream().map((entry) -> (GlucoseEntry) entry).collect(Collectors.toList());
        when(glucoseRepository.getAllByPatientProfileIdAndCommitedAtBetween(anyInt(), eq(from), eq(to), notNull()))
                .thenReturn(expected);
        when(glucoseRepository.getAllByPatientProfileIdAndCommitedAtBetween(anyInt(), eq(from), eq(to)))
                .thenReturn(expected);

        List<DiaryEntry> actual = collector.getAllOfTypeBetweenDates(DiaryEntryType.GLUCOSE_ENTRY,
                new PatientProfile(), from, to, Sort.unsorted());

        assertNotNull(actual);
        actual = actual.stream().map((entry) -> (GlucoseEntry) entry).collect(Collectors.toList());
        assertIterableEquals(expected, actual);


        to = Instant.now();
        from = Instant.now().minus(Duration.ofDays(30));
        List<InsulinEntry> expected2 = filter(storage, from, to, DiaryEntryType.INSULIN_ENTRY)
                .stream().map((entry) -> (InsulinEntry) entry).collect(Collectors.toList());
        when(insulinRepository.getAllByPatientProfileIdAndCommitedAtBetween(anyInt(), eq(from), eq(to), notNull()))
                .thenReturn(expected2);
        when(insulinRepository.getAllByPatientProfileIdAndCommitedAtBetween(anyInt(), eq(from), eq(to)))
                .thenReturn(expected2);

        List<DiaryEntry> actual2 = collector.getAllOfTypeBetweenDates(DiaryEntryType.INSULIN_ENTRY,
                new PatientProfile(), from, to, Sort.unsorted());

        assertNotNull(actual2);
        actual2 = actual2.stream().map((entry) -> (InsulinEntry) entry).collect(Collectors.toList());
        assertIterableEquals(expected2, actual2);
    }

    @Test
    public void getAllOfTypeBetweenDates_GetsNullType_ReturnsListOfAllEntries() {
        List<DiaryEntry> storage = generateList();
        Instant to = Instant.now();
        Instant from = Instant.now().minus(Duration.ofDays(30));
        List<DiaryEntry> expected = filter(storage, from, to, null);
        List<GlucoseEntry> glucoseEntries = filter(expected, from, to, DiaryEntryType.GLUCOSE_ENTRY)
                .stream().map((entry) -> (GlucoseEntry) entry).collect(Collectors.toList());
        List<InsulinEntry> insulinEntries = filter(expected, from, to, DiaryEntryType.INSULIN_ENTRY)
                .stream().map((entry) -> (InsulinEntry) entry).collect(Collectors.toList());
        when(glucoseRepository.getAllByPatientProfileIdAndCommitedAtBetween(anyInt(), eq(from), eq(to), notNull()))
                .thenReturn(glucoseEntries);
        when(glucoseRepository.getAllByPatientProfileIdAndCommitedAtBetween(anyInt(), eq(from), eq(to)))
                .thenReturn(glucoseEntries);
        when(insulinRepository.getAllByPatientProfileIdAndCommitedAtBetween(anyInt(), eq(from), eq(to), notNull()))
                .thenReturn(insulinEntries);
        when(insulinRepository.getAllByPatientProfileIdAndCommitedAtBetween(anyInt(), eq(from), eq(to)))
                .thenReturn(insulinEntries);

        List<DiaryEntry> actual = collector.getAllOfTypeBetweenDates(null,
                new PatientProfile(), from, to, Sort.unsorted());

        assertNotNull(actual);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void getAllOfTypeBetweenDates_PatientProfileIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                collector.getAllOfTypeBetweenDates(DiaryEntryType.GLUCOSE_ENTRY, null,
                        Instant.now(), Instant.now(), Sort.unsorted()));
    }

    @Test
    public void saveOrUpdate_GetsCorrectEntity_CallsCorrectRepositoryToSaveOrUpdate() {
        GlucoseEntry glucoseEntry = new GlucoseEntry();
        glucoseEntry.setPatientProfile(new PatientProfile());
        glucoseEntry.setCommitedAt(Instant.now());
        when(glucoseRepository.save(glucoseEntry)).thenReturn(glucoseEntry);

        DiaryEntry diaryEntry = collector.saveOrUpdate(glucoseEntry);

        assertNotNull(diaryEntry);
        assertEquals(glucoseEntry, diaryEntry);


        InsulinEntry insulinEntry = new InsulinEntry();
        insulinEntry.setPatientProfile(new PatientProfile());
        insulinEntry.setCommitedAt(Instant.now());
        when(insulinRepository.save(insulinEntry)).thenReturn(insulinEntry);

        diaryEntry = collector.saveOrUpdate(insulinEntry);

        assertNotNull(diaryEntry);
        assertEquals(insulinEntry, diaryEntry);

        verify(glucoseRepository).save(any());
        verify(insulinRepository).save(any());
    }

    @Test
    public void saveOrUpdate_DiaryEntryIsNullOrDoesNotHaveIdFields_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> collector.saveOrUpdate(null));


        GlucoseEntry entry = new GlucoseEntry();
        entry.setValue(2.5);
        entry.setMeasurementType(MeasurementType.AFTER_MEAL);
        entry.setNotes("notes");
        entry.setCommitedAt(Instant.now());
        assertThrows(IllegalArgumentException.class, () -> collector.saveOrUpdate(entry));

        entry.setCommitedAt(null);
        entry.setPatientProfile(new PatientProfile());
        assertThrows(IllegalArgumentException.class, () -> collector.saveOrUpdate(entry));
    }

    @Test
    public void remove_GetsCorrectEntity_CallsCorrectRepositoryToRemove() {
        GlucoseEntry glucoseEntry = new GlucoseEntry();
        glucoseEntry.setPatientProfile(new PatientProfile());
        glucoseEntry.setCommitedAt(Instant.now());

        collector.remove(glucoseEntry);

        InsulinEntry insulinEntry = new InsulinEntry();
        insulinEntry.setPatientProfile(new PatientProfile());
        insulinEntry.setCommitedAt(Instant.now());
       // when(insulinRepository.save(insulinEntry)).thenReturn(insulinEntry);

        collector.remove(insulinEntry);

        verify(glucoseRepository).deleteById(
                new DiaryEntry.DiaryEntryID(glucoseEntry.getPatientProfile(), glucoseEntry.getCommitedAt()));
        verify(insulinRepository).deleteById(
                new DiaryEntry.DiaryEntryID(insulinEntry.getPatientProfile(), insulinEntry.getCommitedAt()));
    }

    @Test
    public void remove_DiaryEntryIsNullOrDoesNotHaveIdFields_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> collector.remove(null));

        GlucoseEntry entry = new GlucoseEntry();
        entry.setValue(2.5);
        entry.setMeasurementType(MeasurementType.AFTER_MEAL);
        entry.setNotes("notes");
        entry.setCommitedAt(Instant.now());
        assertThrows(IllegalArgumentException.class, () -> collector.remove(entry));

        entry.setCommitedAt(null);
        entry.setPatientProfile(new PatientProfile());
        assertThrows(IllegalArgumentException.class, () -> collector.remove(entry));
    }

    @Test
    public void exists_GetsCorrectEntity_CallsCorrectRepositoryAndReturnsCorrectBoolean() {
        GlucoseEntry existentGlucoseEntry = new GlucoseEntry();
        existentGlucoseEntry.setPatientProfile(new PatientProfile());
        existentGlucoseEntry.setCommitedAt(Instant.now().minus(Duration.ofDays(1)));
        GlucoseEntry nonexistentGlucoseEntry = new GlucoseEntry();
        nonexistentGlucoseEntry.setPatientProfile(new PatientProfile());
        nonexistentGlucoseEntry.setCommitedAt(Instant.now().minus(Duration.ofDays(2)));

        InsulinEntry existentInsulinEntry = new InsulinEntry();
        existentInsulinEntry.setPatientProfile(new PatientProfile());
        existentInsulinEntry.setCommitedAt(Instant.now().minus(Duration.ofDays(3)));
        InsulinEntry nonexistentInsulinEntry = new InsulinEntry();
        nonexistentInsulinEntry.setPatientProfile(new PatientProfile());
        nonexistentInsulinEntry.setCommitedAt(Instant.now().minus(Duration.ofDays(4)));

        when(insulinRepository.existsById(new DiaryEntry.DiaryEntryID(existentInsulinEntry.getPatientProfile(),
                existentInsulinEntry.getCommitedAt()))).thenReturn(true);
        when(insulinRepository.existsById(new DiaryEntry.DiaryEntryID(nonexistentInsulinEntry.getPatientProfile(),
                nonexistentInsulinEntry.getCommitedAt()))).thenReturn(false);
        when(glucoseRepository.existsById(new DiaryEntry.DiaryEntryID(existentGlucoseEntry.getPatientProfile(),
                existentGlucoseEntry.getCommitedAt()))).thenReturn(true);
        when(glucoseRepository.existsById(new DiaryEntry.DiaryEntryID(nonexistentGlucoseEntry.getPatientProfile(),
                nonexistentGlucoseEntry.getCommitedAt()))).thenReturn(false);

        assertTrue(collector.exists(existentGlucoseEntry));
        assertTrue(collector.exists(existentInsulinEntry));
        assertFalse(collector.exists(nonexistentGlucoseEntry));
        assertFalse(collector.exists(nonexistentInsulinEntry));

        verify(glucoseRepository, times(2)).existsById(any());
        verify(insulinRepository, times(2)).existsById(any());
    }

    @Test
    public void exists_DiaryEntryIsNullOrDoesNotHaveIdFields_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> collector.exists(null));

        GlucoseEntry entry = new GlucoseEntry();
        entry.setValue(2.5);
        entry.setMeasurementType(MeasurementType.AFTER_MEAL);
        entry.setNotes("notes");
        entry.setCommitedAt(Instant.now());
        assertThrows(IllegalArgumentException.class, () -> collector.exists(entry));

        entry.setCommitedAt(null);
        entry.setPatientProfile(new PatientProfile());
        assertThrows(IllegalArgumentException.class, () -> collector.exists(entry));
    }

    private List<DiaryEntry> filter(List<? extends DiaryEntry> list, Instant from, Instant to,
                                              DiaryEntryType type) {
        List<DiaryEntry> filtered = new ArrayList<>();

        for (DiaryEntry entry : list) {
            if (entry.getCommitedAt().isBefore(to) && entry.getCommitedAt().isAfter(from)) {
                boolean isNeededType = (type == null) || (type == DiaryEntryType.GLUCOSE_ENTRY && entry instanceof GlucoseEntry) ||
                        (type == DiaryEntryType.INSULIN_ENTRY && entry instanceof InsulinEntry);
                if (isNeededType) filtered.add(entry);
            }
        }

        return filtered;
    }

    private List<DiaryEntry> generateList() {
        List<DiaryEntry> list = new ArrayList<>();
        Instant now = Instant.now();

        list.add(createEntry(DiaryEntryType.GLUCOSE_ENTRY,
                now.minus(Duration.ofDays(12))));
        list.add(createEntry(DiaryEntryType.GLUCOSE_ENTRY,
                now.minus(Duration.ofDays(20))));

        list.add(createEntry(DiaryEntryType.INSULIN_ENTRY,
                now.minus(Duration.ofDays(25))));
        list.add(createEntry(DiaryEntryType.INSULIN_ENTRY,
                now.minus(Duration.ofDays(12))));

        list.add(createEntry(DiaryEntryType.GLUCOSE_ENTRY,
                now.minus(Duration.ofDays(13))));
        list.add(createEntry(DiaryEntryType.GLUCOSE_ENTRY,
                now.minus(Duration.ofDays(26))));

        list.add(createEntry(DiaryEntryType.INSULIN_ENTRY,
                now.minus(Duration.ofDays(5))));
        list.add(createEntry(DiaryEntryType.INSULIN_ENTRY,
                now.minus(Duration.ofDays(23))));

        list.sort((entry1, entry2) -> entry2.getCommitedAt().compareTo(entry1.getCommitedAt()));

        return list;
    }

    private DiaryEntry createEntry(DiaryEntryType type, Instant commitedAt) {
        DiaryEntry entry = null;
        switch (type) {
            case GLUCOSE_ENTRY -> entry = new GlucoseEntry();
            case INSULIN_ENTRY -> entry = new InsulinEntry();
            case CARBS_ENTRY -> entry = new CarbsEntry();
            case MEDICATION_ENTRY -> entry = new MedicationEntry();
        }

       // entry.setValue(value);
        entry.setCommitedAt(commitedAt);

        return entry;
    }
}
