package com.artlighter.glucosecontrolservice.auth.dto;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import jakarta.validation.constraints.NotNull;

public record RoleAuthorityDTO(
        @NotNull
        Role role,
        @NotNull
        Authority authority) {
}
