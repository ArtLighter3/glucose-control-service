package com.artlighter.glucosecontrolservice.auth.controller.auth;

import com.artlighter.glucosecontrolservice.auth.dto.UserRegistrationDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.mapper.UserRegistrationMapper;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "В случае успеха."),
        @ApiResponse(responseCode = "400", description = "Неверное тело запроса (в т.ч. если пароли не совпадают).",
                        content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                        content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@Tag(name = "auth", description = "методы для аутентификации и авторизации")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private UserService userService;
    private UserRegistrationMapper userRegistrationMapper;

    public AuthController(UserService userService, UserRegistrationMapper userRegistrationMapper) {
        this.userService = userService;
        this.userRegistrationMapper = userRegistrationMapper;
    }

//    @PostMapping("/login")
//    public RedirectView login(@RequestBody UserLoginDTO userLoginDTO, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("username", userLoginDTO.username());
//        redirectAttributes.addFlashAttribute("password", userLoginDTO.password());
//        return new RedirectView("/api/auth/process-login");
//    }

    @Operation(summary = "Зарегистрировать нового больного.")
    @ApiResponses(value = @ApiResponse(responseCode = "409", description = "Если пользователь с таким" +
            "именем уже существует.", content = @Content(schema = @Schema(implementation = ExceptionDTO.class))))
    @PostMapping(value = "/register")
    public void register(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");
        }

        User addedUser = userService.addUser(userRegistrationMapper.mapToInternal(userRegistrationDTO),
                Role.ROLE_PATIENT);

       // return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
