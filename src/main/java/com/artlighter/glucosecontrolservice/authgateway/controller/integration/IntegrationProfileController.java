package com.artlighter.glucosecontrolservice.authgateway.controller.integration;

import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.integration.dto.IntegrationProfileDTO;
import com.artlighter.glucosecontrolservice.integration.entity.IntegrationProfile;
import com.artlighter.glucosecontrolservice.integration.service.IntegrationProfileService;
import com.artlighter.glucosecontrolservice.integration.util.mapper.IntegrationProfileMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "integration", description = "методы для модификации профилей с настройками интеграции с другими сервисами")
@ApiResponses(value =
        {@ApiResponse(responseCode = "404", description = "Если больной или его профиль интеграций не были найдены.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/api/v1/patients/{userId}/integration-profile")
public class IntegrationProfileController {
    private PatientProfileService patientProfileService;
    private IntegrationProfileService integrationProfileService;
    private IntegrationProfileMapper integrationProfileMapper;

    public IntegrationProfileController(PatientProfileService patientProfileService,
                                        IntegrationProfileService integrationProfileService,
                                        IntegrationProfileMapper integrationProfileMapper) {
        this.patientProfileService = patientProfileService;
        this.integrationProfileService = integrationProfileService;
        this.integrationProfileMapper = integrationProfileMapper;
    }

    @Operation(summary = "Получить профиль интеграций больного.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "В случае успеха."))
    @GetMapping
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, false, false)")
    public IntegrationProfileDTO getIntegrationProfile(@PathVariable int userId) {
        //PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        IntegrationProfile integrationProfile = integrationProfileService.getByPatientProfileId(userId);

        return integrationProfileMapper.mapToDTO(integrationProfile);
    }

    @Operation(summary = "Создать профиль интеграций больного. Профиль интеграций, в отличие от обычного профиля " +
            "больного НЕ создается автоматически, его необходимо перед обновлениями сначала создать")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "Если профиль был успешно создан."),
            @ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Если больной не был найден.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "409", description = "Если инсулиновый профиль для больного уже существует.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @SecurityRequirement(name = "csrf")
    @PostMapping
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, false, false)")
    @ResponseStatus(HttpStatus.CREATED)
    public IntegrationProfileDTO postIntegrationProfile(@PathVariable int userId,
                                               @RequestBody @Valid IntegrationProfileDTO integrationProfileDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult);

        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        IntegrationProfile added = integrationProfileService.createIntegrationProfile(integrationProfileMapper
                        .mapToInternal(integrationProfileDTO), patientProfile.getUserId());

        return integrationProfileMapper.mapToDTO(added);
    }

    @Operation(summary = "Обновить профиль интеграций больного. Профиль интеграций, в отличие от обычного профиля " +
            "больного НЕ создается автоматически, его необходимо перед обновлениями сначала создать")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "В случае успеха."),
            @ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @SecurityRequirement(name = "csrf")
    @PutMapping
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, false, false)")
    public IntegrationProfileDTO putIntegrationProfile(@PathVariable int userId,
                                               @RequestBody @Valid IntegrationProfileDTO integrationProfileDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult);

        //PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        IntegrationProfile updated = integrationProfileService
                .updateIntegrationProfile(integrationProfileMapper.mapToInternal(integrationProfileDTO), userId);

        return integrationProfileMapper.mapToDTO(updated);
    }
}
