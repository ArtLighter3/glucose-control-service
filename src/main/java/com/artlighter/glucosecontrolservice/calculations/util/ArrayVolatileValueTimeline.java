package com.artlighter.glucosecontrolservice.calculations.util;

import java.time.LocalTime;

public class ArrayVolatileValueTimeline implements VolatileValueTimeline{
    private float[] values;

    @Override
    public float getFirstValueBeforeTime(LocalTime time) {
        return 0;
    }

    @Override
    public void put(float value, LocalTime time) {

    }


    ArrayVolatileValueTimeline(int arraySize) {
        this.values = new float[arraySize];
    }

}
