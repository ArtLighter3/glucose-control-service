package com.artlighter.glucosecontrolservice.templates.util.mapper;

import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;
import com.artlighter.glucosecontrolservice.templates.dto.MealDTO;
import com.artlighter.glucosecontrolservice.templates.entity.Meal;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import org.springframework.stereotype.Component;

@Component
public class MealMapper implements TemplateMapper<Meal, MealDTO> {

    @Override
    public MealDTO mapToDTO(Meal internal) {
        return mapToDtoWithUnitConversion(internal, CarbsUnit.GRAMS);
    }

    @Override
    public Meal mapToInternal(MealDTO externalDTO) {
        //TODO решить, стоит ли принимать кол-во углеводов на 100 г тоже в единицах пользователя или только в граммах.
        //Просто может быть удобнее на входе передавать в граммах независимо от выставленных единиц, ведь именно граммы
        //написаны на упаковках. Хотя еда необязательно упаковочная...
        return new Meal(new PatientTemplateEntity.PatientTemplateEntityID(0, externalDTO.name()),
                externalDTO.carbsPer100Grams());
    }

    public MealDTO mapToDtoWithUnitConversion(Meal internal, CarbsUnit patientCarbsUnit) {
        return new MealDTO(internal.getId().getName(),
                (float) patientCarbsUnit.convertFromGrams(internal.getCarbsPer100Grams()));
    }

}
