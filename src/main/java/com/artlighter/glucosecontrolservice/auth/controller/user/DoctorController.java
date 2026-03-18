package com.artlighter.glucosecontrolservice.auth.controller.user;

import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "doctors", description = "методы для получения и модификации врачей, получения и модификации" +
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
            "постранично с возможностью сортировки по определенному полю. Для доступа к списку своих больных требует" +
            "право ATTACHED_PATIENT_SHOW_OWN. Для доступа ко всем - ATTACHED_PATIENT_SHOW_ALL.")
    @GetMapping("/attached-patients")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('ATTACHED_PATIENT_SHOW_ALL', " +
            "null, 'ATTACHED_PATIENT_SHOW_OWN', #userId, authentication)")
    public Page<AttachedPatientDTO> getAttachedPatients(@PathVariable int userId,
                                                        @PageableDefault(sort = "user.username")
                                                        @Parameter(description = "Данные о странице и сортировке." +
                                                                "По-умолчанию сортируется " +
                                                                "по имени пользователя (возр.)")
                                                        Pageable pageable) {
        Page<PatientProfile> attachedPatients = doctorProfileService.getAttachedPatients(userId, pageable);

        return attachedPatients.map(attachedPatientMapper::mapToDTO);
    }

    @Operation(summary = "Найти прикрепленных к врачу больных по их ФИО.", description = "Возвращает список " +
            "постранично с возможностью сортировки по определенному полю. Для доступа к списку своих больных требует " +
            "право ATTACHED_PATIENT_SHOW_OWN. Для доступа ко всем - ATTACHED_PATIENT_SHOW_ALL.")
    @GetMapping("/attached-patients/search")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('ATTACHED_PATIENT_SHOW_ALL', " +
            "null, 'ATTACHED_PATIENT_SHOW_OWN', #userId, authentication)")
    public Page<AttachedPatientDTO> getAttachedPatientsBySearchQuery(@PathVariable int userId,
                                                        @RequestParam("query") @Parameter(required = true,
                                                                description = "Поисковая фраза, содержащаяся в ФИО.")
                                                        String query,
                                                        @PageableDefault(sort = "user.username")
                                                        @Parameter(description = "Данные о странице и сортировке. " +
                                                                "По-умолчанию сортируется " +
                                                                "по имени пользователя (возр.)")
                                                        Pageable pageable) {
        Page<PatientProfile> attachedPatients = doctorProfileService.searchAttachedPatients(userId, query, pageable);

        return attachedPatients.map(attachedPatientMapper::mapToDTO);
    }

    @Operation(summary = "Прикрепить больного к врачу.", description = "Для прикрепления больного необходимо " +
            "право ATTACHED_PATIENT_ATTACH_DETACH.")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "400", description = "Если тело запроса неверное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Если врач или пациент не найдены.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "409", description = "Если больной уже прикреплен к врачу.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PostMapping("/attached-patients")
    @PreAuthorize("hasAuthority('ATTACHED_PATIENT_ATTACH_DETACH')")
    public PatientAttachDetachDTO attachPatient(@PathVariable int userId,
                                                @RequestBody @Valid PatientAttachDetachDTO attachDetachDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        doctorProfileService.attachPatientToDoctor(userId, attachDetachDTO.patientId());
        return attachDetachDTO;
    }

    @Operation(summary = "Открепить больного от врача.", description = "Для открепления больного необходимо " +
            "право ATTACHED_PATIENT_ATTACH_DETACH.")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Если врач или пациент не найдены, " +
                            "либо если больной уже откреплен от врача.",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @DeleteMapping("/attached-patients")
    @PreAuthorize("hasAuthority('ATTACHED_PATIENT_ATTACH_DETACH')")
    public void detachPatient(@PathVariable int userId,
                              @RequestParam @Parameter(required = true, description = "ID пользователя больного")
                              Integer patientId) {
        doctorProfileService.detachPatientFromDoctor(userId, patientId);
    }
}
