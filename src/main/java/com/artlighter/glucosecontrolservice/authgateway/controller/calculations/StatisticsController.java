package com.artlighter.glucosecontrolservice.authgateway.controller.calculations;

import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.calculations.dto.RecentActivityDTO;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinCalculationService;
import com.artlighter.glucosecontrolservice.calculations.util.TimeInterval;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.DiaryEntryCollectionMapper;
import com.artlighter.glucosecontrolservice.diary.util.mapper.GlucoseEntryMapper;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Tag(name = "calculations", description = "методы для модификации инсулиновых профилей и для расчетов инсулина, " +
        "подсчета статистики")
@ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "В случае успеха."),
        @ApiResponse(responseCode = "404", description = "Если больной не был найден.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RestController
@RequestMapping("/api/v1/patients/{userId}/recent")
public class StatisticsController {
    private InsulinCalculationService insulinCalculationService;
    private PatientProfileService patientProfileService;
    private DiaryEntryService diaryEntryService;
    private GlucoseEntryMapper glucoseEntryMapper;
    private DiaryEntryCollectionMapper diaryEntryCollectionMapper;

    public StatisticsController(InsulinCalculationService insulinCalculationService,
                                PatientProfileService patientProfileService, DiaryEntryService diaryEntryService,
                                GlucoseEntryMapper glucoseEntryMapper,
                                DiaryEntryCollectionMapper diaryEntryCollectionMapper) {
        this.insulinCalculationService = insulinCalculationService;
        this.patientProfileService = patientProfileService;
        this.diaryEntryService = diaryEntryService;
        this.glucoseEntryMapper = glucoseEntryMapper;
        this.diaryEntryCollectionMapper = diaryEntryCollectionMapper;
    }

    @Operation(summary = "Получить и рассчитать информацию о " +
            "недавней активности больного для сводок на главных страницах.",
            description = "Если у больного не найден инсулиновый профиль, то активный инсулин рассчитан не будет")
    @ApiResponses(value = @ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))))
    @GetMapping
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, true, false)")
    public RecentActivityDTO getRecentActivity(@PathVariable int userId,
                                               @Parameter(description = "UTC-смещение, к которому будут " +
                                                "преобразованы временные отметки записей дневника, а " +
                                                "также на основе которого будут рассчитаны углеводы за день. " +
                                                "Если не указать, то отметки записей будут по UTC+0", required = false)
                                               @RequestParam(required = false)
                                               ZoneOffset outputZoneOffset,
                                               @Parameter(description = "Интервал, по которому собираются недавние " +
                                                       "записи дневника (24 часа, 7 дней или 30 дней). " +
                                                       "Если не указан, то данные собираются за 24 часа.",
                                                       required = false)
                                               @RequestParam(required = false)
                                               TimeInterval interval) {
        //TODO поместить в сервис
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        Instant timestamp = Instant.now();
        List<DiaryEntry> recentEntries = diaryEntryService.getAllDiaryEntries(patientProfile.getUserId(),
                timestamp.minus(interval != null ? interval.getDuration() : TimeInterval.DAY.getDuration()), timestamp);

        GlucoseEntryDTO lastGlucoseEntry = null;
        DiaryEntry lastEntry =
                diaryEntryService.findLastEntryOfType(DiaryEntryType.GLUCOSE_ENTRY, patientProfile.getUserId());
        if (lastEntry instanceof GlucoseEntry)
            lastGlucoseEntry = glucoseEntryMapper.mapToDtoWithUnitConversion((GlucoseEntry) lastEntry,
                    patientProfile, outputZoneOffset);

        Float activeInsulin = null;
        try {
            activeInsulin = insulinCalculationService.calculateActiveInsulin(patientProfile, recentEntries, timestamp);
        } catch (ResourceNotFoundException ignored) {}

//        Float carbsOfDay = outputZoneOffset != null ?
//                calculateCarbs(recentEntries,
//                        timestamp.atOffset(outputZoneOffset).toLocalDate().atStartOfDay().toInstant(outputZoneOffset))
//                : null;

        return new RecentActivityDTO(diaryEntryCollectionMapper
                .mapToDTO(recentEntries, patientProfile, outputZoneOffset), lastGlucoseEntry, activeInsulin/*,
                carbsOfDay, patientProfile.getCarbsUnit()*/);
    }


//
//    private float calculateCarbs(List<? extends DiaryEntry> entries, Instant localStartOfDay) {
//        float totalCarbs = 0f;
//
//        for (DiaryEntry entry : entries) {
//            if (entry instanceof CarbsEntry carbsEntry) {
//                if (carbsEntry.getCommitedAt().isAfter(localStartOfDay) ||
//                        carbsEntry.getCommitedAt().equals(localStartOfDay))
//                    totalCarbs += carbsEntry.getValue();
//            }
//        }
//
//        return totalCarbs;
//    }

}
