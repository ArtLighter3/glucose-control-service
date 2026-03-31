package com.artlighter.glucosecontrolservice.calculations.util;

import java.time.Duration;

public enum TimeInterval {
    DAY(Duration.ofHours(24)),
    WEEK(Duration.ofDays(7)),
    MONTH(Duration.ofDays(30)),;

    private Duration duration;

    TimeInterval(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
