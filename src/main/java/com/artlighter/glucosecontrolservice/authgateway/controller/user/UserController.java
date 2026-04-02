package com.artlighter.glucosecontrolservice.authgateway.controller.user;

import com.artlighter.glucosecontrolservice.authgateway.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.UserService;
import com.artlighter.glucosecontrolservice.user.dto.AttachedPatientDTO;
import com.artlighter.glucosecontrolservice.user.dto.PatientAttachDetachDTO;
import com.artlighter.glucosecontrolservice.user.dto.UserCreationDTO;
import com.artlighter.glucosecontrolservice.user.dto.UserDetailedInfoDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.service.DoctorProfileService;
import com.artlighter.glucosecontrolservice.user.util.mapper.AttachedPatientMapper;
import com.artlighter.glucosecontrolservice.user.util.mapper.UserCreationMapper;
import com.artlighter.glucosecontrolservice.user.util.mapper.UserDetailedInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "users", description = "администраторские методы для управления пользователями")
@ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "В случае успеха."),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;
    private UserCreationMapper userCreationMapper;
    private UserDetailedInfoMapper userDetailedInfoMapper;

    public UserController(UserService userService, UserCreationMapper userCreationMapper,
                          UserDetailedInfoMapper userDetailedInfoMapper) {
        this.userService = userService;
        this.userCreationMapper = userCreationMapper;
        this.userDetailedInfoMapper = userDetailedInfoMapper;
    }

    @Operation(summary = "Найти пользователей по фамилиям.", description = "Возвращает список " +
            "постранично с возможностью сортировки по определенному полю.")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Slice<UserDetailedInfoDTO> getUsersBySearchQuery(@RequestParam
                                                            @Parameter(required = true,
                                                                    description = "Поисковая фраза, " +
                                                                            "содержащаяся в фамилии.")
                                                                @Valid @NotBlank
                                                                String query,
                                                  @PageableDefault(size = 10, page = 0, sort = "lastName")
                                                        @Parameter(description = "Данные о странице и сортировке." +
                                                                "По-умолчанию сортируется " +
                                                                "по фамилии пользователя (возр.)")
                                                        Pageable pageable) {
        Slice<User> users = userService.searchByLastName(query, pageable);

        return users.map(userDetailedInfoMapper::mapToDTO);
    }

    @Operation(summary = "Создать пользователя")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "400", description = "Если тело запроса неверное.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "409", description = "Если пользователь уже существует.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailedInfoDTO postUser(@RequestBody @Valid UserCreationDTO userCreationDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult);

        User createdUser = userService.addUser(userCreationMapper.mapToInternal(userCreationDTO));

        return userDetailedInfoMapper.mapToDTO(createdUser);
    }
}
