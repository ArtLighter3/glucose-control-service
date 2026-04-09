package com.artlighter.glucosecontrolservice.authgateway.controller.auth;

import com.artlighter.glucosecontrolservice.authgateway.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.dto.userinfo.UserRegistrationDTO;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.authgateway.util.mapper.UserRegistrationMapper;
import com.artlighter.glucosecontrolservice.user.dto.userinfo.UserSessionDTO;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.UserService;
import com.artlighter.glucosecontrolservice.user.util.mapper.UserSessionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "В случае успеха."),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                        content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@Tag(name = "auth", description = "методы для аутентификации и авторизации")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private UserService userService;
    private UserRegistrationMapper userRegistrationMapper;
    private UserSessionMapper userSessionMapper;

    public AuthController(UserService userService, UserRegistrationMapper userRegistrationMapper,
                          UserSessionMapper userSessionMapper) {
        this.userService = userService;
        this.userRegistrationMapper = userRegistrationMapper;
        this.userSessionMapper = userSessionMapper;
    }

//    @PostMapping("/login")
//    public RedirectView login(@RequestBody UserLoginDTO userLoginDTO, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("username", userLoginDTO.username());
//        redirectAttributes.addFlashAttribute("password", userLoginDTO.password());
//        return new RedirectView("/api/auth/process-login");
//    }

    @Operation(summary = "Зарегистрировать нового больного.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Неверное тело запроса (в т.ч. если пароли не совпадают).",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "409", description = "Если пользователь с таким именем уже существует.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PostMapping(value = "/register")
    public void register(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");
        }

        User addedUser = userService.addUser(userRegistrationMapper.mapToInternal(userRegistrationDTO),
                Role.ROLE_PATIENT);
    }

    @Operation(summary = "Получить информацию о пользователе в текущей сессии.",
            description = "Должно передаваться ID сессии в Cookie с именем JSESSIONID.")
    @ApiResponses(value = @ApiResponse(responseCode = "404", description = "Если в сессии с переданным ID не было " +
            "найдено пользователя, либо если ID сессии не был передан.",
            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))))
    @GetMapping("/get-current-user")
    public UserSessionDTO getUserInSession(@AuthenticationPrincipal ServiceUserDetails serviceUserDetails) {
        if (serviceUserDetails == null)
            throw new ResourceNotFoundException(ServiceUserDetails.class, "no user for this session");

        return userSessionMapper.mapToDTO(serviceUserDetails);
    }
}
