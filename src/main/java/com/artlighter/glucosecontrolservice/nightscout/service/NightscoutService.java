package com.artlighter.glucosecontrolservice.nightscout.service;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.nightscout.dto.NightscoutEntryDTO;
import com.artlighter.glucosecontrolservice.nightscout.dto.NightscoutTreatmentDTO;
import com.artlighter.glucosecontrolservice.nightscout.util.mapper.NightscoutEntryMapper;
import com.artlighter.glucosecontrolservice.nightscout.util.mapper.NightscoutTreatmentMapper;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NightscoutService {
    private NightscoutEntryMapper nightscoutEntryMapper;
    private NightscoutTreatmentMapper nightscoutTreatmentMapper;
    private DiaryEntryService diaryEntryService;

    public NightscoutService(NightscoutEntryMapper nightscoutEntryMapper,
                             NightscoutTreatmentMapper nightscoutTreatmentMapper,
                             DiaryEntryService diaryEntryService) {
        this.nightscoutEntryMapper = nightscoutEntryMapper;
        this.nightscoutTreatmentMapper = nightscoutTreatmentMapper;
        this.diaryEntryService = diaryEntryService;
    }

    public List<NightscoutEntryDTO> addGlucoseEntries(List<NightscoutEntryDTO> entries, PatientProfile patientProfile,
                                                      boolean updateIfExists) {
        List<DiaryEntry> diaryEntriesToAdd = new ArrayList<>();

        for (NightscoutEntryDTO entry : entries) {
            GlucoseEntry glucoseEntry = nightscoutEntryMapper.mapToInternal(entry);
            glucoseEntry.setPatientProfile(patientProfile);
            diaryEntriesToAdd.add(glucoseEntry);
        }

        List<DiaryEntry> rejected =
                diaryEntryService.addDiaryEntries(diaryEntriesToAdd, patientProfile, updateIfExists);

        return rejected.stream()
                .filter((rejectedEntry) -> rejectedEntry instanceof GlucoseEntry)
                .map((rejectedEntry) -> nightscoutEntryMapper.mapToDTO((GlucoseEntry) rejectedEntry))
                .toList();
       // return Collections.emptyList();
    }

    public List<NightscoutTreatmentDTO> addTreatments(List<NightscoutTreatmentDTO> treatments,
                                                      PatientProfile patientProfile, boolean updateIfExists) {
        List<DiaryEntry> diaryEntriesToAdd = new ArrayList<>();

        for (NightscoutTreatmentDTO treatment : treatments) {
            List<DiaryEntry> dividedDiaryEntries = nightscoutTreatmentMapper.mapToInternal(treatment);

            for (DiaryEntry dividedDiaryEntry : dividedDiaryEntries) {
                dividedDiaryEntry.setPatientProfile(patientProfile);
                diaryEntriesToAdd.add(dividedDiaryEntry);
            }
        }

        List<DiaryEntry> rejected = diaryEntryService.addDiaryEntries(diaryEntriesToAdd, patientProfile, updateIfExists);

//        return rejected.stream()
//                .filter((rejectedEntry) -> rejectedEntry instanceof GlucoseEntry)
//                .map((rejectedEntry) -> nightscoutEntryMapper.mapToDTO((GlucoseEntry) rejectedEntry))
//                .toList();
        return Collections.emptyList();
    }

//    private boolean isCorrect(NightscoutEntryDTO entry) {
//
//        return true;
//    }
}
