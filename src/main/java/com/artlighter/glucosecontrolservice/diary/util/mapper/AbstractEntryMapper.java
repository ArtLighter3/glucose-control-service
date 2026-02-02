package com.artlighter.glucosecontrolservice.diary.util.mapper;

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

public abstract class AbstractEntryMapper<INT extends DiaryEntry, EXT> implements EntryMapper<INT, EXT> {
    private DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

    @Override
    public INT mapToInternalWithUnitConversion(EXT entryDTO, PatientProfile patientProfile) {
        INT entry = createEntry();
        fillFieldsOfInternalWithUnitConversion(entry, entryDTO, patientProfile);
        entry.setCommitedAt(entry.getCommitedAt().truncatedTo(ChronoUnit.MINUTES));
        //entry.setValue(round(entry.getValue()));
        return entry;
    }

    @Override
    public INT mapToInternal(EXT entryDTO) {
        return mapToInternalWithUnitConversion(entryDTO, createDefaultPatientProfile());
    }

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

//    protected DecimalFormat getDecimalFormat() {
//        return decimalFormat;
//    }

    protected float round(Number value) {
        return Float.parseFloat(decimalFormat.format(value));
    }

    /**
     * Функция должна заполнять поля записи дневника INT, используя поля EXT, и конвертировать единицы
     * измерения значений при необходимости.
     * @param entry
     * @param entryDTO
     * @param patientProfile
     */
    protected abstract void fillFieldsOfInternalWithUnitConversion(INT entry, EXT entryDTO,
                                                                   PatientProfile patientProfile);

    /**
     * Функция должна создавать экземпляр записи дневника INT
     * @return экземпляр записи дневника определенного типа
     */
    protected abstract INT createEntry();
}
