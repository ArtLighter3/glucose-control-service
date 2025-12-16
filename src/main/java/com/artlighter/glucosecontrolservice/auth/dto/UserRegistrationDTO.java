package com.artlighter.glucosecontrolservice.auth.dto;

import com.artlighter.glucosecontrolservice.auth.util.validation.PasswordsMatch;
import com.artlighter.glucosecontrolservice.auth.util.validation.UserDoesNotExist;
import jakarta.validation.constraints.NotEmpty;

@UserDoesNotExist
@PasswordsMatch
public record UserRegistrationDTO(
        @NotEmpty(message = "username shouldn't be empty")
        String username,
        @NotEmpty(message = "password shouldn't be empty")
        String password,
        @NotEmpty(message = "password shouldn't be empty")
        String repeatedPassword,
        Integer age) {
}
