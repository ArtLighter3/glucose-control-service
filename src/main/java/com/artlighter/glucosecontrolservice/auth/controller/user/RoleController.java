package com.artlighter.glucosecontrolservice.auth.controller.user;

import com.artlighter.glucosecontrolservice.auth.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.user.service.AuthorityService;
import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.user.util.exception.NoSuchRoleException;
import com.artlighter.glucosecontrolservice.user.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.util.SessionManager;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.RoleWithAuthorities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "roles", description = "методы для модификации прав ролей системы")
@ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "В случае успеха."),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера.",
                content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
@RestController
@RequestMapping("/api/v1/roles")
//@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    private AuthorityService authorityService;
    private SessionManager sessionManager;

    public RoleController(AuthorityService authorityService,
                          SessionManager sessionManager) {
        this.authorityService = authorityService;
        this.sessionManager = sessionManager;
    }

    @Operation(summary = "Получить все права системы с их ролями.", description = "Также содержит информацию о том," +
            "является ли определенное право у определенной роли отзываемым (или оно должно быть у роли всегда)")
    @GetMapping
    @PreAuthorize("hasAuthority('AUTHORITY_SHOW')")
    public Set<RoleWithAuthorities> getAllRolesWithAuthorities() {
        return authorityService.getAllRolesWithAuthorities();
    }

    @Operation(summary = "Обновить права роли новым списком прав.",
            description = "Возвратит новый список прав роли. Не удалит права, которые нельзя отзывать, поэтому они " +
                    "будут в результирующем списке в любом случае и не будут удалены из роли.")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200",
                    description = "В случае успешного обновления списка прав роли, даже если список остался прежним."),
            @ApiResponse(responseCode = "400", description = "Если роли не существует.",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))})
    @PutMapping("/authorities")
    @PreAuthorize("hasAuthority('AUTHORITY_GRANT_REVOKE')")
    public Set<Authority> updateRole(@RequestParam Role role,
                                            @RequestBody Set<Authority> authorities,
                                            @AuthenticationPrincipal ServiceUserDetails userDetails) {
        if (role == Role.ROLE_SUPERUSER) throw new NoSuchRoleException("ROLE_SUPERUSER");
        if (role == Role.ROLE_ADMIN &&
                !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPERUSER")))
            throw new AuthorizationDeniedException("forbidden to edit admin authorities");

        Set<Authority> updatedAuthorities = updateAuthorities(role, authorities);
        if (updatedAuthorities != null) sessionManager.expireAllUsersWithRole(role);

        return updatedAuthorities;
    }

//    @PostMapping("/add-authority")
//    @PreAuthorize("hasAuthority('AUTHORITY_GRANT_REVOKE')")
//    public RoleAuthorityDTO addAuthority(@RequestBody @Valid RoleAuthorityDTO roleAuthorityDTO,
//                                         BindingResult bindingResult) {
//        //TODO Сделать так, чтобы можно было передавать массив ролей и прав, иначе админов после первого
//        //запроса выкинет из сессии, и последующие запросы по одной паре на добавление не пройдут (или мб
//        // переделать логику и не выкидывать из сессии, а обновлять права?).
//        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult, "");
//
//        Authority addedAuthority = authorityService.addDeletableAuthority(roleAuthorityDTO.role(),
//                roleAuthorityDTO.authority());
//        if (addedAuthority != null) sessionManager.expireAllUsersWithRole(roleAuthorityDTO.role());
//
//        return roleAuthorityDTO;
//    }

    private Set<Authority> updateAuthorities(Role role, Set<Authority> authorities) {
        return authorityService.updateRole(role, authorities);
    }

}
