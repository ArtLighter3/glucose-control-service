package com.artlighter.glucosecontrolservice.calculations.repository;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsulinProfileRepository extends JpaRepository<InsulinProfile, Integer> {
    InsulinProfile getByProfileId(int profileId);
    boolean existsByProfileId(int profileId);
    void deleteByProfileId(int profileId);
}
