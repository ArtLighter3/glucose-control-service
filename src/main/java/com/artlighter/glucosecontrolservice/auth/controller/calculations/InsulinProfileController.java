package com.artlighter.glucosecontrolservice.auth.controller.calculations;

import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinProfileDTO;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinProfileService;
import com.artlighter.glucosecontrolservice.calculations.util.mapper.InsulinProfileMapper;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "calculations", description = "методы для модификации инсулиновых профилей и для расчетов инсулина, " +
        "подсчета статистики")
@ApiResponses(value =
        {@ApiResponse(responseCode = "404", description = "Если больной или его инсулиновый профиль не были найдены.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RestController
@RequestMapping("/api/v1/patients/{userId}/insulin-profile")
public class InsulinProfileController {
    private PatientProfileService patientProfileService;
    private InsulinProfileService insulinProfileService;
    private InsulinProfileMapper insulinProfileMapper;

    public InsulinProfileController(PatientProfileService patientProfileService,
                                    InsulinProfileService insulinProfileService,
                                    InsulinProfileMapper insulinProfileMapper) {
        this.patientProfileService = patientProfileService;
        this.insulinProfileService = insulinProfileService;
        this.insulinProfileMapper = insulinProfileMapper;
    }

    @Operation(summary = "Получить инсулиновый профиль больного.", description = "Для доступа к своему профилю" +
            "необходимо право INSULIN_PROFILE_SHOW_OWN; для доступа к профилям прикрепленных больных - " +
            "INSULIN_PROFILE_SHOW_ATTACHED; для доступа к профилям всех больных - INSULIN_PROFILE_SHOW_ALL")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "В случае успеха."))
    @GetMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('INSULIN_PROFILE_SHOW_ALL', " +
            "'INSULIN_PROFILE_SHOW_ATTACHED', 'INSULIN_PROFILE_SHOW_OWN', #userId, authentication)")
    public InsulinProfileDTO getInsulinProfile(@PathVariable int userId) {
       // PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        InsulinProfile insulinProfile = insulinProfileService.getByPatientProfileId(userId);

        return insulinProfileMapper.mapToDTO(insulinProfile);
    }

    @Operation(summary = "Создать инсулиновый профиль больного.", description = "Для создания своего профиля" +
            "необходимо право INSULIN_PROFILE_ADD_OWN; для создания профиля прикрепленных больных - " +
            "INSULIN_PROFILE_ADD_ATTACHED; для создания профиля любого больного - INSULIN_PROFILE_ADD_ALL")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "Если профиль был успешно создан."),
            @ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Если больной не был найден.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "409", description = "Если инсулиновый профиль для больного уже существует.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PostMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('INSULIN_PROFILE_ADD_ALL', " +
            "'INSULIN_PROFILE_ADD_ATTACHED', 'INSULIN_PROFILE_ADD_OWN', #userId, authentication)")
    @ResponseStatus(HttpStatus.CREATED)
    public InsulinProfileDTO postInsulinProfile(@PathVariable int userId,
                                               @RequestBody @Valid InsulinProfileDTO insulinProfileDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "insulin profile is invalid");

        //Здесь все же проверяется наличие профиля пациента, чтобы пользователь получил нашу
        //NOT FOUND ошибку, а не от базы данных, при попытке создания для несуществующего профиля пациента
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        insulinProfileService.createInsulinProfile(insulinProfileMapper.mapToInternal(insulinProfileDTO),
                patientProfile.getUserId());
        return insulinProfileDTO;
    }

    @Operation(summary = "Обновить инсулиновый профиль больного.", description = "Для обновления своего профиля" +
            "необходимо право INSULIN_PROFILE_UPDATE_OWN; для обновления профиля прикрепленных больных - " +
            "INSULIN_PROFILE_UPDATE_ATTACHED; для обновления профиля любого больного - INSULIN_PROFILE_UPDATE_ALL")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "В случае успеха."),
            @ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PutMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('INSULIN_PROFILE_UPDATE_ALL', " +
            "'INSULIN_PROFILE_UPDATE_ATTACHED', 'INSULIN_PROFILE_UPDATE_OWN', #userId, authentication)")
    public InsulinProfileDTO putInsulinProfile(@PathVariable int userId,
                                               @RequestBody @Valid InsulinProfileDTO insulinProfileDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "insulin profile is invalid");

        //PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        InsulinProfile updated = insulinProfileService
                .updateInsulinProfile(insulinProfileMapper.mapToInternal(insulinProfileDTO), userId);
        return insulinProfileMapper.mapToDTO(updated);
    }
}
