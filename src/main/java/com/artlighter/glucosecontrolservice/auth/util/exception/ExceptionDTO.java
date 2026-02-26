package com.artlighter.glucosecontrolservice.auth.util.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

@Schema(name = "Exception", description = "Сообщение об ошибке")
public record ExceptionDTO(
        @Schema(description = "Временная отметка генерации ошибки")
        Instant timestamp,
        @Schema(description = "Код статуса по HTTP", example = "404")
        String status,
        @Schema(description = "Полное имя ошибки по HTTP", example = "Not Found")
        String error,
        @Schema(description = "Сообщение с подробностями про ошибку от сервера")
        String message,
        @Schema(description = "Ошибки валидации конкретных полей или всего объекта в целом (если был некорректный " +
                "объект, который проверялся на корректность). " +
                "Формат каждого вхождения: \"Имя поля\":\"Сообщение\"; Если ошибка относится к объекту в целом, то " +
                "в имени поля указывается пустая строка.")
        Map<String, String> validationErrors
) {
}
