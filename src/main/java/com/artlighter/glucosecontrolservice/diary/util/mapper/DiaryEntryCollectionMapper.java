package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.*;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.*;
import com.artlighter.glucosecontrolservice.general.DTOMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

@Component
public class DiaryEntryCollectionMapper {
    private GlucoseEntryMapper glucoseEntryMapper;
    private InsulinEntryMapper insulinEntryMapper;
    private CarbsEntryMapper carbsEntryMapper;
    private MedicationEntryMapper medicationEntryMapper;

    public DiaryEntryCollectionMapper(GlucoseEntryMapper glucoseEntryMapper, InsulinEntryMapper insulinEntryMapper,
                                      CarbsEntryMapper carbsEntryMapper, MedicationEntryMapper medicationEntryMapper) {
        this.glucoseEntryMapper = glucoseEntryMapper;
        this.insulinEntryMapper = insulinEntryMapper;
        this.carbsEntryMapper = carbsEntryMapper;
        this.medicationEntryMapper = medicationEntryMapper;
    }

    public List<DiaryEntryDTO> mapToDTO(List<DiaryEntry> internalEntries, PatientProfile patientProfile,
                                        ZoneOffset outputZoneOffset) {
        ZoneOffset actualOffset = outputZoneOffset == null ? ZoneOffset.UTC : outputZoneOffset;

        return internalEntries.stream()
                .map((entry) -> convert(entry, patientProfile, actualOffset))
                .toList();
    }

    public List<DiaryEntry> mapToInternal(List<DiaryEntryDTO> externalEntries, PatientProfile patientProfile) {

        return externalEntries.stream()
                .map((entryDTO) -> convert(entryDTO, patientProfile))
                .toList();
    }

    private DiaryEntryDTO convert(DiaryEntry entry, PatientProfile patientProfile, ZoneOffset outputZoneOffset) {
        return switch (entry) {
            case GlucoseEntry glucoseEntry ->
                    glucoseEntryMapper.mapToDtoWithUnitConversion(glucoseEntry, patientProfile, outputZoneOffset);
            case InsulinEntry insulinEntry ->
                    insulinEntryMapper.mapToDtoWithUnitConversion(insulinEntry, patientProfile, outputZoneOffset);
            case CarbsEntry carbsEntry ->
                    carbsEntryMapper.mapToDtoWithUnitConversion(carbsEntry, patientProfile, outputZoneOffset);
            case MedicationEntry medicationEntry ->
                    medicationEntryMapper.mapToDtoWithUnitConversion(medicationEntry, patientProfile, outputZoneOffset);
            default -> null;
        };
    }

    private DiaryEntry convert(DiaryEntryDTO entryDTO, PatientProfile patientProfile) {
        return switch (entryDTO) {
            case GlucoseEntryDTO glucoseEntryDTO ->
                    glucoseEntryMapper.mapToInternalWithUnitConversion(glucoseEntryDTO, patientProfile);
            case InsulinEntryDTO insulinEntryDTO ->
                    insulinEntryMapper.mapToInternalWithUnitConversion(insulinEntryDTO, patientProfile);
            case CarbsEntryDTO carbsEntryDTO ->
                    carbsEntryMapper.mapToInternalWithUnitConversion(carbsEntryDTO, patientProfile);
            case MedicationEntryDTO medicationEntryDTO ->
                    medicationEntryMapper.mapToInternalWithUnitConversion(medicationEntryDTO, patientProfile);
            default -> null;
        };
    }
}
