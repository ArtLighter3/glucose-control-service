package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.CarbsUnit;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.GlucoseUnit;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Абстрактная реализация EntryMapper, реализующая шаблонный для каждого отдельного типа записи дневника код и
 * предлагающая нижележащим реализациям определить специфические поля преобразуемых объектов. В том числе преобразует
 * выводные числовые значения записей до определенного формата в зависимости от decimalFormat, а также вводные форматы
 * временных отметок, срезая до хранимой точности.
 * @param <INT> внутренний объект записи дневника определенного типа, наследник DiaryEntry;
 * @param <EXT> внешний DTO-объект записи дневника определенного типа, реализация интерфейса DiaryEntryDTO;
 */

public abstract class AbstractEntryMapper<INT extends DiaryEntry, EXT extends DiaryEntryDTO>
        implements EntryMapper<INT, EXT> {
    private final DecimalFormat diaryEntryFloatValueOutputFormat;

    /**
     *
     * @param diaryEntryFloatValueOutputFormat формат, определяющий количество знаков после запятой у
     *                                         выводного значения записи дневника;
     */
    public AbstractEntryMapper(DecimalFormat diaryEntryFloatValueOutputFormat) {
        this.diaryEntryFloatValueOutputFormat = diaryEntryFloatValueOutputFormat;
    }

    @Override
    public INT mapToInternalWithUnitConversion(EXT entryDTO, PatientProfile patientProfile) {
        INT entry = createEntry();

        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt().toInstant().truncatedTo(ChronoUnit.MINUTES));
        entry.setNotes(entryDTO.notes());
        entry.setPatientProfile(patientProfile);

        fillFieldsOfInternalWithUnitConversion(entry, entryDTO, patientProfile);
        //entry.setValue(round(entry.getValue()));
        return entry;
    }

    /**
     * В данной реализации функция преобразует внешний объект во внутренний без преобразования единиц измерения
     * из единиц пользователя в те, с которыми работает логика.
     * Для записи дневника рекомендуется использовать mapToInternalWithUnitConversion(EXT, PatientProfile).
     * @param entryDTO внешний DTO-объект записи дневника определенного типа; никогда не null;
     * @return DiaryEntry с полностью заполненными данными (без конвертирования единиц);
     */
    @Override
    public INT mapToInternal(EXT entryDTO) {
        return mapToInternalWithUnitConversion(entryDTO, createDefaultPatientProfile());
    }

    /**
     * В данной реализации функция преобразует внутренний объект во внешний без преобразования единиц измерения
     * в единицы, выставленные конкретным пользователем, и без преобразования временных отметок относительно смещения.
     * Для записи дневника лучше использовать mapToDtoWithUnitConversion(INT, PatientProfile, ZoneOffset).
     * @param internal внутренний объект записи дневника определенного типа; никогда не null;
     * @return DiaryEntryDTO с полностью заполненными данными (без конвертирования единиц);
     */
    @Override
    public EXT mapToDTO(INT internal) {
        return mapToDtoWithUnitConversion(internal, createDefaultPatientProfile(), ZoneOffset.UTC);
    }

    @Override
    public INT mapToInternal(DiaryEntryDeleteDTO entryDeletionDTO) {
        INT entry = createEntry();
        // При удалении мы не срезаем секунды у отметки. Иначе, если в БД окажется значение
        // с секундами, то его будет невозможно удалить без вмешательства в БД (в ID входит отметка, и отметка без
        // срезанных секунд не будет равна отметке с секундами, запись не будет найдена)
        entry.setCommitedAt(entryDeletionDTO.commitedAt());
        return entry;
    }

    private PatientProfile createDefaultPatientProfile() {
        return new PatientProfile(0, GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS,
                1, 0, 12f, 10f, 4f, 2f);
    }

    /**
     * Функция преобразует переданное число в float с определенным форматом (количество знаков после запятой).
     * @param value числовое значение для конвертации; никогда не null;
     * @return float в преобразованном виде;
     */
    protected float round(Number value) {
        return Float.parseFloat(diaryEntryFloatValueOutputFormat.format(value));
    }

    /**
     * Функция должна заполнять специфические поля записи дневника INT, используя поля EXT, и конвертировать единицы
     * измерения значений при необходимости. Основные поля INT, то есть значение value, дата совершения commitedAt,
     * заметки notes, уже заполнены.
     * @param entry внутренний объект записи дневника определенного типа
     *              с заполненными полями значения value, даты совершения commitedAt и заметок notes;
     * @param entryDTO внешний DTO-объект записи дневника определенного типа, из которого должны браться поля;
     *                 никогда не null;
     * @param patientProfile профиль пользователя с настройками его выводных и вводных единиц измерения; никогда не
     *                       null;
     */
    protected abstract void fillFieldsOfInternalWithUnitConversion(INT entry, EXT entryDTO,
                                                                   PatientProfile patientProfile);

    /**
     * Функция должна создавать экземпляр записи дневника, наследника DiaryEntry.
     * @return экземпляр записи дневника определенного типа DiaryEntry;
     */
    protected abstract INT createEntry();
}
