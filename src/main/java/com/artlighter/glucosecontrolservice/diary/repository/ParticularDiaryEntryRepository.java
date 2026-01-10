package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Общий интерфейс для репозитория записей дневника одного определенного типа.
 * @param <T> Тип-класс записи дневника (наследник DiaryEntry)
 */

@NoRepositoryBean
public interface ParticularDiaryEntryRepository<T extends DiaryEntry>
        extends JpaRepository<T, DiaryEntry.DiaryEntryID> {

    List<? extends DiaryEntry> getAllByPatientProfileOrderByCommitedAtDesc(PatientProfile patientProfile);

}
