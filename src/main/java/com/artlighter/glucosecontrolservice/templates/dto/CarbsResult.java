package com.artlighter.glucosecontrolservice.templates.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.CarbsUnit;

public record CarbsResult(
        Float overallCarbs,
        CarbsUnit carbsUnit
) {
}
