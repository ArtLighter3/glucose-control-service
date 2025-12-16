package com.artlighter.glucosecontrolservice.auth.util.convert;

import com.artlighter.glucosecontrolservice.auth.dto.RoleAuthorityDTO;
import com.artlighter.glucosecontrolservice.auth.dto.UserRegistrationDTO;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.entity.User;
import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchAuthorityException;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchRoleException;
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

    public static ExceptionDTO createValidationException(Errors errors) {
        return createException(HttpStatus.BAD_REQUEST, errors, "Validation of request body failed");
    }

    public static ExceptionDTO createOutputException(HttpStatus status, Exception exception) {
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
