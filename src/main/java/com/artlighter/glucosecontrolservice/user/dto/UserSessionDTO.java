package com.artlighter.glucosecontrolservice.user.dto;

import com.artlighter.glucosecontrolservice.user.entity.Role;

import java.util.Set;

/**
 * DTO для выдачи информации о пользователе в текущей сессии.
 */

public record UserSessionDTO(
    int id,
    String username,
    Set<Role> roles
) {
}
