package com.artlighter.glucosecontrolservice.calculations.dto;

import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "RecentActivity",
        description = "Различная информация и расчеты по недавней активности по дневнику больного")
public record RecentActivityDTO(
        @Schema(description = "Недавние записи дневника (24 часа с запрошенного момента)")
        List<DiaryEntry> recentEntries,
        @Schema(description = "Последняя запись измерения глюкозы. В отличие от recentEntries, может быть датирована " +
                "хоть годы назад. Может быть null, если записей нет.")
        GlucoseEntryDTO lastGlucoseEntry,
        @Schema(description = "Активный инсулин, рассчитанный относительно запрошенного момента. " +
                "Может быть null, если не удалось рассчитать из-за отсутствия инсулинового профиля.")
        Float activeInsulin
) {
}
