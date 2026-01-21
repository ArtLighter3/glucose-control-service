package com.artlighter.glucosecontrolservice.auth.controller;

import com.artlighter.glucosecontrolservice.auth.dto.UserRegistrationDTO;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.entity.User;
import com.artlighter.glucosecontrolservice.auth.util.mapper.DTOConvertUtils;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/login")
//    public RedirectView login(@RequestBody UserLoginDTO userLoginDTO, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("username", userLoginDTO.username());
//        redirectAttributes.addFlashAttribute("password", userLoginDTO.password());
//        return new RedirectView("/api/auth/process-login");
//    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationIsFailedException(bindingResult, "Validation of request body failed");
        }

        User addedUser = userService.addUser(DTOConvertUtils.convertToUserFromRegistrationForm(userRegistrationDTO),
                Role.ROLE_PATIENT);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
