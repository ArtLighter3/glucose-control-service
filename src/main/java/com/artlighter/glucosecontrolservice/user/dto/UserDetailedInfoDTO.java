package com.artlighter.glucosecontrolservice.user.dto;

import com.artlighter.glucosecontrolservice.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

/**
 * DTO для выдачи подробной информации о пользователе
 */
@Schema(name = "UserDetailedInfo", description = "Подробная информация о пользователе")
public record UserDetailedInfoDTO(
        @Schema(description = "ID пользователя в системе")
        int id,
        @Schema(description = "Логин пользователя")
        String username,
        @Schema(description = "Эл. почта")
        String email,
        @Schema(description = "Имя")
        String firstName,
        @Schema(description = "Отчество/второе имя")
        String middleName,
        @Schema(description = "Фамилия")
        String lastName,
        @Schema(description = "Дата рождения в формате ISO 8601")
        LocalDate birthDate,
        @Schema(description = "Роли пользователя в системе")
        Collection<Role> roles
) {
}
