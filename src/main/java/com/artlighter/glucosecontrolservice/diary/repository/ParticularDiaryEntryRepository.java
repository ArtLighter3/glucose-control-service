package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.Instant;
import java.util.List;

/**
 * Общий интерфейс для репозитория записей дневника одного определенного типа.
 * @param <T> Тип-класс записи дневника (наследник DiaryEntry)
 */

@NoRepositoryBean
public interface ParticularDiaryEntryRepository<T extends DiaryEntry>
        extends JpaRepository<T, DiaryEntry.DiaryEntryID> {

//    List<T> getAllByPatientProfile(PatientProfile patientProfile);
//    List<T> getAllByPatientProfile(PatientProfile patientProfile, Sort sort);
    List<T> getAllByPatientProfileAndCommitedAtBetween(PatientProfile patientProfile,
                                                                  Instant from, Instant to);
    List<T> getAllByPatientProfileAndCommitedAtBetween(PatientProfile patientProfile,
                                                       Instant from, Instant to, Sort sort);
}
