package com.artlighter.glucosecontrolservice.diary.service;

import com.artlighter.glucosecontrolservice.diary.entity.entry.*;
import com.artlighter.glucosecontrolservice.diary.repository.CommonDiaryEntryDAO;
import com.artlighter.glucosecontrolservice.diary.repository.CommonDiaryEntryRepository;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(DiaryEntryService.class)
public class DiaryEntryServiceTests {
    @MockitoBean
    private CommonDiaryEntryDAO diaryEntryDAO;
    @Autowired
    private DiaryEntryService diaryEntryService;

    @Test
    public void addDiaryEntry_EntryExists_ThrowsResourceAlreadyExistsException() {
        DiaryEntry toSave = createEntry(6.7, null, "123");

        when(diaryEntryDAO.exists(toSave)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
           diaryEntryService.addDiaryEntry(toSave, 2, Instant.ofEpochMilli(67895));
        });
    }

    @Test
    public void addDiaryEntry_SavesEntryAndReturnsSaved() {
        DiaryEntry toSave = createEntry(6.7, Instant.ofEpochMilli(11), "123");
       // DiaryEntry.DiaryEntryID id = new DiaryEntry.DiaryEntryID(2, Instant.ofEpochMilli(67895));
        DiaryEntry expected =
                createEntry(toSave.getValue().doubleValue(), Instant.ofEpochMilli(67895), toSave.getNotes());
        expected.setProfileId(2);
        when(diaryEntryDAO.saveOrUpdate(toSave)).thenReturn(expected);

        DiaryEntry actual = diaryEntryService.addDiaryEntry(toSave, 2, Instant.ofEpochMilli(67895));

        assertEquals(2, toSave.getProfileId());
        checkEquality(expected, actual);
        verify(diaryEntryDAO).saveOrUpdate(toSave);
    }

    @Test
    public void updateDiaryEntry_EntryDoesNotExist_ThrowsResourceNotFoundException() {
        DiaryEntry toUpdate = createEntry(6.7, null, "123");
        when(diaryEntryDAO.exists(toUpdate)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            diaryEntryService.updateDiaryEntry(toUpdate, 2, Instant.ofEpochMilli(67895));
        });
    }

    @Test
    public void updateDiaryEntry_UpdatesEntryAndReturnsSaved() {
        DiaryEntry toUpdate = createEntry(6.7, Instant.ofEpochMilli(11), "123");
        DiaryEntry expected =
                createEntry(toUpdate.getValue().doubleValue(), Instant.ofEpochMilli(67895), toUpdate.getNotes());
        expected.setProfileId(2);
        when(diaryEntryDAO.exists(toUpdate)).thenReturn(true);
        when(diaryEntryDAO.saveOrUpdate(toUpdate)).thenReturn(expected);

        DiaryEntry actual = diaryEntryService.updateDiaryEntry(toUpdate, 2, Instant.ofEpochMilli(67895));

        assertEquals(2, toUpdate.getProfileId());
        checkEquality(expected, actual);
        verify(diaryEntryDAO).saveOrUpdate(toUpdate);
    }

    @Test
    public void deleteDiaryEntry_CallsRepositoryToDelete() {
        diaryEntryService.deleteDiaryEntry(DiaryEntryType.GLUCOSE_ENTRY, 2, Instant.ofEpochMilli(67895));
        verify(diaryEntryDAO).deleteById(eq(new DiaryEntry.DiaryEntryID(2,
                Instant.ofEpochMilli(67895),
                DiaryEntryType.GLUCOSE_ENTRY)));
    }

    //TODO дополнить тесты

    private DiaryEntry createEntry(double value, Instant commitedAt, String notes) {
        return createEntry(DiaryEntryType.GLUCOSE_ENTRY, value, commitedAt, notes);
    }

    private DiaryEntry createEntry(DiaryEntryType type, double value, Instant commitedAt, String notes) {
        DiaryEntry entry = null;

        switch (type) {
            case GLUCOSE_ENTRY -> entry = new GlucoseEntry();
            case CARBS_ENTRY -> entry = new CarbsEntry();
            case INSULIN_ENTRY -> entry = new InsulinEntry();
            case MEDICATION_ENTRY -> entry = new MedicationEntry();
        }

        entry.setProfileId(0);
        entry.setCommitedAt(commitedAt);
        entry.setNotes(notes);
        entry.setValue(value);

        return entry;
    }

    private void checkEquality(DiaryEntry expected, DiaryEntry actual) {
        assertSame(expected.getClass(), actual.getClass());

        assertEquals(expected.getValue(), actual.getValue());
        assertEquals(expected.getCommitedAt(), actual.getCommitedAt());
        assertEquals(expected.getNotes(), actual.getNotes());
        assertEquals(expected.getProfileId(), actual.getProfileId());

        if (expected instanceof GlucoseEntry) {
            GlucoseEntry glExpected = (GlucoseEntry) expected;
            GlucoseEntry glActual = (GlucoseEntry) actual;
            assertEquals(glExpected.getMeasurementType(), glActual.getMeasurementType());
        } else if (expected instanceof InsulinEntry) {
            InsulinEntry insExpected = (InsulinEntry) expected;
            InsulinEntry insActual = (InsulinEntry) actual;
            assertEquals(insExpected.getInsulinType(), insActual.getInsulinType());
        } else if (expected instanceof MedicationEntry) {
            MedicationEntry medExpected = (MedicationEntry) expected;
            MedicationEntry medActual = (MedicationEntry) actual;
            assertEquals(medExpected.getMedicationName(), medActual.getMedicationName());
        }
    }
}
