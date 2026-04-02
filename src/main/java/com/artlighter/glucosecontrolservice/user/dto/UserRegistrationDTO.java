package com.artlighter.glucosecontrolservice.user.dto;

import com.artlighter.glucosecontrolservice.authgateway.util.validation.PasswordsMatch;
import com.artlighter.glucosecontrolservice.general.TypeGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(name = "UserRegistration", description = "Форма регистрации нового пользователя-больного.")
@GroupSequence({UserRegistrationDTO.class, TypeGroup.class})
@PasswordsMatch(groups = TypeGroup.class)
public record UserRegistrationDTO(
        @Schema(description = "Логин пользователя")
        @NotBlank
        @Size(max = 255)
        String username,
        @Schema(description = "Пароль пользователя")
        @NotBlank
        String password,
        @Schema(description = "Повторенный пароль. Должен совпадать с полем password")
        @NotBlank
        String repeatedPassword,
        @Schema(description = "Эл. почта")
        @Email
        @Size(max = 255)
        String email,
        @Schema(description = "Имя")
        @NotBlank
        @Size(max = 255)
        String firstName,
        @Schema(description = "Отчество/второе имя")
        @Size(max = 255)
        String middleName,
        @Schema(description = "Фамилия")
        @NotBlank
        @Size(max = 255)
        String lastName,
        @Schema(description = "Дата рождения в формате ISO 8601")
        LocalDate birthDate) {
}
