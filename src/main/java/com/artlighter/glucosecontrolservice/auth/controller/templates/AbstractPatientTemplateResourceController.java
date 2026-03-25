package com.artlighter.glucosecontrolservice.auth.controller.templates;

import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.templates.dto.PatientTemplateEntityDTO;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import com.artlighter.glucosecontrolservice.templates.service.TemplateService;
import com.artlighter.glucosecontrolservice.templates.util.mapper.TemplateMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Общий абстрактный класс для контроллеров, обслуживающих точки доступа к ресурсу-заготовке для быстрого заполнения
 * записей дневника (для подробностей см. PatientTemplateEntity).
 * Нужен для того, чтобы для каждого типа заготовки, сколько бы их ни было в будущем,
 * не переписывать одинаковую логику доступа и проверки доступа.
 * Содержит методы для добавления, обновления, получения всех, получения одного по имени, получения списка по
 * поисковому запросу, удаления.
 * <p>
 * Реализации могут не специфицировать авторизацию, она здесь уже объявлена
 * для каждого метода, но они должны указывать аннотации @RequestMapping с передачей имени типа
 * записи дневника (для метода поиска с добавлением "/search")
 * (например, @GetMapping("/meals"), @GetMapping("meals/search")),
 * по которому будет производиться доступ. Без указания будет использоваться "/defaults".
 * Для метода поиска "/defaults/search"
 * Для перечисленных методов реализации могут просто вызвать метод родителя либо использовать представленные protected
 * методы, возвращающие внутренние объекты для доп. логики.
 * <p>
 * Реализации должны самостоятельно принимать и хранить сервисы и маппер, реализуя для них геттеры.
 * @param <INT> внутренний класс сущности, представляющей тип заготовки (наследник PatientTemplateEntity),
 *             к которому относится реализация.
 * @param <EXT> внешний класс сущности (DTO) определенного типа заготовки
 *             для передачи вовне или приема извне (наследник PatientTemplateEntityDTO).
 * @see PatientTemplateEntity
 * @see PatientTemplateEntityDTO
 */
@Tag(name = "templates", description = "методы для получения, " +
        "модификации личных заготовок больного (блюд, препаратов), а также для расчета общего количества")
@ApiResponses(value =
        {@ApiResponse(responseCode = "404", description = "Если больной не был найден.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RequestMapping("/api/v1/patients/{userId}/templates/")
public abstract class AbstractPatientTemplateResourceController
        <INT extends PatientTemplateEntity, EXT extends PatientTemplateEntityDTO> {

//    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_SHOW_OWN', " +
//            "#userId, authentication)")
//    @GetMapping("/default-by-name")
//    public EXT getTemplate(@PathVariable int userId,
//                            @RequestParam String name) {
//        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);
//
//        INT template = getTemplateService().getByName(patientProfile.getId(), name);
//
//        return getTemplateMapper().mapToDTO(template);
//    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "В случае успеха.")})
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_SHOW_OWN', " +
            "#userId, authentication)")
    @GetMapping("/defaults")
    public Page<EXT> getTemplates(@PathVariable int userId,
                                  @PageableDefault(size = 10, page = 0, sort = "id.name")
                                  @Parameter(description = "Данные о странице и сортировке. " +
                                          "По-умолчанию сортируется по наименованию (возр.)")
                                  Pageable pageable) {
        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        Page<INT> templates = getTemplateService().getAllByPatientProfileId(patientProfile.getUserId(), pageable);

        return templates.map(getTemplateMapper()::mapToDTO);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "В случае успеха.")})
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_SHOW_OWN', " +
            "#userId, authentication)")
    @GetMapping("/defaults/search")
    public Page<EXT> getTemplatesBySearchQuery(@PathVariable int userId,
                                               @RequestParam @Parameter(required = true,
                                                       description = "Поисковая фраза, содержащаяся в наименовании.")
                                               String query,
                                               @PageableDefault(size = 10, page = 0, sort = "id.name")
                                               @Parameter(description = "Данные о странице и сортировке. " +
                                                       "По-умолчанию сортируется по наименованию (возр.)")
                                               Pageable pageable) {
        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        Page<INT> templates = getTemplateService().searchByNameQuery(patientProfile.getUserId(), query, pageable);

        return templates.map(getTemplateMapper()::mapToDTO);
    }

    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "Если заготовка была успешно создана."),
            @ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "409", description = "Если заготовка этого типа с этим именем" +
                            "для этого пользователя уже существует.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_ADD_OWN', " +
            "#userId, authentication)")
    @PostMapping("/defaults")
    @ResponseStatus(HttpStatus.CREATED)
    public EXT postTemplate(@PathVariable int userId, @RequestBody @Valid EXT template, BindingResult bindingResult) {
        INT added = post(userId, template, bindingResult);
        return getTemplateMapper().mapToDTO(added);
    }

    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "В случае успеха."),
            @ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Если больной или обновляемая заготовка не были найдены.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_UPDATE_OWN', " +
            "#userId, authentication)")
    @PutMapping("/defaults")
    public EXT putTemplate(@PathVariable int userId, @RequestBody @Valid EXT template, BindingResult bindingResult) {
        INT updated = update(userId, template, bindingResult);
        return getTemplateMapper().mapToDTO(updated);
    }

    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Заготовка удалена, либо ее и не существовало."),
            @ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_DELETE_OWN', " +
            "#userId, authentication)")
    @DeleteMapping("/defaults")
    public void deleteTemplate(@PathVariable int userId, @RequestParam String name) {
        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        getTemplateService().deleteFromPatient(patientProfile.getUserId(), name);
    }

    protected INT post(int userId, EXT template, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        INT toAdd = getTemplateMapper().mapToInternal(template);
        return getTemplateService().addToPatient(toAdd, patientProfile.getUserId(), toAdd.getId().getName());
    }

    protected INT update(int userId, EXT template, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        INT toUpdate = getTemplateMapper().mapToInternal(template);
        return getTemplateService().update(toUpdate, patientProfile.getUserId(), toUpdate.getId().getName());
    }

    protected PatientProfile getPatientProfileOrThrowException(int userId) {
        return getPatientProfileService().getByUserId(userId);
    }

   // protected abstract EXT mapToDTO(INT internal);
    protected abstract TemplateService<INT> getTemplateService();
    protected abstract PatientProfileService getPatientProfileService();
    protected abstract TemplateMapper<INT, EXT> getTemplateMapper();
}
