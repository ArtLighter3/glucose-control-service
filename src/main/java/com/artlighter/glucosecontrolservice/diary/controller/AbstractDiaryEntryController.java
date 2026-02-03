package com.artlighter.glucosecontrolservice.diary.controller;

import com.artlighter.glucosecontrolservice.auth.util.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.diary.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.DiaryEntryCollectionMapper;
import com.artlighter.glucosecontrolservice.diary.util.mapper.EntryMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

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
 *             для передачи вовне или приема извне.
 */
@RequestMapping("api/patients/{userId}/entries")
public abstract class AbstractDiaryEntryController<INT extends DiaryEntry, EXT extends DiaryEntryDTO> {
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

    @GetMapping("/default")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('GLUCOSE_SHOW_ALL', 'GLUCOSE_SHOW_ATTACHED', " +
            "'GLUCOSE_SHOW_OWN', #userId, authentication)")
    public List<EXT> getDiaryEntries(@PathVariable int userId,
                                     @RequestParam(required = false) Instant from,
                                     @RequestParam(required = false) Instant to,
                                     @RequestParam(required = false) ZoneOffset outputZoneOffset) {
        return getEntries(getEntryType(), userId, from, to, outputZoneOffset);
    }

    @PostMapping("/default")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('GLUCOSE_ADD_ALL', 'GLUCOSE_ADD_ATTACHED', " +
            "'GLUCOSE_ADD_OWN', #userId, authentication)")
    @ResponseStatus(HttpStatus.CREATED)
    public EXT postDiaryEntry(@PathVariable int userId, @RequestBody @Valid EXT entryDTO,
                               BindingResult bindingResult) {
        return addEntry(userId, entryDTO, bindingResult);
    }

    @PutMapping("/default")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('GLUCOSE_UPDATE_ALL', " +
            "'GLUCOSE_UPDATE_ATTACHED','GLUCOSE_UPDATE_OWN', #userId, authentication)")
    public EXT putDiaryEntry(@PathVariable int userId, @RequestBody @Valid EXT entryDTO,
                                    BindingResult bindingResult) {
        return updateEntry(userId, entryDTO, bindingResult);
    }

    @DeleteMapping("/default")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('GLUCOSE_DELETE_ALL', " +
            "'GLUCOSE_DELETE_ATTACHED','GLUCOSE_DELETE_OWN', #userId, authentication)")
    public DiaryEntryDeleteDTO deleteDiaryEntry(@PathVariable int userId,
                                                  @RequestBody @Valid DiaryEntryDeleteDTO entryDTO,
                                                  BindingResult bindingResult) {
        return deleteEntry(userId, entryDTO, bindingResult);
    }

    private EXT addEntry(int userId, EXT entryDTO, BindingResult bindingResult) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        checkValidationErrorsAndThrowException(bindingResult);

        INT entryToAdd = entryMapper.mapToInternalWithUnitConversion(entryDTO, patientProfile);
        //log.info("timestamp of entry = {}", entryToAdd.getCommitedAt());
        diaryEntryService.addDiaryEntry(entryToAdd, patientProfile, entryToAdd.getCommitedAt());

        return entryDTO;
    }

    private EXT updateEntry(int userId, EXT entryDTO, BindingResult bindingResult) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        checkValidationErrorsAndThrowException(bindingResult);

        INT entryToUpdate = entryMapper.mapToInternalWithUnitConversion(entryDTO, patientProfile);
        diaryEntryService.updateDiaryEntry(entryToUpdate, patientProfile, entryToUpdate.getCommitedAt());

        return entryDTO;
    }

    private DiaryEntryDeleteDTO deleteEntry(int userId, DiaryEntryDeleteDTO entryDeleteDTO,
                                              BindingResult bindingResult) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        checkValidationErrorsAndThrowException(bindingResult);

        DiaryEntry entryToDelete = entryMapper.mapToInternal(entryDeleteDTO);
        diaryEntryService.deleteDiaryEntry(entryToDelete, patientProfile, entryToDelete.getCommitedAt());

        return entryDeleteDTO;
    }

//    protected void checkUserId(int userId, ServiceUserDetails userDetails) {
//        if (userId != userDetails.getId())
//            throw new NotCurrentUsersInfoException("You don't have access to this user's info");
//    }

    private List<EXT> getEntries(DiaryEntryType entryType, int userId,
                                 Instant from, Instant to, ZoneOffset outputZoneOffset) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesOfType(entryType, patientProfile, from, to);

        return entryMapper.mapToDtoCollectionWithUnitConversion(entries, patientProfile, outputZoneOffset);
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
}
