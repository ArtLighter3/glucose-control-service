package com.artlighter.glucosecontrolservice.authgateway.controller.calculations;

import com.artlighter.glucosecontrolservice.authgateway.util.validation.ConvertableValueRangeValidator;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ConvertableValueValidationException;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinCalculationRequestDTO;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinResult;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinCalculationService;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Tag(name = "calculations", description = "методы для модификации инсулиновых профилей и для расчетов инсулина, " +
        "подсчета статистики")
@ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "В случае успеха."),
        @ApiResponse(responseCode = "404", description = "Если больной или его инсулиновый профиль не были найдены.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RestController
@SecurityRequirement(name = "sessionAuth")
@RequestMapping("/api/v1/patients/{userId}/insulin")
public class InsulinCalculationController {
    private InsulinCalculationService insulinCalculationService;
    private PatientProfileService patientProfileService;
    private ConvertableValueRangeValidator convertableValueRangeValidator;

    public InsulinCalculationController(InsulinCalculationService insulinCalculationService,
                                        PatientProfileService patientProfileService,
                                        ConvertableValueRangeValidator convertableValueRangeValidator) {
        this.insulinCalculationService = insulinCalculationService;
        this.patientProfileService = patientProfileService;
        this.convertableValueRangeValidator = convertableValueRangeValidator;
    }

    @Operation(summary = "Рассчитать количество единиц инсулина для больного на основе его профиля и записей дневника.",
            description = "Могут учитываться предыдущие вводы инсулина, " +
                    "если указан соответствующий параметр в теле запроса. Доступ только у самого больного.")
    @ApiResponses(value = @ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))))
    @GetMapping("/calculate")
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, false, false)")
    public InsulinResult calculate(@PathVariable int userId,
                                   @Valid InsulinCalculationRequestDTO calculationRequest,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        validateCarbsAndGlucose(calculationRequest, patientProfile, bindingResult);

        //TODO инкапсулировать преобразование, вынести из контроллера
        return insulinCalculationService.calculateInsulinDose(patientProfile,
                Instant.now().atOffset(calculationRequest.patientZoneOffset()).toLocalTime(),
                calculationRequest.considerActiveInsulin(), calculationRequest.correctGlucoseLevel(),
                (float) patientProfile.getCarbsUnit().convertToGrams(calculationRequest.carbs()),
                calculationRequest.glucose() != null ?
                        (float)patientProfile.getGlucoseUnit().convertToMmolPerLiter(calculationRequest.glucose()) : 0f,
                calculationRequest.correction() != null ? calculationRequest.correction() : 0f);
    }

    private void validateCarbsAndGlucose(InsulinCalculationRequestDTO calculationRequest,
                                         PatientProfile patientProfile, BindingResult bindingResult) {
        boolean hasErrors = false;

        try {
            convertableValueRangeValidator.isCarbsValid(calculationRequest.carbs(), patientProfile.getCarbsUnit());
        } catch (ConvertableValueValidationException ex) {
            bindingResult.rejectValue("carbs", "not_in_range", ex.getMessage());
            hasErrors = true;
        }

        if (calculationRequest.glucose() != null) {
            try {
                convertableValueRangeValidator.isGlucoseValid(calculationRequest.glucose(),
                        patientProfile.getGlucoseUnit());
            } catch (ConvertableValueValidationException ex) {
                bindingResult.rejectValue("glucose", "not_in_range", ex.getMessage());
                hasErrors = true;
            }
        }

        if (hasErrors) throw new ValidationIsFailedException(bindingResult);
    }

//    @Operation(summary = "Рассчитать только активный инсулин.",
//            description = "Необходимо право INSULIN_CALCULATE_OWN, " +
//                    "доступ только у самого больного.")
//    @ApiResponses(value = @ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
//            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))))
//    @GetMapping("/calculate-iob")
//    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'INSULIN_CALCULATE_OWN'," +
//            "#userId, authentication)")
//    public Float calculateActiveInsulin(@PathVariable int userId,
//                                        @Parameter(description = "Временная отметка в формате ISO 8601, " +
//                                                "в зависимости от которой " +
//                                                "считать прошлый введенный инсулин.")
//                                        @RequestParam(required = true)
//                                        Instant timestamp) {
//        return insulinCalculationService.calculateActiveInsulin(userId, timestamp);
//    }


}
