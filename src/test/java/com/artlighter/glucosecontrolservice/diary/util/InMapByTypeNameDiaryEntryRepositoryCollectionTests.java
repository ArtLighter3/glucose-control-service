package com.artlighter.glucosecontrolservice.diary.util;

import com.artlighter.glucosecontrolservice.auth.util.exception.NoRepositoryForEntryTypeException;
import com.artlighter.glucosecontrolservice.diary.entity.entry.*;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
public class InMapByTypeNameDiaryEntryRepositoryCollectionTests {
    private InMapByTypeNameDiaryEntryRepositoryCollection collection;
    private Map<String, ParticularDiaryEntryRepository> repositoryMap;

    @BeforeEach
    public void setUp() {
        GlucoseEntryRepository glucoseEntryRepository = Mockito.mock(GlucoseEntryRepository.class);
        InsulinEntryRepository insulinEntryRepository = Mockito.mock(InsulinEntryRepository.class);
        MealEntryRepository mealEntryRepository = Mockito.mock(MealEntryRepository.class);
        //MedicationEntryRepository medicationEntryRepository = Mockito.mock(MedicationEntryRepository.class);

        repositoryMap = new HashMap<>();
        repositoryMap.put("GlucoseEntry", glucoseEntryRepository);
        repositoryMap.put("InsulinEntry", insulinEntryRepository);
        repositoryMap.put("MealEntry", mealEntryRepository);
        //repositoryMap.put("MedicationEntry", medicationEntryRepository);

        collection = new InMapByTypeNameDiaryEntryRepositoryCollection(repositoryMap);
    }

    @Test
    public void getRepositoryForEntity_GetsDiaryEntryWithExistingRepository_ReturnsNeededRepository() {
        GlucoseEntry glucoseEntry = new GlucoseEntry();
        InsulinEntry insulinEntry = new InsulinEntry();
       // MedicationEntry medicationEntry = new MedicationEntry();
        MealEntry mealEntry = new MealEntry();

        ParticularDiaryEntryRepository glucoseEntryRepository = collection.getRepositoryForEntity(glucoseEntry);
        ParticularDiaryEntryRepository insulinEntryRepository = collection.getRepositoryForEntity(insulinEntry);
        //ParticularDiaryEntryRepository medicationEntryRepository = collection.getRepositoryForEntity(medicationEntry);
        ParticularDiaryEntryRepository mealEntryRepository = collection.getRepositoryForEntity(mealEntry);

        assertNotNull(glucoseEntryRepository);
        assertNotNull(insulinEntryRepository);
        //assertNotNull(medicationEntryRepository);
        assertNotNull(mealEntryRepository);
        assertTrue(glucoseEntryRepository instanceof GlucoseEntryRepository);
        assertTrue(insulinEntryRepository instanceof InsulinEntryRepository);
        //assertTrue(medicationEntryRepository instanceof MedicationEntryRepository);
        assertTrue(mealEntryRepository instanceof MealEntryRepository);
    }

    @Test
    public void getRepositoryForEntity_GetsNullDiaryEntry_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> collection.getRepositoryForEntity(null));
    }

    @Test
    public void getRepositoryForEntity_DoesNotFindRepositoryForEntity_ThrowsNoRepositoryFoundException() {
        MedicationEntry medicationEntry = new MedicationEntry();

        assertThrows(NoRepositoryForEntryTypeException.class, () -> collection.getRepositoryForEntity(medicationEntry));
    }

    @Test
    public void getRepositoryForType_GetsDiaryEntryTypeWithExistingRepository_ReturnsNeededRepository() {
        ParticularDiaryEntryRepository glucoseEntryRepository =
                collection.getRepositoryForType(DiaryEntryType.GLUCOSE_ENTRY);
        ParticularDiaryEntryRepository insulinEntryRepository =
                collection.getRepositoryForType(DiaryEntryType.INSULIN_ENTRY);
//        ParticularDiaryEntryRepository medicationEntryRepository =
//                collection.getRepositoryForType(DiaryEntryType.MEDICATION_ENTRY);
        ParticularDiaryEntryRepository mealEntryRepository =
                collection.getRepositoryForType(DiaryEntryType.MEAL_ENTRY);

        assertNotNull(glucoseEntryRepository);
        assertNotNull(insulinEntryRepository);
        //assertNotNull(medicationEntryRepository);
        assertNotNull(mealEntryRepository);
        assertTrue(glucoseEntryRepository instanceof GlucoseEntryRepository);
        assertTrue(insulinEntryRepository instanceof InsulinEntryRepository);
       // assertTrue(medicationEntryRepository instanceof MedicationEntryRepository);
        assertTrue(mealEntryRepository instanceof MealEntryRepository);
    }

    @Test
    public void getRepositoryForType_GetsNullDiaryEntryType_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> collection.getRepositoryForType(null));
    }

    @Test
    public void getRepositoryForType_DoesNotFindRepositoryForType_ThrowsNoRepositoryFoundException() {
        assertThrows(NoRepositoryForEntryTypeException.class, () ->
                collection.getRepositoryForType(DiaryEntryType.MEDICATION_ENTRY));
    }

    @Test
    public void getAllRepositories_ReturnsCorrectCollectionOfAllRepositories() {
        Collection<ParticularDiaryEntryRepository> expected = repositoryMap.values();

        Collection<ParticularDiaryEntryRepository> actual = collection.getAllRepositories();
        assertNotNull(actual);
        assertIterableEquals(expected, actual);
    }
}
