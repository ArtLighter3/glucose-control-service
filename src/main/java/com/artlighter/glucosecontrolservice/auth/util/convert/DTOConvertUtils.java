package com.artlighter.glucosecontrolservice.auth.util.convert;

import com.artlighter.glucosecontrolservice.auth.dto.RoleAuthorityDTO;
import com.artlighter.glucosecontrolservice.auth.dto.UserRegistrationDTO;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.entity.User;
import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchAuthorityException;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchRoleException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.diary.dto.*;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Класс, помогающий конвертировать объекты DTO для передачи вовне (или полученных извне) во внутренние объекты
 */
public class DTOConvertUtils {

    /**
     * Конвертирует полученный DTO объект, содержащий строковые значения роли и права в пару
     * отдельных объектов роли и права соответственно
     * @param roleAuthorityDTO DTO объект, скрывающий связку строковых роли и права
     * @return пара, содержащая роль и право соответственно
     * @throws NoSuchRoleException в случае,
     * если в системе нет роли с таким строковым идентификатором, как в DTO-объекте
     * @throws NoSuchAuthorityException в случае,
     * если в системе нет права с таким строковым идентификатором, как в DTO-объекте
     */
    public static Pair<Role, Authority> convertToRoleAndAuthority(RoleAuthorityDTO roleAuthorityDTO) {
        Role role = null;
        Authority authority = null;
        try {
            role = Role.valueOf(roleAuthorityDTO.role());
        } catch (IllegalArgumentException e) {
            throw new NoSuchRoleException(roleAuthorityDTO.role());
        }

        try {
            authority = Authority.valueOf(roleAuthorityDTO.authority());
        } catch (IllegalArgumentException e) {
            throw new NoSuchAuthorityException(roleAuthorityDTO.authority());
        }

        return Pair.of(role, authority);
    }

    public static User convertToUserFromRegistrationForm(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setUsername(userRegistrationDTO.username());
        user.setPassword(userRegistrationDTO.password());
        user.setRoles(Set.of(Role.ROLE_PATIENT));
        return user;
    }

    public static GlucoseEntry convertToEntry(GlucoseEntryDTO entryDTO, PatientProfile patientProfile) {
        GlucoseEntry entry = new GlucoseEntry();

        entry.setPatientProfile(patientProfile);
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt());
        entry.setNotes(entryDTO.notes());

        entry.setMeasurementType(entryDTO.type());

        return entry;
    }

    public static InsulinEntry convertToEntry(InsulinEntryDTO entryDTO, PatientProfile patientProfile) {
        InsulinEntry entry = new InsulinEntry();

        entry.setPatientProfile(patientProfile);
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt());
        entry.setNotes(entryDTO.notes());

        entry.setInsulinType(entryDTO.type());

        return entry;
    }

    public static MealEntry convertToEntry(MealEntryDTO entryDTO, PatientProfile patientProfile) {
        MealEntry entry = new MealEntry();

        entry.setPatientProfile(patientProfile);
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt());
        entry.setNotes(entryDTO.notes());

        return entry;
    }

    public static MedicationEntry convertToEntry(MedicationEntryDTO entryDTO, PatientProfile patientProfile) {
        MedicationEntry entry = new MedicationEntry();

        entry.setPatientProfile(patientProfile);
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt());
        entry.setNotes(entryDTO.notes());

        entry.setMedicationName(entryDTO.name());

        return entry;
    }

    public static DiaryEntry convertToEntry(DiaryEntryDeleteDTO deleteDTO,
                                            PatientProfile patientProfile, DiaryEntryType entryType) {
        DiaryEntry entry = null;

        switch (entryType) {
            case GLUCOSE_ENTRY -> entry = new GlucoseEntry();
            case INSULIN_ENTRY -> entry = new InsulinEntry();
            case MEDICATION_ENTRY -> entry = new MedicationEntry();
            case MEAL_ENTRY -> entry = new MealEntry();
        }

        entry.setPatientProfile(patientProfile);
        entry.setCommitedAt(deleteDTO.commitedAt());

        return entry;
    }

    public static ExceptionDTO createValidationException(ValidationIsFailedException ex) {
        return createException(HttpStatus.BAD_REQUEST, ex.getErrors(), ex.getMessage());
    }

    public static ExceptionDTO createOutputException(HttpStatus status, Exception exception,
                                                     boolean hideExceptionMessage) {
        String exceptionMessage = hideExceptionMessage ? "" : exception.getMessage();
        return createException(status, null, exception.getMessage());
    }

    private static ExceptionDTO createException(HttpStatus status, Errors errors, String message) {
        Map<String, String> validationErrors = errors != null ? new HashMap<>() : null;
        if (errors != null && errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                validationErrors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }

        ExceptionDTO exceptionDTO = new ExceptionDTO(Date.from(Instant.now()),
                String.valueOf(status.value()),
                status.name(),
                message,
                validationErrors);

        return exceptionDTO;
    }

}
