package com.artlighter.glucosecontrolservice.calculations.util.mapper;

import com.artlighter.glucosecontrolservice.calculations.dto.InsulinProfileDTO;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinSensitivityFactor;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinToCarbsRatio;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinVolatileValue;
import com.artlighter.glucosecontrolservice.general.DTOMapper;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InsulinProfileMapper implements DTOMapper<InsulinProfile, InsulinProfileDTO> {

    @Override
    public InsulinProfileDTO mapToDTO(InsulinProfile internal) {
        return new InsulinProfileDTO(internal.getDefaultInsulinToCarbsRatio(),
                internal.getDefaultInsulinSensitivityFactor(), internal.getDurationOfInsulinAction(),
                getVolatileValuesMapFromList(internal.getFactorsByTime()),
                getVolatileValuesMapFromList(internal.getRatiosByTime()));
    }

    @Override
    public InsulinProfile mapToInternal(InsulinProfileDTO externalDTO) {
        InsulinProfile internal = new InsulinProfile();

        if (externalDTO.factorsByTime() != null) {
            List<InsulinSensitivityFactor> factorsByTime = new ArrayList<>();
            externalDTO.factorsByTime().forEach((time, value) ->
                    factorsByTime.add(new InsulinSensitivityFactor(value, time, internal)));
            internal.setFactorsByTime(factorsByTime);
        }
        if (externalDTO.ratiosByTime() != null) {
            List<InsulinToCarbsRatio> ratiosByTime = new ArrayList<>();
            externalDTO.ratiosByTime().forEach((time, value) ->
                    ratiosByTime.add(new InsulinToCarbsRatio(value, time, internal)));
            internal.setRatiosByTime(ratiosByTime);
        }

        internal.setDefaultInsulinSensitivityFactor(externalDTO.defaultInsulinSensitivityFactor());
        internal.setDefaultInsulinToCarbsRatio(externalDTO.defaultInsulinToCarbsRatio());
        internal.setDurationOfInsulinAction(externalDTO.durationOfInsulinAction());

        return internal;
    }

    private Map<LocalTime, Float> getVolatileValuesMapFromList(List<? extends InsulinVolatileValue> list) {
        if (list == null) return null;

        return list.stream().collect(Collectors.toMap((value) -> value.getTimeOfDay(),
                value -> value.getValue()));
    }

//    private <T extends InsulinVolatileValue> List<T> getVolatileValuesListFromMap(Map<LocalTime, Float> map,
//                                                                                  Class<T> type) {
//        if (map == null) return null;
//
//        List<T> resultList = new ArrayList<>();
//        map.forEach((time, value) -> resultList.add(type.getDe));
//    }
}
