package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface CommonDiaryEntryRepository extends JpaRepository<DiaryEntry, DiaryEntry.DiaryEntryID> {
    @Modifying
    @Query("DELETE FROM DiaryEntry e " +
            "WHERE TYPE(e) = :typeClass AND e.profileId = :patientProfileId AND e.commitedAt = :commitedAt")
    void deleteById(int patientProfileId, Instant commitedAt, Class<? extends DiaryEntry> typeClass);

    Slice<DiaryEntry> getAllByProfileId(int profileId, Pageable pageable);
    List<DiaryEntry> getAllByProfileIdAndCommitedAtBetween(int profileId, Instant from, Instant to);
    Slice<DiaryEntry> getAllByProfileIdAndCommitedAtBetween(int profileId, Instant from, Instant to, Pageable pageable);
    Slice<DiaryEntry> getAllByProfileIdAndCommitedAtBefore(int profileId, Instant before, Pageable pageable);
    DiaryEntry findFirstByProfileIdAndCommitedAtBefore(int patientProfileId, Instant before, Sort sort);
}
