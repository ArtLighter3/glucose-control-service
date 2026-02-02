package com.artlighter.glucosecontrolservice.calculations.util.calc;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinVolatileValue;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

/**
 * Реализация VolatileValueExtractor, находящая изменяемое от времени суток значение с помощью сортировки списка
 * значений.
 * @see VolatileValueExtractor
 */
@Component
public class BySortVolatileValueExtractor implements VolatileValueExtractor {
    @Override
    public float extractVolatileValue(List<? extends InsulinVolatileValue> valuesByTime, LocalTime timeToFindFor,
                                      float defaultValue) {
        if (valuesByTime == null || valuesByTime.isEmpty() || timeToFindFor == null)
            return defaultValue;

        // Хотя загрузка из БД у этих сущностей уже отсортирована по времени суток, на всякий случай делается это снова
        // (да и этот класс о предварительной сортировке знать не должен),
        // тем более, как заявляется, при уже отсортированном списке сложность будет почти O(n).
        valuesByTime.sort(Comparator.comparing((InsulinVolatileValue value) -> value.getTimeOfDay()));

        for (int i = valuesByTime.size() - 1; i >= 0; i--) {
            InsulinVolatileValue insulinVolatileValue = valuesByTime.get(i);
            if (timeToFindFor.isAfter(insulinVolatileValue.getTimeOfDay()) ||
                    timeToFindFor.equals(insulinVolatileValue.getTimeOfDay()))
                return insulinVolatileValue.getValue();
        }

        return defaultValue;
    }
}
