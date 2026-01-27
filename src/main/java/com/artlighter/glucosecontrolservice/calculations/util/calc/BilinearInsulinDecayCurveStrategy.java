package com.artlighter.glucosecontrolservice.calculations.util.calc;

import org.springframework.stereotype.Component;

/**
 * Реализация графика кривой выведения инсулина, представляющая билинейный график
 * с вычисляемой отметкой пика активности.
 */
@Component
public class BilinearInsulinDecayCurveStrategy implements InsulinDecayCurveStrategy {
    @Override
    public double getCurrentActiveInsulin(float initialInsulin,
                                         int minutesPassedFromAdministration,
                                         int durationOfInsulinAction) {
        int defaultDia = 3;
        int defaultPeakTime = 75;
        int defaultEndTime = defaultDia * 60;

        double timeScalar = (double) defaultDia / durationOfInsulinAction;
        double scaledMinsAgo = timeScalar * minutesPassedFromAdministration;

//        double peakTime = 2.0 / (durationOfInsulinAction * 60.0);
//        double slopeUp = peakTime / defaultPeakTime;
//        double slopeDown = -1 * (peakTime / (defaultEndTime - defaultPeakTime));

        if (scaledMinsAgo < defaultPeakTime) {
            double x1 = (scaledMinsAgo / 5.0) + 1.0;
            return initialInsulin * ((-0.001852*x1*x1) + (0.001852*x1) + 1.0);
        } else if (scaledMinsAgo < defaultEndTime) {
            double x2 = ((scaledMinsAgo - defaultPeakTime) / 5.0);
            return initialInsulin * ((0.001323*x2*x2) + (-0.054233*x2) + 0.555560);
        }

        return 0.0;
    }
}
