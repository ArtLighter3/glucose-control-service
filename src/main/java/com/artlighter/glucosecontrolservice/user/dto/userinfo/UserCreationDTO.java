package com.artlighter.glucosecontrolservice.user.dto.userinfo;

import com.artlighter.glucosecontrolservice.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO для запроса создания нового пользователя администраторами
 */
@Schema(name = "UserCreation", description = "Форма создания нового пользователя")
public record UserCreationDTO(
        @Schema(description = "Логин пользователя")
        @NotBlank
        @Size(max = 255)
        String username,
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
        @Schema(description = "Пароль пользователя")
        @NotBlank
        String password,
        @Schema(description = "Дата рождения в формате ISO 8601")
        LocalDate birthDate,
        @Schema(description = "Роли нового пользователя")
        @NotNull
        @Size(min = 1)
        Set<Role> roles
) {
}
