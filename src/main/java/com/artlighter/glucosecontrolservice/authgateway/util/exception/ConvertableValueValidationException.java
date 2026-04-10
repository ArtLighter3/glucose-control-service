package com.artlighter.glucosecontrolservice.authgateway.util.exception;

/**
 * Исключение выбрасывается при провале валидации значения, диапазон которого зависит от единицы измерения,
 * заданной пользователем
 */
public class ConvertableValueValidationException extends RuntimeException {
    private Number min, max, actual;

    public ConvertableValueValidationException(Number actual, Number min, Number max, String message) {
        super(message);
        this.actual = actual;
        this.min = min;
        this.max = max;
    }

    public ConvertableValueValidationException(Number actual, Number min, Number max) {
        this(actual, min, max,
                String.format("value should be in range from %.2f to %.2f", min.floatValue(), max.floatValue()));
    }
}
