package com.artlighter.glucosecontrolservice.calculations.dto;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "RecentActivity",
        description = "Различная информация и расчеты по недавней активности по дневнику больного")
public record RecentActivityDTO(
        @Schema(description = "Недавние записи дневника (24 часа, 7 дней или 30 дней с запрошенного момента)")
        List<DiaryEntryDTO> recentEntries,
        @Schema(description = "Последняя запись измерения глюкозы. В отличие от recentEntries, может быть датирована " +
                "хоть годы назад. Может быть null, если записей нет.")
        GlucoseEntryDTO lastGlucoseEntry,
        @Schema(description = "Активный инсулин, рассчитанный относительно запрошенного момента. " +
                "Может быть null, если не удалось рассчитать из-за отсутствия инсулинового профиля.")
        Float activeInsulin/*,
        @Schema(description = "Углеводы, принятые от начала дня. Может быть null, если не был передан outputZoneOffset " +
                "в запросе.")
        Float carbsOfDay,
        @Schema(description = "Единицы измерения значения углеводов в carbsOfDay")
        CarbsUnit carbsUnit*/
) {
}
