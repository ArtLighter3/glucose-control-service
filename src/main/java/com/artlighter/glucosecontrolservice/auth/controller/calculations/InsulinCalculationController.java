package com.artlighter.glucosecontrolservice.auth.controller.calculations;

import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinCalculationRequestDTO;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinResult;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinProfileService;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinCalculationService;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "insulin-calculations", description = "методы для модификации инсулиновых профилей и для расчетов инсулина")
@ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "В случае успеха."),
        @ApiResponse(responseCode = "404", description = "Если больной или его инсулиновый профиль не были найдены.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RestController
@RequestMapping("/api/v1/patients/{userId}/insulin")
public class InsulinCalculationController {
    private PatientProfileService patientProfileService;
    private InsulinProfileService insulinProfileService;
    private InsulinCalculationService insulinCalculationService;
    private DiaryEntryService diaryEntryService;

    public InsulinCalculationController(PatientProfileService patientProfileService,
                                        InsulinProfileService insulinProfileService,
                                        InsulinCalculationService insulinCalculationService,
                                        DiaryEntryService diaryEntryService) {
        this.patientProfileService = patientProfileService;
        this.insulinProfileService = insulinProfileService;
        this.insulinCalculationService = insulinCalculationService;
        this.diaryEntryService = diaryEntryService;
    }

    @Operation(summary = "Рассчитать количество единиц инсулина для больного на основе его профиля и записей дневника.",
            description = "Могут учитываться предыдущие вводы инсулина, " +
                    "если указан соответствующий параметр в теле запроса. Необходимо право INSULIN_CALCULATE_OWN, " +
                    "доступ только у самого больного.")
    @ApiResponses(value = @ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))))
    @GetMapping("/calculate")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'INSULIN_CALCULATE_OWN'," +
            "#userId, authentication)")
    public InsulinResult calculate(@Valid InsulinCalculationRequestDTO calculationRequest, BindingResult bindingResult,
                                   @PathVariable int userId) {
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        InsulinProfile insulinProfile = insulinProfileService.getByPatientProfileId(patientProfile.getId());
        if (insulinProfile == null) throw new ResourceNotFoundException("insulin profile not found");

        Instant now = Instant.now();
        List<DiaryEntry> insulinEntries = diaryEntryService.getDiaryEntriesOfType(DiaryEntryType.INSULIN_ENTRY,
                patientProfile, now.minus(Duration.ofHours(12)), now);

        return insulinCalculationService.calculateInsulinDose(insulinProfile, null,
                calculationRequest.localTimeOfDay(), calculationRequest.carbs(),
                calculationRequest.glucose() != null ? calculationRequest.glucose() : 0f,
                calculationRequest.correction() != null ? calculationRequest.correction() : 0f,
                patientProfile.getHighGlucose());
    }
}
