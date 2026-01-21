package com.artlighter.glucosecontrolservice.auth.util.mapper;

import com.artlighter.glucosecontrolservice.auth.dto.UserRegistrationDTO;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.entity.User;
import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
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
 * Класс со статическими методами, помогающими конвертировать объекты DTO во внутренние объекты и наоборот.
 */
public class DTOConvertUtils {

    public static User convertToUserFromRegistrationForm(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setUsername(userRegistrationDTO.username());
        user.setPassword(userRegistrationDTO.password());
        user.setRoles(Set.of(Role.ROLE_PATIENT));
        return user;
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
                if (error instanceof FieldError fieldError) {
                    validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
                } else validationErrors.put("", error.getDefaultMessage());
            }
        }

        return new ExceptionDTO(Date.from(Instant.now()),
                String.valueOf(status.value()),
                status.name(),
                message,
                validationErrors);
    }

}
