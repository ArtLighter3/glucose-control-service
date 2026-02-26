package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.general.DTOMapper;

import java.time.ZoneOffset;
import java.util.List;

/**
 * Общий интерфейс для мапперов, конвертирующих определенный тип записи дневника из внешних DTO-объектов во
 * внутренние и наоборот.
 * @param <INT> внутренний объект записи дневника определенного типа, наследник DiaryEntry;
 * @param <EXT> внешний DTO-объект записи дневника определенного типа, реализация интерфейса DiaryEntryDTO;
 */
public interface EntryMapper<INT extends DiaryEntry, EXT extends DiaryEntryDTO> extends DTOMapper<INT, EXT> {
    /**
     * Функция конвертирует внешний DTO-объект записи дневника определенного типа во внутренний объект записи дневника
     * определенного типа, при этом совершая необходимые преобразования единиц измерения
     * (на основании настроек профиля пользователя patientProfile) и форматов данных для внутреннего пользования.
     * @param entryDTO внешний DTO-объект записи дневника определенного типа; никогда не null;
     * @param patientProfile профиль пользователя с настройками его выводных и вводных единиц измерения; никогда не
     *                       null;
     * @return DiaryEntry с полностью заполненными данными;
     */
    INT mapToInternalWithUnitConversion(EXT entryDTO, PatientProfile patientProfile);

    /**
     * Функция конвертирует внутренний объект записи дневника определенного типа во внешний DTO-объект записи дневника
     * определенного типа, при этом совершая необходимые преобразования единиц измерения
     * (на основании настроек профиля пользователя patientProfile), форматов данных и времени для вывода пользователю.
     * @param internal внутренний объект записи дневника определенного типа; никогда не null;
     * @param patientProfile профиль пользователя с настройками его выводных и вводных единиц измерения; никогда не
     *                       null;
     * @param outputZoneOffset UTC-смещение для того, чтобы выдать пользователю временные отметки относительно его зоны;
     * @return DiaryEntryDTO с полностью заполненными данными;
     */
    EXT mapToDtoWithUnitConversion(INT internal, PatientProfile patientProfile, ZoneOffset outputZoneOffset);

    List<EXT> mapToDtoCollectionWithUnitConversion(List<DiaryEntry> internal, PatientProfile patientProfile,
                                                   ZoneOffset outputZoneOffset);
}
