package com.artlighter.glucosecontrolservice.templates.service.impl;

import com.artlighter.glucosecontrolservice.templates.entity.Meal;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import com.artlighter.glucosecontrolservice.templates.repository.MealRepository;
import com.artlighter.glucosecontrolservice.templates.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Сервис для выборки и модификации заготовок-блюд больного.
 */
@Service
public class MealService extends TemplateService<Meal> {

    @Autowired
    public MealService(MealRepository mealRepository) {
        super(mealRepository);
    }

    /**
     * Рассчитывает общее количество принимаемых углеводов на основе весов передаваемых блюд.
     * Если по названию блюдо не было найдено в списках блюд больного, оно
     * не используется для общего расчета.
     * @param patientProfileId ID профиля больного;
     * @param mealWeights словарь, в котором ключом является наименование блюда, а значение - вес блюда при приеме пищи.
     * @return вещ. значение, обозначающее общее количество принимаемых углеводов в граммах;
     */
    @Transactional(readOnly = true)
    public float calculateOverallCarbs(int patientProfileId, Map<String, Float> mealWeights) {
        if (mealWeights == null || mealWeights.isEmpty()) return 0f;

        List<PatientTemplateEntity.PatientTemplateEntityID> ids = new ArrayList<>();
        for (String mealName : mealWeights.keySet())
            ids.add(new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, mealName));

        List<Meal> meals = patientTemplateEntityRepository.findAllById(ids);

        float result = 0f;
        for (Meal meal : meals)
            result += mealWeights.getOrDefault(meal.getId().getName(), 0f) / 100f
                    * meal.getCarbsPer100Grams();

        return result;
    }
}
