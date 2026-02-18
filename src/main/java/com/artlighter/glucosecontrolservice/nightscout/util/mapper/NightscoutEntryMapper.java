package com.artlighter.glucosecontrolservice.nightscout.util.mapper;

import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.nightscout.dto.NightscoutEntryDTO;
import com.artlighter.glucosecontrolservice.user.entity.GlucoseUnit;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class NightscoutEntryMapper implements DTOMapper<GlucoseEntry, NightscoutEntryDTO> {
    @Override
    public NightscoutEntryDTO mapToDTO(GlucoseEntry internal) {
        return new NightscoutEntryDTO("sgv", internal.getCommitedAt().toString(),
                internal.getCommitedAt().toEpochMilli(), internal.getValue());
    }

    @Override
    public GlucoseEntry mapToInternal(NightscoutEntryDTO externalDTO) {
        GlucoseEntry glucoseEntry = new GlucoseEntry();

        glucoseEntry.setValue(GlucoseUnit.MILLIGRAMS_PER_DECILITER.convertToMmolPerLiter(externalDTO.sgv()));
        glucoseEntry.setCommitedAt(Instant.parse(externalDTO.dateString()));

        return new GlucoseEntry();
    }
}
