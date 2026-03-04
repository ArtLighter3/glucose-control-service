package com.artlighter.glucosecontrolservice.user.dto;

import com.artlighter.glucosecontrolservice.user.entity.Authority;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import jakarta.validation.constraints.NotNull;

public record RoleAuthorityDTO(
        @NotNull
        Role role,
        @NotNull
        Authority authority) {
}
