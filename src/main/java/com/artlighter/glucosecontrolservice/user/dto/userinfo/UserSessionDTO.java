package com.artlighter.glucosecontrolservice.user.dto.userinfo;

import com.artlighter.glucosecontrolservice.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * DTO для выдачи сессионной информации о пользователе в текущей сессии.
 */
@Schema(name = "UserSession", description = "Информация о сессии текущего пользователя")
public record UserSessionDTO(
    @Schema(description = "ID пользователя в текущей сессии")
    int id,
    @Schema(description = "Имя пользователя в текущей сессии")
    String username,
    @Schema(description = "Роли пользователя в текущей сессии")
    Set<Role> roles
) {
}
