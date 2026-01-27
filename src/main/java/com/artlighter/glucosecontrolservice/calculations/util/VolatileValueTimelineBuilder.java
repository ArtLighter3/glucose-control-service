package com.artlighter.glucosecontrolservice.calculations.util;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinVolatileValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

public class VolatileValueTimelineBuilder {
    @Value("${calculations.insulin-profile.volatile-values-division-factor:48}")
    public static final int DIVISION_FACTOR = 48;

//    public static VolatileValueTimeline from(List<? extends InsulinVolatileValue> volatileValues) {
//
//    }
    public static VolatileValueTimeline build() {
        return new ArrayVolatileValueTimeline(DIVISION_FACTOR);
    }
}
