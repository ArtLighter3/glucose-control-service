package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
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
    List<T> getAllByProfileIdAndCommitedAtBetween(int profileId,
                                                  Instant from, Instant to);
    List<T> getAllByProfileIdAndCommitedAtBetween(int profileId,
                                                  Instant from, Instant to, Sort sort);
    T findFirstByProfileIdAndCommitedAtBefore(int patientProfileId, Instant before, Sort sort);
}
