package com.artlighter.glucosecontrolservice.calculations.util.mapper;

import com.artlighter.glucosecontrolservice.calculations.dto.InsulinProfileDTO;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinSensitivityFactor;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinToCarbsRatio;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinVolatileValue;
import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.user.entity.GlucoseUnit;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InsulinProfileMapper implements DTOMapper<InsulinProfile, InsulinProfileDTO> {
    private DecimalFormat decimalOutputFormat;

    public InsulinProfileMapper(DecimalFormat decimalOutputFormat) {
        this.decimalOutputFormat = decimalOutputFormat;
    }

    @Override
    public InsulinProfileDTO mapToDTO(InsulinProfile internal) {
        return mapToDTOWithUnitConversion(internal, GlucoseUnit.MILLIMOLES_PER_LITER);
    }

    @Override
    public InsulinProfile mapToInternal(InsulinProfileDTO externalDTO) {
        return mapToInternalWithUnitConversion(externalDTO, GlucoseUnit.MILLIMOLES_PER_LITER);
    }

    public InsulinProfileDTO mapToDTOWithUnitConversion(InsulinProfile internal, GlucoseUnit glucoseUnit) {
        return new InsulinProfileDTO(Float.valueOf(decimalOutputFormat.format(internal.getDefaultInsulinToCarbsRatio())),
                Float.valueOf(decimalOutputFormat
                        .format(glucoseUnit.convertFromMmolPerLiter(internal.getDefaultInsulinSensitivityFactor()))),
                internal.getDurationOfInsulinAction(),
                getISFValuesMapFromListWithUnitConversion(internal.getFactorsByTime(), glucoseUnit),
                getVolatileValuesMapFromList(internal.getRatiosByTime()));
    }

    public InsulinProfile mapToInternalWithUnitConversion(InsulinProfileDTO externalDTO, GlucoseUnit glucoseUnit) {
        InsulinProfile internal = new InsulinProfile();

        if (externalDTO.factorsByTime() != null) {
            List<InsulinSensitivityFactor> factorsByTime = new ArrayList<>();
            externalDTO.factorsByTime().forEach((time, value) ->
                    factorsByTime.add(new InsulinSensitivityFactor(
                            (float) glucoseUnit.convertToMmolPerLiter(value),
                            time,
                            internal)));
            internal.setFactorsByTime(factorsByTime);
        }
        if (externalDTO.ratiosByTime() != null) {
            List<InsulinToCarbsRatio> ratiosByTime = new ArrayList<>();
            externalDTO.ratiosByTime().forEach((time, value) ->
                    ratiosByTime.add(new InsulinToCarbsRatio(value, time, internal)));
            internal.setRatiosByTime(ratiosByTime);
        }

        internal.setDefaultInsulinSensitivityFactor((float)
                glucoseUnit.convertToMmolPerLiter(externalDTO.defaultInsulinSensitivityFactor()));
        internal.setDefaultInsulinToCarbsRatio(externalDTO.defaultInsulinToCarbsRatio());
        internal.setDurationOfInsulinAction(externalDTO.durationOfInsulinAction());

        return internal;
    }

    private Map<LocalTime, Float> getVolatileValuesMapFromList(List<? extends InsulinVolatileValue> list) {
        if (list == null) return null;

        return list.stream().collect(Collectors.toMap((value) -> value.getTimeOfDay(),
                value -> Float.valueOf(decimalOutputFormat.format(value.getValue()))));
    }

    private Map<LocalTime, Float> getISFValuesMapFromListWithUnitConversion(List<InsulinSensitivityFactor> list,
                                                          GlucoseUnit glucoseUnit) {
        if (list == null) return null;

        return list.stream().collect(Collectors.toMap((value) -> value.getTimeOfDay(),
                value ->
                        Float.valueOf(decimalOutputFormat
                                .format(glucoseUnit.convertFromMmolPerLiter(value.getValue())))));
    }

//    private <T extends InsulinVolatileValue> List<T> getVolatileValuesListFromMap(Map<LocalTime, Float> map,
//                                                                                  Class<T> type) {
//        if (map == null) return null;
//
//        List<T> resultList = new ArrayList<>();
//        map.forEach((time, value) -> resultList.add(type.getDe));
//    }
}
