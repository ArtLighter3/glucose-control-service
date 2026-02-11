package com.artlighter.glucosecontrolservice.auth.util.validation;

import com.artlighter.glucosecontrolservice.auth.dto.UserRegistrationDTO;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserDoesNotExistValidator implements ConstraintValidator<UserDoesNotExist, UserRegistrationDTO> {

    private UserService userService;

    public UserDoesNotExistValidator(UserService userService) {
        this.userService = userService;
    }
    @Override
    public boolean isValid(UserRegistrationDTO value, ConstraintValidatorContext context) {
        User user = userService.getUserByUsername(value.username());
        return user == null;
    }
}
