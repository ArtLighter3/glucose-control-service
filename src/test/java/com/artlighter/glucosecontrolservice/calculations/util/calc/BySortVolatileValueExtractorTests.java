package com.artlighter.glucosecontrolservice.calculations.util.calc;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinSensitivityFactor;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinToCarbsRatio;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinVolatileValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import(BySortVolatileValueExtractor.class)
public class BySortVolatileValueExtractorTests {
    @Autowired
    private BySortVolatileValueExtractor volatileValueExtractor;

    @Test
    public void extractVolatileValue_SomeOfArgumentsAreNullOrListIsEmpty_ReturnsDefaultValue() {
        test(null, LocalTime.now(), 10.2f, 10.2f);
        test(createValuesByTime(List.of(LocalTime.of(2, 5)), List.of(20f), InsulinToCarbsRatio.class),
                null, 10.2f, 10.2f);

        test(null, LocalTime.now().minusHours(4), 4.44f, 4.44f);
        test(createValuesByTime(List.of(LocalTime.of(15, 0)), List.of(1.5f),
                        InsulinSensitivityFactor.class), null, 4.44f, 4.44f);

        test(null, null, 4.44f, 4.44f);

        test(List.of(), LocalTime.now(), 2.55f, 2.55f);
    }

    @Test
    public void extractVolatileValue_ListHasDifferentValuesButTimeOfDayIsNotAfterAnyOfThem_ReturnsDefaultValue() {
        test(createValuesByTime(List.of(
                                LocalTime.of(15, 30),
                                LocalTime.of(18, 0),
                                LocalTime.of(12, 0)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(11, 0), 50f, 50f);

        test(createValuesByTime(List.of(
                                LocalTime.of(13, 30),
                                LocalTime.of(11, 0),
                                LocalTime.of(16, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(10, 30), 78.2f, 78.2f);

        test(createValuesByTime(List.of(
                                LocalTime.of(17, 0),
                                LocalTime.of(7, 0),
                                LocalTime.of(8, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(5, 0), 85f, 85f);

        test(createValuesByTime(List.of(
                                LocalTime.of(13, 30),
                                LocalTime.of(11, 0),
                                LocalTime.of(16, 30),
                                LocalTime.of(17, 30),
                                LocalTime.of(22, 0)),
                        List.of(13f, 8f, 6f, 50f, 25.2f),
                        InsulinSensitivityFactor.class),
                LocalTime.of(10, 59), 78.2f, 78.2f);

        test(createValuesByTime(List.of(
                                LocalTime.of(17, 0),
                                LocalTime.of(7, 0),
                                LocalTime.of(8, 30),
                                LocalTime.of(15, 30),
                                LocalTime.of(5, 30)),
                        List.of(13f, 8f, 6f, 50f, 25.2f),
                        InsulinSensitivityFactor.class),
                LocalTime.of(5, 0), 85f, 85f);
    }

    @Test
    public void extractVolatileValue_ListHasDifferentValuesAndTimeOfDayIsAfterOneOfThem_ReturnsCorrectValueByTime() {
        test(createValuesByTime(List.of(
                                LocalTime.of(15, 30),
                                LocalTime.of(18, 0),
                                LocalTime.of(12, 0)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(16, 0), 50f, 13f);

        test(createValuesByTime(List.of(
                                LocalTime.of(15, 30),
                                LocalTime.of(18, 0),
                                LocalTime.of(12, 0)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(13, 0), 50f, 6f);

        test(createValuesByTime(List.of(
                                LocalTime.of(13, 30),
                                LocalTime.of(11, 0),
                                LocalTime.of(16, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(17, 30), 78.2f, 6f);

        test(createValuesByTime(List.of(
                                LocalTime.of(13, 30),
                                LocalTime.of(11, 0),
                                LocalTime.of(16, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(13, 0), 78.2f, 8f);

        test(createValuesByTime(List.of(
                                LocalTime.of(17, 0),
                                LocalTime.of(7, 0),
                                LocalTime.of(8, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(8, 30), 85f, 6f);

        test(createValuesByTime(List.of(
                                LocalTime.of(17, 0),
                                LocalTime.of(7, 0),
                                LocalTime.of(8, 30)),
                        List.of(13f, 8f, 6f),
                        InsulinToCarbsRatio.class),
                LocalTime.of(17, 0), 85f, 13f);

        test(createValuesByTime(List.of(
                                LocalTime.of(13, 30),
                                LocalTime.of(11, 0),
                                LocalTime.of(16, 30),
                                LocalTime.of(17, 30),
                                LocalTime.of(22, 0)),
                        List.of(13f, 8f, 6f, 50f, 25.2f),
                        InsulinSensitivityFactor.class),
                LocalTime.of(13, 29), 10f, 8f);

        test(createValuesByTime(List.of(
                                LocalTime.of(17, 0),
                                LocalTime.of(7, 0),
                                LocalTime.of(8, 30),
                                LocalTime.of(15, 30),
                                LocalTime.of(5, 30)),
                        List.of(13f, 8f, 6f, 50f, 25.2f),
                        InsulinSensitivityFactor.class),
                LocalTime.of(16, 30), 11f, 50f);
    }

    private void test(List<? extends InsulinVolatileValue> valuesByTime, LocalTime timeToFindFor, float defaultValue,
                      float expected) {
        assertEquals(expected, volatileValueExtractor.extractVolatileValue(valuesByTime, timeToFindFor, defaultValue));
    }

    private <T extends InsulinVolatileValue> List<T> createValuesByTime(List<LocalTime> times,
                                                                        List<Float> values,
                                                                        Class<T> type) {
        if (times == null || values == null || times.size() != values.size())
            return Collections.emptyList();

        List<T> result = new ArrayList<>(times.size());
        for (int i = 0; i < times.size(); i++) {
            try {
                result.add(type.getDeclaredConstructor(float.class, LocalTime.class, InsulinProfile.class)
                        .newInstance(values.get(i), times.get(i), null));
            } catch (Exception ignored) {}
        }

        return result;
    }
}
