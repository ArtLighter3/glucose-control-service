package com.artlighter.glucosecontrolservice.authgateway.util;

import com.artlighter.glucosecontrolservice.authgateway.util.exception.ConvertableValueValidationException;
import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;
import com.artlighter.glucosecontrolservice.user.entity.GlucoseUnit;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import org.springframework.stereotype.Component;

/**
 * Класс используется для валидации диапазонов входных значений пользователей, которые могут меняться в зависимости
 * от выбранной пользователем единицы измерения этого значения.
 * <p>
 * Валидирующие аннотации, как в других случаях валидации, не используются, чтобы не загружать по несколько раз профиль
 * пациента: при валидации и при основной логике (профиль чаще нужен, чем нет), когда можно сделать это один раз и
 * передать профиль как сюда, так и в логику.
 * @see ConvertableValueValidationException
 */
@Component
public class ConvertableValueRangeValidator {
    /**
     * Проверяет, входит ли значение в диапазон допустимых значений глюкозы в зависимости от единицы измерения, заданной
     *  в профиле больного. Не возвращает логическое значение, а выкидывает исключение!
     * @param value значение глюкозы;
     * @param glucoseUnit единицы измерения глюкозы, заданные пользователем;
     * @throws ConvertableValueValidationException если валидация не была пройдена;
     * @throws IllegalArgumentException если glucoseUnit равен null;
     */
    public void isGlucoseValid(double value, GlucoseUnit glucoseUnit) {
        if (glucoseUnit == null) throw new IllegalArgumentException("glucoseUnit cannot be null");

        double min = glucoseUnit.convertFromMmolPerLiter(0.5);
        double max = glucoseUnit.convertFromMmolPerLiter(40.0);

        if (!isValid(value, min, max)) throw new ConvertableValueValidationException(value, min, max);
    }

    /**
     * Проверяет, входит ли значение в диапазон допустимых значений углеводов в зависимости от единицы измерения,
     * заданной в профиле больного. Не возвращает логическое значение, а выкидывает исключение!
     * @param value значение углеводов;
     * @param carbsUnit единицы измерения углеводов, заданные пользователем;
     * @throws ConvertableValueValidationException если валидация не была пройдена;
     * @throws IllegalArgumentException если carbsUnit равен null;
     */
    public void isCarbsValid(double value, CarbsUnit carbsUnit) {
        if (carbsUnit == null) throw new IllegalArgumentException("carbsUnit cannot be null");

        double min = carbsUnit.convertFromGrams(0.1);
        double max = carbsUnit.convertFromGrams(300.0);

        if (!isValid(value, min, max)) throw new ConvertableValueValidationException(value, min, max);
    }

    private boolean isValid(double value, double min, double max) {
        return value >= min && value <= max;
    }
//
//    private void checkArguments(Number value, Number min, Number max) {
//        if (value == null) throw new IllegalArgumentException("value cannot be null");
//        if (min == null) throw new IllegalArgumentException("min cannot be null");
//        if (max == null) throw new IllegalArgumentException("max cannot be null");
//    }
}
