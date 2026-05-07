package com.artlighter.glucosecontrolservice.authgateway.controller.diary;

import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.general.dto.CustomSliceMetadata;
import com.artlighter.glucosecontrolservice.general.dto.CustomSlicedModel;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.EntryMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Общий абстрактный класс для контроллеров, обслуживающих точки доступа к определенному
 * типу записи дневника самоконтроля. Нужен для того, чтобы для каждого типа записи дневника (глюкоза, инсулин и т.д.)
 * не переписывать одинаковую логику доступа и проверки доступа.
 * Содержит методы для добавления (POST), обновления (PUT), получения (GET), удаления (DELETE).
 * <p>
 * Реализации могут не специфицировать авторизацию, она здесь уже объявлена
 * для каждого метода, но они должны указывать аннотации @RequestMapping с передачей имени типа
 * записи дневника (например, @GetMapping("/glucose")),
 * по которому будет производиться доступ. Без указания будет использоваться "/default".
 * Для перечисленных методов реализации могут просто вызвать метод родителя.
 * @param <INT> внутренний класс сущности, представляющей тип записи дневника (наследник DiaryEntry),
 *             к которому относится реализация.
 * @param <EXT> внешний класс сущности (DTO) определенного типа записи дневника
 *             для передачи вовне или приема извне (наследник DiaryEntryDTO).
 * @see DiaryEntry
 * @see DiaryEntryDTO
 */
@Tag(name = "diary", description = "методы для ведения дневника самоконтроля: " +
        "добавление, модификация записей разных типов")
@ApiResponses(value =
        {@ApiResponse(responseCode = "404", description = "Если больной не был найден.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RequestMapping("api/v1/patients/{userId}/entries")
//@CrossOrigin(origins = "http://localhost:5173")
public abstract class AbstractDiaryEntryController<INT extends DiaryEntry, EXT extends DiaryEntryDTO> {
    //TODO мб убрать зависимости из абстрактного класса и просто сделать абстрактные геттеры, как в модуле templates?
    protected DiaryEntryService diaryEntryService;
    protected PatientProfileService patientProfileService;
    protected EntryMapper<INT, EXT> entryMapper;
    protected Logger log = LoggerFactory.getLogger(AbstractDiaryEntryController.class);

    /**
     *
     * @param diaryEntryService сервис для доступа к самим записям дневника
     * @param patientProfileService сервис для доступа к профилям больных
     * @param entryMapper маппер сущностей из внутренних (INT) во внешние DTO (EXT) и наоборот
     */
    public AbstractDiaryEntryController(DiaryEntryService diaryEntryService,
                                        PatientProfileService patientProfileService,
                                        EntryMapper<INT, EXT> entryMapper) {
        this.diaryEntryService = diaryEntryService;
        this.patientProfileService = patientProfileService;
        this.entryMapper = entryMapper;
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "В случае успеха.")})
    @GetMapping("/default")
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, true, false)")
    public CustomSlicedModel<EXT> getDiaryEntries(@PathVariable int userId,
                                     @RequestParam(required = false)
                                     @Parameter(description = "Нижняя граница временного периода выборки записей.")
                                     Instant from,
                                     @RequestParam(required = false)
                                     @Parameter(description = "Верхняя граница временного периода выборки записей.")
                                     Instant to,
                                     @RequestParam(required = false) @Parameter(description = "UTC-смещение, к" +
                                             " которому будут преобразованы временные отметки записей на выходе. " +
                                             "Если не указано, то результаты показываются по UTC+0.")
                                     ZoneOffset outputZoneOffset,
                                     @PageableDefault(size = 20, page = 0,
                                             sort = "commitedAt", direction = Sort.Direction.DESC)
                                     @Parameter(description = "Данные о странице и сортировке. " +
                                                 "По-умолчанию сортируется по дате совершения (убыв.)")
                                     Pageable pageable) {
        return getEntries(getEntryType(), userId, from, to, outputZoneOffset, pageable);
    }

    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "Если запись была успешно создана."),
            @ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "409", description = "Если запись этого типа с этой временной отметкой" +
                    " для этого пользователя уже существует.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PostMapping("/default")
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, false, false)")
    @ResponseStatus(HttpStatus.CREATED)
    public EXT postDiaryEntry(@PathVariable int userId, @RequestBody @Valid EXT entryDTO,
                               BindingResult bindingResult) {
        return addEntry(userId, entryDTO, bindingResult);
    }

    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "В случае успеха."),
            @ApiResponse(responseCode = "400", description = "Если тело запроса некорректное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Если больной или обновляемая запись не были найдены.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PutMapping("/default")
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, false, false)")
    public EXT putDiaryEntry(@PathVariable int userId, @RequestBody @Valid EXT entryDTO,
                                    BindingResult bindingResult) {
        return updateEntry(userId, entryDTO, bindingResult);
    }

    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Запись удалена, либо ее и не существовало."),
            @ApiResponse(responseCode = "400", description = "Если параметры запроса некорректны.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @DeleteMapping("/default")
    @PreAuthorize("@resourceAccessInspector.hasAccessToPatientResource(#userId, authentication, false, false)")
    public void deleteDiaryEntry(@PathVariable int userId,
                                 @RequestParam @Parameter(required = true,
                                         description = "Временная отметка записи этого типа, которую надо удалить.")
                                 Instant commitedAt) {
        deleteEntry(userId, commitedAt);
    }

    private EXT addEntry(int userId, EXT entryDTO, BindingResult bindingResult) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        checkValidationErrorsAndThrowException(bindingResult);
        doAdditionalValidation(bindingResult, entryDTO, patientProfile);

        INT entryToAdd = entryMapper.mapToInternalWithUnitConversion(entryDTO, patientProfile);
        //log.info("timestamp of entry = {}", entryToAdd.getCommitedAt());
        diaryEntryService.addDiaryEntry(entryToAdd, patientProfile.getUserId(), entryToAdd.getCommitedAt());

        return entryDTO;
    }

    private EXT updateEntry(int userId, EXT entryDTO, BindingResult bindingResult) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        checkValidationErrorsAndThrowException(bindingResult);
        doAdditionalValidation(bindingResult, entryDTO, patientProfile);

        INT entryToUpdate = entryMapper.mapToInternalWithUnitConversion(entryDTO, patientProfile);
        diaryEntryService.updateDiaryEntry(entryToUpdate, patientProfile.getUserId(), entryToUpdate.getCommitedAt());

        return entryDTO;
    }

    private void deleteEntry(int userId, Instant commitedAt) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        diaryEntryService.deleteDiaryEntry(getEntryType(), patientProfile.getUserId(), commitedAt);
    }

//    protected void checkUserId(int userId, ServiceUserDetails userDetails) {
//        if (userId != userDetails.getId())
//            throw new NotCurrentUsersInfoException("You don't have access to this user's info");
//    }

    private CustomSlicedModel<EXT> getEntries(DiaryEntryType entryType, int userId,
                                              Instant from, Instant to, ZoneOffset outputZoneOffset,
                                              Pageable pageable) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

        Slice<DiaryEntry> entries = diaryEntryService
                .getDiaryEntriesOfType(entryType, patientProfile.getUserId(), from, to, pageable);

        return new CustomSlicedModel<EXT>(entryMapper
                .mapToDtoCollectionWithUnitConversion(entries.getContent(), patientProfile, outputZoneOffset),
                new CustomSliceMetadata(entries.getSize(), entries.getNumber(), entries.hasNext()));
    }

    /**
     * Функция возвращает тип записи дневника DiaryEntryType, с которым работает контроллер.
     * @return экземпляр перечисления DiaryEntryType.
     */
    protected abstract DiaryEntryType getEntryType();

    private void checkValidationErrorsAndThrowException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationIsFailedException(bindingResult, "Validation of request body failed");
        }
    }

    /**
     * Функция может использоваться для проведения дополнительных проверок входного объекта, если ее переопределить
     * в наследнике-контроллере. Проверки производятся только при добавлении (POST) и обновлении (PUT) записи дневника.
     * Ничего не возвращает и должна выбрасывать исключения при провалах для их дальнейшей обработки в ControllerAdvice.
     * @param bindingResult объект с ошибками валидации, полученными до этого метода;
     * @param patientProfile профиль больного;
     * @param entryDTO входной объект;
     */
    protected void doAdditionalValidation(BindingResult bindingResult, EXT entryDTO, PatientProfile patientProfile) {

    }
}
