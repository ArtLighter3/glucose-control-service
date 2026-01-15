package com.artlighter.glucosecontrolservice.diary.controller;

import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.diary.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.EntryMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RequestMapping("api/patients/{userId}/entries")
public abstract class AbstractDiaryEntryController<INT extends DiaryEntry, EXT> {
    protected DiaryEntryService diaryEntryService;
    protected PatientProfileService patientProfileService;
    protected EntryMapper<INT, EXT> entryMapper;
    //protected Logger log = LoggerFactory.getLogger(AbstractDiaryEntryController.class);

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
                                         @RequestParam(required = false) Instant to) {
        return getEntries(getEntryType(), userId, from, to);
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
        checkValidationErrorsAndThrowException(bindingResult);

        INT entryToAdd = entryMapper.mapToInternal(entryDTO);
        diaryEntryService.addDiaryEntry(entryToAdd,
                patientProfileService.getByUserId(userId), entryToAdd.getCommitedAt());

        return entryDTO;
    }

    private EXT updateEntry(int userId, EXT entryDTO, BindingResult bindingResult) {
        checkValidationErrorsAndThrowException(bindingResult);

        INT entryToUpdate = entryMapper.mapToInternal(entryDTO);
        diaryEntryService.updateDiaryEntry(entryToUpdate,
                patientProfileService.getByUserId(userId), entryToUpdate.getCommitedAt());

        return entryDTO;
    }

    private DiaryEntryDeleteDTO deleteEntry(int userId, DiaryEntryDeleteDTO entryDeleteDTO,
                                              BindingResult bindingResult) {
        checkValidationErrorsAndThrowException(bindingResult);

        DiaryEntry entryToDelete = entryMapper.mapToInternal(entryDeleteDTO);
        diaryEntryService.deleteDiaryEntry(entryToDelete, patientProfileService.getByUserId(userId),
                entryToDelete.getCommitedAt());

        return entryDeleteDTO;
    }

//    protected void checkUserId(int userId, ServiceUserDetails userDetails) {
//        if (userId != userDetails.getId())
//            throw new NotCurrentUsersInfoException("You don't have access to this user's info");
//    }

    private List<EXT> getEntries(DiaryEntryType entryType, int userId, Instant from, Instant to) {
        List<DiaryEntry> entries =
                diaryEntryService.getDiaryEntriesOfType(entryType, patientProfileService.getByUserId(userId), from, to);

        return entries.stream().map((entry) -> entryMapper.mapToDTO((INT) entry)).toList();
    }

    protected abstract DiaryEntryType getEntryType();


    private void checkValidationErrorsAndThrowException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationIsFailedException(bindingResult, "Validation of request body failed");
        }
    }
}
