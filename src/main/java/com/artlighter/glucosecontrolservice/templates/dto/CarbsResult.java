package com.artlighter.glucosecontrolservice.templates.dto;

import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;

public record CarbsResult(
        Float overallCarbs,
        CarbsUnit carbsUnit
) {
}
