package com.artlighter.glucosecontrolservice.user.repository;

import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientProfileRepository
        extends JpaRepository<PatientProfile, Integer> {

    PatientProfile findByUserId(int userId);
    PatientProfile findByUserUsername(String username);
    boolean existsByUserId(int userId);
    void deleteByUserId(int userId);

    @Query("SELECT pp FROM PatientProfile pp JOIN FETCH pp.user u " +
            "JOIN pp.doctors d WHERE d.id = :doctorProfileId")
    Page<PatientProfile> getPatientsAttachedToDoctorByDoctorId(@Param("doctorProfileId") int doctorProfileId,
                                                                  Pageable pageable);

    @Query("SELECT pp FROM PatientProfile pp JOIN FETCH pp.user u " +
            "JOIN pp.doctors d WHERE LOWER(CONCAT(u.lastName, ' ', u.firstName, ' ', COALESCE(u.middleName, ''))) " +
            "LIKE LOWER(CONCAT('%', :searchQuery, '%')) " +
            "AND d.id = :doctorProfileId")
    Page<PatientProfile> searchPatientsAttachedToDoctorByFullName(@Param("doctorProfileId") int doctorProfileId,
                                                                  @Param("searchQuery") String searchQuery,
                                                                  Pageable pageable);

    @Query("SELECT pp FROM PatientProfile pp JOIN FETCH pp.user u " +
            "WHERE LOWER(CONCAT(u.lastName, ' ', u.firstName, ' ', COALESCE(u.middleName, ''))) " +
            "LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Slice<PatientProfile> searchPatientsByFullName(@Param("searchQuery") String searchQuery, Pageable pageable);
}

