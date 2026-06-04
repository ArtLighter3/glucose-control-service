package com.artlighter.glucosecontrolservice.authgateway.controller.user;

import com.artlighter.glucosecontrolservice.authgateway.util.validation.ConvertableValueRangeValidator;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ConvertableValueValidationException;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.dto.PatientProfileDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.user.util.mapper.PatientProfileMapper;
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

@Tag(name = "patient-profile", description = "методы для доступа и модификации профилей больных")
@ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "В случае успеха."),
        @ApiResponse(responseCode = "404", description = "Если профиль больного не был найден.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RestController
@RequestMapping("/api/v1/patients/{userId}/patient-profile")
public class PatientProfileController {
    private PatientProfileService patientProfileService;
    private PatientProfileMapper patientProfileMapper;
    private ConvertableValueRangeValidator convertableValueRangeValidator;

    public PatientProfileController(PatientProfileService patientProfileService,
                                    PatientProfileMapper patientProfileMapper,
                                    ConvertableValueRangeValidator convertableValueRangeValidator) {
        this.patientProfileService = patientProfileService;
        this.patientProfileMapper = patientProfileMapper;
        this.convertableValueRangeValidator = convertableValueRangeValidator;
    }

    @Operation(summary = "Получить профиль больного с его настройками.", description = "Доступно самим " +
            "владельцам профиля с ролью больного и их врачам")
    @GetMapping
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, true, false)")
    public PatientProfileDTO getPatientProfile(@PathVariable int userId) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        return patientProfileMapper.mapToDTO(patientProfile);
    }

    @Operation(summary = "Обновить существующий профиль больного.", description = "Доступно только самим" +
            "владельцам профиля с ролью больного")
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
            content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PutMapping
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, false, false)")
    public PatientProfileDTO putPatientProfile(@PathVariable int userId,
                                               @RequestBody @Valid PatientProfileDTO patientProfileDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "patient profile is invalid");

        //PatientProfile содержит значения, которые передаются в разных единицах измерения в зависимости от настроек
        //больного. Необходима валидация диапазона в зависимости от единицы измерения;
        validateGlucose(patientProfileDTO, bindingResult);

        PatientProfile toUpdate = patientProfileMapper.mapToInternal(patientProfileDTO);

        PatientProfile updated = patientProfileService.updateProfileForPatient(toUpdate, userId);
        return patientProfileMapper.mapToDTO(updated);
    }

    private void validateGlucose(PatientProfileDTO patientProfileDTO, BindingResult bindingResult) {
        //TODO: Выглядит не очень, может стоит переделать структуру валидаций конвертируемых значений;
        boolean check1 = checkGlucoseAndRejectValue(patientProfileDTO.hyperGlucose(), "hyperGlucose",
                patientProfileDTO, bindingResult);
        boolean check2 = checkGlucoseAndRejectValue(patientProfileDTO.highGlucose(), "highGlucose",
                patientProfileDTO, bindingResult);
        boolean check3 = checkGlucoseAndRejectValue(patientProfileDTO.lowGlucose(), "lowGlucose",
                patientProfileDTO, bindingResult);
        boolean check4 = checkGlucoseAndRejectValue(patientProfileDTO.hypoGlucose(), "hypoGlucose",
                patientProfileDTO, bindingResult);

        if (!check1 || !check2 || !check3 || !check4) throw new ValidationIsFailedException(bindingResult);
    }

    private boolean checkGlucoseAndRejectValue(Float value, String fieldName,
                                            PatientProfileDTO patientProfileDTO, BindingResult bindingResult) {
        try {
            convertableValueRangeValidator.isGlucoseValid(value, patientProfileDTO.glucoseUnit());
        } catch (ConvertableValueValidationException ex) {
            bindingResult.rejectValue(fieldName, "not_in_range", ex.getMessage());
            return false;
        }
        return true;
    }
}
