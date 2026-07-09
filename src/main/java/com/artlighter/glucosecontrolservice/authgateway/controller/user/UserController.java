package com.artlighter.glucosecontrolservice.authgateway.controller.user;

import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.UserService;
import com.artlighter.glucosecontrolservice.user.dto.userinfo.UserCreationDTO;
import com.artlighter.glucosecontrolservice.user.dto.userinfo.UserDetailedInfoDTO;
import com.artlighter.glucosecontrolservice.user.dto.userinfo.UserUpdatableInfoDTO;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.util.mapper.UserCreationMapper;
import com.artlighter.glucosecontrolservice.user.util.mapper.UserDetailedInfoMapper;
import com.artlighter.glucosecontrolservice.user.util.mapper.UserUpdatableInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "users", description = "методы для управления пользователями, общей информацией о них")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;
    private UserCreationMapper userCreationMapper;
    private UserDetailedInfoMapper userDetailedInfoMapper;
    private UserUpdatableInfoMapper userUpdatableInfoMapper;

    public UserController(UserService userService, UserCreationMapper userCreationMapper,
                          UserDetailedInfoMapper userDetailedInfoMapper,
                          UserUpdatableInfoMapper userUpdatableInfoMapper) {
        this.userService = userService;
        this.userCreationMapper = userCreationMapper;
        this.userDetailedInfoMapper = userDetailedInfoMapper;
        this.userUpdatableInfoMapper = userUpdatableInfoMapper;
    }

    @Operation(summary = "Найти пользователей по ФИО (для администраторов).", description = "Возвращает список " +
            "постранично с возможностью сортировки по определенному полю.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "В случае успеха."))
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDetailedInfoDTO> getUsersBySearchQuery(@RequestParam
                                                            @Parameter(required = true,
                                                                       description = "Поисковая фраза, " +
                                                                            "содержащаяся в ФИО.")
                                                            String query,
                                                            @RequestParam(required = false)
                                                            @Parameter(required = false,
                                                                       description = "Фильтрация по роли в системе")
                                                            Role role,
                                                        @PageableDefault(size = 10, page = 0,
                                                          sort = {"lastName", "firstName", "middleName"})
                                                        @Parameter(description = "Данные о странице и сортировке. " +
                                                                "По-умолчанию сортируется " +
                                                                "по фамилии пользователя (возр.)")
                                                            Pageable pageable) {
        Page<User> users = userService.searchByFullNameAndRole(query, role, pageable);

        return users.map(userDetailedInfoMapper::mapToDTO);
    }

    @Operation(summary = "Получить обновляемую информацию об аккаунте (для самого владельца аккаунта).")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "В случае успеха."),
            @ApiResponse(responseCode = "404", description = "Если пользователь с таким ID не найден.")})
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @resourceAccessInspector.isOwnerOfResource(#userId, authentication)")
    public UserUpdatableInfoDTO getUser(@PathVariable int userId) {
        return userUpdatableInfoMapper.mapToDTO(userService.getUserById(userId));
    }

    @Operation(summary = "Создать пользователя (для администраторов).",
            description = "Администраторы по-умолчанию не могут создавать других администраторов")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "В случае успеха."),
                    @ApiResponse(responseCode = "400", description = "Если тело запроса неверное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
                    @ApiResponse(responseCode = "409", description = "Если пользователь уже существует.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @SecurityRequirement(name = "csrf")
    @PostMapping
    @PreAuthorize("hasRole('SUPERUSER') or (hasRole('ADMIN') " +
            "and not #userCreationDTO.roles()" +
                ".contains(T(com.artlighter.glucosecontrolservice.user.entity.Role).ROLE_ADMIN) " +
            "and not #userCreationDTO.roles()" +
                ".contains(T(com.artlighter.glucosecontrolservice.user.entity.Role).ROLE_SUPERUSER))")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailedInfoDTO postUser(@RequestBody @Valid UserCreationDTO userCreationDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult);

        User createdUser = userService.addUser(userCreationMapper.mapToInternal(userCreationDTO));

        return userDetailedInfoMapper.mapToDTO(createdUser);
    }

    @Operation(summary = "Обновить данные аккаунта пользователя (для самого владельца аккаунта или администратора)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Если тело запроса неверное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Если пользователь с таким ID не найден.")})
    @SecurityRequirement(name = "csrf")
    @PutMapping("/{userId}")
    @PreAuthorize("@resourceAccessInspector.isOwnerOfResource(#userId, authentication) or " +
            "(hasRole('ADMIN') and not @resourceAccessInspector.isAdmin(#userId)) or " +
            "hasRole('SUPERUSER')")
    public UserUpdatableInfoDTO putUser(@PathVariable int userId,
                                        @RequestBody @Valid UserUpdatableInfoDTO userUpdatableInfoDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult);

        User updatedUser =
                userService.updateUserInfo(userUpdatableInfoMapper.mapToInternal(userUpdatableInfoDTO), userId);

        return userUpdatableInfoMapper.mapToDTO(updatedUser);
    }

    @Operation(summary = "Удалить пользователя. Только для администраторов")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Пользователь удален, либо его и не существовало.")})
    @SecurityRequirement(name = "csrf")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('SUPERUSER') or (hasRole('ADMIN') and not @resourceAccessInspector.isAdmin(#userId))")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }
}
