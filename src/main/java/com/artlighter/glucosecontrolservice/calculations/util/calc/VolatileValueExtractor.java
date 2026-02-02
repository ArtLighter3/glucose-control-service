package com.artlighter.glucosecontrolservice.calculations.util.calc;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinVolatileValue;

import java.time.LocalTime;
import java.util.List;

/**
 * Общий интерфейс для объекта, способного извлечь из списка зависящих от времени суток параметров инсулинового режима
 * подходящее под текущее время суток значение.
 */
public interface VolatileValueExtractor {

    /**
     * Функция возвращает зависящее от времени суток значение параметра инсулинового режима, подходящее под переданное
     * значение. Если подходящего значения нет, либо переданное время LocalTime или список равны null, то возвращает
     * значение по-умолчанию defaultValue.
     * @param valuesByTime список изменяемых по времени суток значений; если null, то вернется defaultValue;
     * @param timeToFindFor время суток, под которое нужно найти значение; если null, то вернется defaultValue;
     * @param defaultValue значение параметра по-умолчанию, которое вернется, если не было найдено значения
     *                     под время суток или какой-то из предыдущих параметров равен null;
     * @return вещественное значение float, являющееся значением изменяемого параметра для данного времени суток;
     */
    float extractVolatileValue(List<? extends InsulinVolatileValue> valuesByTime, LocalTime timeToFindFor,
                               float defaultValue);
}
