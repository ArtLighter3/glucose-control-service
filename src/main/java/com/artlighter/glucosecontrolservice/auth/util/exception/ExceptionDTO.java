package com.artlighter.glucosecontrolservice.auth.util.exception;

import org.springframework.validation.Errors;

import java.util.Date;
import java.util.Map;

public record ExceptionDTO(
        Date timestamp,
        String status,
        String error,
        String message,
        Map<String, String> validationErrors
) {
}
