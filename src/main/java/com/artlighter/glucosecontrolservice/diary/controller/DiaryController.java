package com.artlighter.glucosecontrolservice.diary.controller;

import com.artlighter.glucosecontrolservice.auth.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.auth.util.convert.DTOConvertUtils;
import com.artlighter.glucosecontrolservice.auth.util.exception.*;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.InsulinEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.MealEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.MedicationEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.List;

@RestController
@RequestMapping("api/patients")
public class DiaryController {
    private DiaryEntryService diaryEntryService;
    private PatientProfileService patientProfileService;

    public DiaryController(DiaryEntryService diaryEntryService, PatientProfileService patientProfileService) {
        this.diaryEntryService = diaryEntryService;
        this.patientProfileService = patientProfileService;
    }

//    @PostMapping("/{userId}/")
//    public HttpStatus postMeasurement(@RequestBody DiaryEntryDTO measurement) {
//        DiaryEntry entry = diaryService.saveMeasurement(convertFromDTO(measurement));
//        if (entry != null) {
//            return HttpStatus.CREATED;
//        }
//        return HttpStatus.INTERNAL_SERVER_ERROR;
//    }

    @GetMapping("/{userId}/entries")
    @PreAuthorize("hasAuthority('GLUCOSE_SHOW_OWN')")
    public List<DiaryEntry> getAllPatientEntries(@PathVariable int userId,
                                              @RequestParam(required = false) Instant from,
                                              @RequestParam(required = false) Instant to,
                                              @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkUserId(userId, userDetails);
        return getEntries(null, userId, from, to);
    }

    @GetMapping("/{userId}/entries/glucose")
    @PreAuthorize("hasAuthority('GLUCOSE_SHOW_OWN')")
    public List<DiaryEntry> getPatientGlucoseEntries(@PathVariable int userId,
                                              @RequestParam(required = false) Instant from,
                                              @RequestParam(required = false) Instant to,
                                              @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkUserId(userId, userDetails);
        return getEntries(DiaryEntryType.GLUCOSE_ENTRY, userId, from, to);
    }

    @GetMapping("/{userId}/entries/insulin")
    @PreAuthorize("hasAuthority('GLUCOSE_SHOW_OWN')")
    public List<DiaryEntry> getPatientInsulinEntries(@PathVariable int userId,
                                                     @RequestParam(required = false) Instant from,
                                                     @RequestParam(required = false) Instant to,
                                                     @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkUserId(userId, userDetails);
        return getEntries(DiaryEntryType.INSULIN_ENTRY, userId, from, to);
    }

    @GetMapping("/{userId}/entries/medication")
    @PreAuthorize("hasAuthority('GLUCOSE_SHOW_OWN')")
    public List<DiaryEntry> getPatientMedicationEntries(@PathVariable int userId,
                                                     @RequestParam(required = false) Instant from,
                                                     @RequestParam(required = false) Instant to,
                                                     @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkUserId(userId, userDetails);
        return getEntries(DiaryEntryType.MEDICATION_ENTRY, userId, from, to);
    }

    @GetMapping("/{userId}/entries/meal")
    @PreAuthorize("hasAuthority('GLUCOSE_SHOW_OWN')")
    public List<DiaryEntry> getPatientMealEntries(@PathVariable int userId,
                                                        @RequestParam(required = false) Instant from,
                                                        @RequestParam(required = false) Instant to,
                                                        @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkUserId(userId, userDetails);
        return getEntries(DiaryEntryType.MEAL_ENTRY, userId, from, to);
    }

    @PostMapping("/{userId}/entries/glucose")
    @PreAuthorize("hasAuthority('GLUCOSE_ADD_OWN')")
    public ResponseEntity<DiaryEntry> addGlucoseEntry(@PathVariable int userId,
                                      @RequestBody @Valid GlucoseEntryDTO entryDTO,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkValidationErrorsAndThrowException(bindingResult);
        checkUserId(userId, userDetails);

        return addEntry(DTOConvertUtils.convertToEntry(entryDTO, patientProfileService.getByUserId(userId)));
    }

    @PostMapping("/{userId}/entries/insulin")
    @PreAuthorize("hasAuthority('GLUCOSE_ADD_OWN')")
    public ResponseEntity<DiaryEntry> addInsulinEntry(@PathVariable int userId,
                                                      @RequestBody @Valid InsulinEntryDTO entryDTO,
                                                      BindingResult bindingResult,
                                                      @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkValidationErrorsAndThrowException(bindingResult);
        checkUserId(userId, userDetails);

        return addEntry(DTOConvertUtils.convertToEntry(entryDTO, patientProfileService.getByUserId(userId)));
    }

    @PostMapping("/{userId}/entries/meal")
    @PreAuthorize("hasAuthority('GLUCOSE_ADD_OWN')")
    public ResponseEntity<DiaryEntry> addMealEntry(@PathVariable int userId,
                                                      @RequestBody @Valid MealEntryDTO entryDTO,
                                                      BindingResult bindingResult,
                                                      @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkValidationErrorsAndThrowException(bindingResult);
        checkUserId(userId, userDetails);

        return addEntry(DTOConvertUtils.convertToEntry(entryDTO, patientProfileService.getByUserId(userId)));
    }

    @PostMapping("/{userId}/entries/medication")
    @PreAuthorize("hasAuthority('GLUCOSE_ADD_OWN')")
    public ResponseEntity<DiaryEntry> addMedicationEntry(@PathVariable int userId,
                                                      @RequestBody @Valid MedicationEntryDTO entryDTO,
                                                      BindingResult bindingResult,
                                                      @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkValidationErrorsAndThrowException(bindingResult);
        checkUserId(userId, userDetails);

        return addEntry(DTOConvertUtils.convertToEntry(entryDTO, patientProfileService.getByUserId(userId)));
    }

    @PutMapping("/{userId}/entries/glucose")
    @PreAuthorize("hasAuthority('GLUCOSE_UPDATE_OWN')")
    public ResponseEntity<DiaryEntry> updateGlucoseEntry(@PathVariable int userId,
                                                      @RequestBody @Valid GlucoseEntryDTO entryDTO,
                                                      BindingResult bindingResult,
                                                      @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkValidationErrorsAndThrowException(bindingResult);
        checkUserId(userId, userDetails);

        return updateEntry(DTOConvertUtils.convertToEntry(entryDTO, patientProfileService.getByUserId(userId)));
    }

    @PutMapping("/{userId}/entries/insulin")
    @PreAuthorize("hasAuthority('GLUCOSE_UPDATE_OWN')")
    public ResponseEntity<DiaryEntry> updateInsulinEntry(@PathVariable int userId,
                                                      @RequestBody @Valid InsulinEntryDTO entryDTO,
                                                      BindingResult bindingResult,
                                                      @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkValidationErrorsAndThrowException(bindingResult);
        checkUserId(userId, userDetails);

        return updateEntry(DTOConvertUtils.convertToEntry(entryDTO, patientProfileService.getByUserId(userId)));
    }

    @PutMapping("/{userId}/entries/meal")
    @PreAuthorize("hasAuthority('GLUCOSE_UPDATE_OWN')")
    public ResponseEntity<DiaryEntry> updateMealEntry(@PathVariable int userId,
                                                   @RequestBody @Valid MealEntryDTO entryDTO,
                                                   BindingResult bindingResult,
                                                   @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkValidationErrorsAndThrowException(bindingResult);
        checkUserId(userId, userDetails);

        return updateEntry(DTOConvertUtils.convertToEntry(entryDTO, patientProfileService.getByUserId(userId)));
    }

    @PutMapping("/{userId}/entries/medication")
    @PreAuthorize("hasAuthority('GLUCOSE_UPDATE_OWN')")
    public ResponseEntity<DiaryEntry> updateMedicationEntry(@PathVariable int userId,
                                                         @RequestBody @Valid MedicationEntryDTO entryDTO,
                                                         BindingResult bindingResult,
                                                         @AuthenticationPrincipal ServiceUserDetails userDetails) {
        checkValidationErrorsAndThrowException(bindingResult);
        checkUserId(userId, userDetails);

        return updateEntry(DTOConvertUtils.convertToEntry(entryDTO, patientProfileService.getByUserId(userId)));
    }

    private void checkValidationErrorsAndThrowException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationIsFailedException(bindingResult, "Validation of request body failed");
        }
    }

    private ResponseEntity<DiaryEntry> addEntry(DiaryEntry entry) {
        DiaryEntry added = diaryEntryService.addDiaryEntry(entry);
        if (added != null) return ResponseEntity.status(HttpStatus.CREATED).body(added);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<DiaryEntry> updateEntry(DiaryEntry entry) {
        DiaryEntry updated = diaryEntryService.updateDiaryEntry(entry);
        if (updated != null) return ResponseEntity.status(HttpStatus.OK).body(updated);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private void checkUserId(int userId, ServiceUserDetails userDetails) {
        if (userId != userDetails.getId())
            throw new NotCurrentUsersInfoException("You don't have access to this user's info");
    }

    private List<DiaryEntry> getEntries(DiaryEntryType entryType, int userId, Instant from, Instant to) {
        if (to == null) to = Instant.now();
        if (from == null) from = to.minus(Duration.ofDays(30));

        return diaryEntryService.getDiaryEntriesOfType(entryType, patientProfileService.getByUserId(userId), from, to);
    }

    @ExceptionHandler(ValidationIsFailedException.class)
    public ResponseEntity<ExceptionDTO> validationIsFailedException(ValidationIsFailedException ex) {
        return ResponseEntity.badRequest().body(DTOConvertUtils.createValidationException(ex));
    }

    @ExceptionHandler(NotCurrentUsersInfoException.class)
    public ResponseEntity<ExceptionDTO> notCurrentUsersInfoException(NotCurrentUsersInfoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(DTOConvertUtils.createOutputException(HttpStatus.FORBIDDEN, ex, false));
    }

    @ExceptionHandler(NoRepositoryForEntryTypeException.class)
    public ResponseEntity<ExceptionDTO> noRepositoryForEntryTypeException(NoRepositoryForEntryTypeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DTOConvertUtils.createOutputException(HttpStatus.INTERNAL_SERVER_ERROR, ex, true));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDTO> illegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DTOConvertUtils.createOutputException(HttpStatus.INTERNAL_SERVER_ERROR, ex, true));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> resourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(DTOConvertUtils.createOutputException(HttpStatus.CONFLICT, ex, false));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDTO> resourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(DTOConvertUtils.createOutputException(HttpStatus.NOT_FOUND, ex, false));
    }

//    private DiaryEntry convertFromDTO(DiaryEntryDTO dto) {
//        return new DiaryEntry(dto.measurement(), dto.date(), dto.notes(), new UserDTO(dto.username(), "", Collections.emptySet()));
//    }
//
//    private DiaryEntryDTO convertToDTO(DiaryEntry diaryEntry) {
//        return new DiaryEntryDTO(diaryEntry.getMeasurement(), diaryEntry.getDate(), diaryEntry.getNotes(), diaryEntry.getUser().username());
//    }
}
