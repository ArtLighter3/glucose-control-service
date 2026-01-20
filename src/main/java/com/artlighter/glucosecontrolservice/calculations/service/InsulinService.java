package com.artlighter.glucosecontrolservice.calculations.service;

import com.artlighter.glucosecontrolservice.calculations.util.InsulinCalculator;
import org.springframework.stereotype.Component;

@Component
public class InsulinService {
    private InsulinCalculator insulinCalculator;

    public InsulinService(InsulinCalculator insulinCalculator) {
        this.insulinCalculator = insulinCalculator;
    }
}
