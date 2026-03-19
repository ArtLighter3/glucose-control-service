package com.artlighter.glucosecontrolservice.auth.controller.calculations;

import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinCalculationRequestDTO;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinResult;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinProfileService;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinCalculationService;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
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
import java.time.LocalTime;
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
    private InsulinCalculationService insulinCalculationService;

    public InsulinCalculationController(InsulinCalculationService insulinCalculationService) {
        this.insulinCalculationService = insulinCalculationService;
    }

    @Operation(summary = "Рассчитать количество единиц инсулина для больного на основе его профиля и записей дневника.",
            description = "Могут учитываться предыдущие вводы инсулина, " +
                    "если указан соответствующий параметр в теле запроса. Необходимо право INSULIN_CALCULATE_OWN, " +
                    "доступ только у самого больного.")
    @ApiResponses(value = @ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))))
    @GetMapping("/calculate")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'INSULIN_CALCULATE_OWN'," +
            "#userId, authentication)")
    public InsulinResult calculate(@PathVariable int userId,
                                   @Valid InsulinCalculationRequestDTO calculationRequest,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        return insulinCalculationService.calculateInsulinDose(userId,
                calculationRequest.localTimeOfDay(),
                calculationRequest.considerActiveInsulin(), calculationRequest.correctGlucoseLevel(),
                calculationRequest.carbs(),
                calculationRequest.glucose() != null ? calculationRequest.glucose() : 0f,
                calculationRequest.correction() != null ? calculationRequest.correction() : 0f);
    }

    @Operation(summary = "Рассчитать только активный инсулин.",
            description = "Необходимо право INSULIN_CALCULATE_OWN, " +
                    "доступ только у самого больного.")
    @ApiResponses(value = @ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))))
    @GetMapping("/calculate-iob")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'INSULIN_CALCULATE_OWN'," +
            "#userId, authentication)")
    public Float calculateActiveInsulin(@PathVariable int userId,
                                        @Parameter(description = "Временная отметка в формате ISO 8601, " +
                                                "в зависимости от которой " +
                                                "считать прошлый введенный инсулин.")
                                        @RequestParam(required = true)
                                        Instant timestamp) {
        return insulinCalculationService.calculateActiveInsulin(userId, timestamp);
    }
}
