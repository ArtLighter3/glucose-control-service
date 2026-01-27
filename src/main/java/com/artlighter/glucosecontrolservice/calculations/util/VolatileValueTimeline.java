package com.artlighter.glucosecontrolservice.calculations.util;

import java.time.LocalTime;

public interface VolatileValueTimeline {
    float getFirstValueBeforeTime(LocalTime time);
    void put(float value, LocalTime time);
}
