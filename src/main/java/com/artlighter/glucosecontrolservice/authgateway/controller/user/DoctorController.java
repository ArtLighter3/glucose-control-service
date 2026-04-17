package com.artlighter.glucosecontrolservice.authgateway.controller.user;

import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.dto.AttachedPatientDTO;
import com.artlighter.glucosecontrolservice.user.dto.PatientAttachDetachDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.DoctorProfileService;
import com.artlighter.glucosecontrolservice.user.util.mapper.AttachedPatientMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "doctors", description = "методы для получения и модификации врачей, получения и модификации " +
        "прикрепленных к ним больных")
@ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "В случае успеха."),
        @ApiResponse(responseCode = "404", description = "Врач с таким ID не найден.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RestController
@RequestMapping("/api/v1/doctors/{userId}")
public class DoctorController {
    private DoctorProfileService doctorProfileService;
    private AttachedPatientMapper attachedPatientMapper;

    public DoctorController(DoctorProfileService doctorProfileService, AttachedPatientMapper attachedPatientMapper) {
        this.doctorProfileService = doctorProfileService;
        this.attachedPatientMapper = attachedPatientMapper;
    }

    @Operation(summary = "Получить список прикрепленных к врачу больных.", description = "Возвращает список " +
            "постранично с возможностью сортировки по определенному полю.")
    @GetMapping("/attached-patients")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('DOCTOR') and @resourceAccessInspector.isOwnerOfResource(#userId, authentication))")
    public Page<AttachedPatientDTO> getAttachedPatients(@PathVariable int userId,
                                                        @PageableDefault(sort =
                                                                {"user.lastName", "user.firstName", "user.middleName"})
                                                        @Parameter(description = "Данные о странице и сортировке." +
                                                                "По-умолчанию сортируется " +
                                                                "по ФИО пользователя (возр.)")
                                                        Pageable pageable) {
        Page<PatientProfile> attachedPatients = doctorProfileService.getAttachedPatients(userId, pageable);

        return attachedPatients.map(attachedPatientMapper::mapToDTO);
    }

    @Operation(summary = "Найти прикрепленных к врачу больных по их ФИО.", description = "Возвращает список " +
            "постранично с возможностью сортировки по определенному полю.")
    @GetMapping("/attached-patients/search")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('DOCTOR') and @resourceAccessInspector.isOwnerOfResource(#userId, authentication))")
    public Page<AttachedPatientDTO> getAttachedPatientsBySearchQuery(@PathVariable int userId,
                                                        @RequestParam("query") @Parameter(required = true,
                                                                description = "Поисковая фраза, содержащаяся в ФИО.")
                                                        @Valid @NotBlank
                                                        String query,
                                                        @PageableDefault(sort =
                                                                {"user.lastName", "user.firstName", "user.middleName"})
                                                        @Parameter(description = "Данные о странице и сортировке. " +
                                                                "По-умолчанию сортируется " +
                                                                "по фамилии пользователя (возр.)")
                                                        Pageable pageable) {
        Page<PatientProfile> attachedPatients = doctorProfileService.searchAttachedPatients(userId, query, pageable);

        return attachedPatients.map(attachedPatientMapper::mapToDTO);
    }

    @Operation(summary = "Прикрепить больного к врачу.")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "400", description = "Если тело запроса неверное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Если врач или пациент не найдены.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "409", description = "Если больной уже прикреплен к врачу.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PostMapping("/attached-patients")
    @PreAuthorize("hasRole('ADMIN')")
    public PatientAttachDetachDTO attachPatient(@PathVariable int userId,
                                                @RequestBody @Valid PatientAttachDetachDTO attachDetachDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        doctorProfileService.attachPatientToDoctor(userId, attachDetachDTO.patientId());
        return attachDetachDTO;
    }

    @Operation(summary = "Открепить больного от врача.")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Если врач или пациент не найдены, " +
                            "либо если больной уже откреплен от врача.",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @DeleteMapping("/attached-patients")
    @PreAuthorize("hasRole('ADMIN')")
    public void detachPatient(@PathVariable int userId,
                              @RequestParam @Parameter(required = true, description = "ID пользователя-больного")
                              Integer patientId) {
        doctorProfileService.detachPatientFromDoctor(userId, patientId);
    }
}
