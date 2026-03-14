package com.artlighter.glucosecontrolservice.auth.util.mapper;

import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.Instant;
import java.util.*;

/**
 * Класс со статическими методами, помогающими конвертировать внутренние исключения во внешние объекты с информацией
 * об ошибках.
 */
public class ExceptionOutputUtils {

    public static ExceptionDTO createValidationException(ValidationIsFailedException ex) {
        return createException(HttpStatus.BAD_REQUEST, ex.getErrors(), ex.getMessage());
    }

    public static ExceptionDTO createOutputException(HttpStatus status, Exception exception,
                                                     boolean hideExceptionMessage) {
        String exceptionMessage = hideExceptionMessage ? "" : exception.getMessage();
        return createException(status, null, exceptionMessage);
    }

    private static ExceptionDTO createException(HttpStatus status, Errors errors, String message) {
        Map<String, List<String>> fieldErrors = new HashMap<>();
        List<String> objectErrors = new ArrayList<>();

        if (errors != null && errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                if (error instanceof FieldError fieldError) {
                    fieldErrors
                            .computeIfAbsent(fieldError.getField(), (key) -> new ArrayList<>())
                            .add(fieldError.getDefaultMessage());
                } else objectErrors.add(error.getDefaultMessage());
            }
        }

        return new ExceptionDTO(Instant.now(),
                String.valueOf(status.value()),
                status.name(),
                message,
                fieldErrors,
                objectErrors);
    }

}
