package com.artlighter.glucosecontrolservice.auth.controller.calculations;

import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinCalculationRequestDTO;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinResult;
import com.artlighter.glucosecontrolservice.calculations.dto.RecentActivityDTO;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinCalculationService;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
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
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
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

    public StatisticsController(InsulinCalculationService insulinCalculationService,
                                PatientProfileService patientProfileService, DiaryEntryService diaryEntryService,
                                GlucoseEntryMapper glucoseEntryMapper) {
        this.insulinCalculationService = insulinCalculationService;
        this.patientProfileService = patientProfileService;
        this.diaryEntryService = diaryEntryService;
        this.glucoseEntryMapper = glucoseEntryMapper;
    }

    @Operation(summary = "Получить и рассчитать информацию о " +
            "недавней активности больного для сводок на главных страницах.",
            description = "Если у больного не найден инсулиновый профиль, то активный инсулин рассчитан не будет")
    @ApiResponses(value = @ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))))
    @GetMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'ACTIVITY_SHOW_OWN'," +
            "#userId, authentication)")
    public RecentActivityDTO getRecentActivity(@PathVariable int userId,
                                        @Parameter(description = "Временная отметка в формате ISO 8601, " +
                                                "в зависимости от которой рассчитывать активность.")
                                        @RequestParam(required = true)
                                        Instant timestamp) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        List<DiaryEntry> recentEntries = diaryEntryService.getAllDiaryEntries(patientProfile,
                timestamp.minus(Duration.ofHours(24)), timestamp);

        GlucoseEntryDTO lastGlucoseEntry = null;
        DiaryEntry lastEntry = diaryEntryService.findLastEntryOfType(DiaryEntryType.GLUCOSE_ENTRY, patientProfile);
        if (lastEntry instanceof GlucoseEntry)
            lastGlucoseEntry = glucoseEntryMapper.mapToDTO((GlucoseEntry) lastEntry);

        Float activeInsulin = null;
        try {
            activeInsulin = insulinCalculationService.calculateActiveInsulin(patientProfile, recentEntries, timestamp);
        } catch (ResourceNotFoundException ignored) {}

        return new RecentActivityDTO(recentEntries, lastGlucoseEntry, activeInsulin);
    }


}
