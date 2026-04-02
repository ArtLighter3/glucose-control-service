package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.*;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;
//TODO какой-то странной структуры класс
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

    public List<DiaryEntryWithTypeDTO> mapToDTO(List<DiaryEntry> internalEntries, PatientProfile patientProfile,
                                                ZoneOffset outputZoneOffset) {
        ZoneOffset actualOffset = outputZoneOffset == null ? ZoneOffset.UTC : outputZoneOffset;

        return internalEntries.stream()
                .map((entry) -> convert(entry, patientProfile, actualOffset))
                .toList();
    }

    public List<DiaryEntry> mapToInternal(List<DiaryEntryWithTypeDTO> externalEntries,
                                          PatientProfile patientProfile) {

        return externalEntries.stream()
                .map((entryDTO) -> convert(entryDTO.entryInfo(), patientProfile))
                .toList();
    }

    private DiaryEntryWithTypeDTO convert(DiaryEntry entry,
                                          PatientProfile patientProfile, ZoneOffset outputZoneOffset) {
        return switch (entry) {
            case GlucoseEntry glucoseEntry -> new DiaryEntryWithTypeDTO(DiaryEntryType.GLUCOSE_ENTRY,
                    glucoseEntryMapper.mapToDtoWithUnitConversion(glucoseEntry, patientProfile, outputZoneOffset));
            case InsulinEntry insulinEntry -> new DiaryEntryWithTypeDTO(DiaryEntryType.INSULIN_ENTRY,
                    insulinEntryMapper.mapToDtoWithUnitConversion(insulinEntry, patientProfile, outputZoneOffset));
            case CarbsEntry carbsEntry -> new DiaryEntryWithTypeDTO(DiaryEntryType.CARBS_ENTRY,
                    carbsEntryMapper.mapToDtoWithUnitConversion(carbsEntry, patientProfile, outputZoneOffset));
            case MedicationEntry medicationEntry -> new DiaryEntryWithTypeDTO(DiaryEntryType.MEDICATION_ENTRY,
                    medicationEntryMapper.mapToDtoWithUnitConversion(medicationEntry, patientProfile, outputZoneOffset));
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
