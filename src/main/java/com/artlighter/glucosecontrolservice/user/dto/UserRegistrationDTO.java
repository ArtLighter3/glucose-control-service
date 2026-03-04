package com.artlighter.glucosecontrolservice.user.dto;

import com.artlighter.glucosecontrolservice.auth.util.validation.PasswordsMatch;
import com.artlighter.glucosecontrolservice.general.TypeGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "UserRegistration", description = "Форма регистрации нового пользователя.")
@GroupSequence({UserRegistrationDTO.class, TypeGroup.class})
@PasswordsMatch(groups = TypeGroup.class)
public record UserRegistrationDTO(
        @Schema(description = "Имя пользователя")
        @NotBlank
        String username,
        @Schema(description = "Пароль пользователя")
        @NotBlank
        String password,
        @Schema(description = "Повторенный пароль. Должен совпадать с полем password")
        @NotBlank
        String repeatedPassword) {
}
