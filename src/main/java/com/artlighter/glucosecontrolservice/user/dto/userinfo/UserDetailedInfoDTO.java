package com.artlighter.glucosecontrolservice.user.dto.userinfo;

import com.artlighter.glucosecontrolservice.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Collection;

/**
 * DTO для выдачи подробной информации о пользователе администраторам
 */
@Schema(name = "UserDetailedInfo", description = "Подробная информация о пользователе для администраторов")
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
