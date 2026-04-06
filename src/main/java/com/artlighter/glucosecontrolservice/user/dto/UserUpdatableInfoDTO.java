package com.artlighter.glucosecontrolservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO с обновляемой самим пользователем информацией о его аккаунте. В отличие от UserDetailedInfoDTO, не содержит,
 * например, список ролей, т.к. обычному пользователю их нельзя для себя обновить.
 */
@Schema(name = "UserUpdatableInfo", description = "Доступная для обновления самим пользователем " +
        "информация о его аккаунте. Используется как для вывода, так и для вводных данных для обновления аккаунта")
public record UserUpdatableInfoDTO(
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
        LocalDate birthDate
) {
}
