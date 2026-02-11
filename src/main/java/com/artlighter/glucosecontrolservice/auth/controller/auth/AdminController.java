package com.artlighter.glucosecontrolservice.auth.controller.auth;

import com.artlighter.glucosecontrolservice.auth.service.AuthorityService;
import com.artlighter.glucosecontrolservice.auth.dto.RoleAuthorityDTO;
import com.artlighter.glucosecontrolservice.user.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.util.SessionManager;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private AuthorityService authorityService;
    private SessionManager sessionManager;

    public AdminController(AuthorityService authorityService,
                           SessionManager sessionManager) {
        this.authorityService = authorityService;
        this.sessionManager = sessionManager;
    }

    @PostMapping("/roles/revoke-authority")
    @PreAuthorize("hasAuthority('AUTHORITY_GRANT_REVOKE')")
    public RoleAuthorityDTO revokeAuthority(@RequestBody @Valid RoleAuthorityDTO roleAuthorityDTO,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult, "");

        Authority revokedAuthority = authorityService.removeAuthority(roleAuthorityDTO.role(),
                roleAuthorityDTO.authority());
        if (revokedAuthority != null) sessionManager.expireAllUsersWithRole(roleAuthorityDTO.role());

        return roleAuthorityDTO;
    }

    @PostMapping("/roles/add-authority")
    @PreAuthorize("hasAuthority('AUTHORITY_GRANT_REVOKE')")
    public RoleAuthorityDTO addAuthority(@RequestBody @Valid RoleAuthorityDTO roleAuthorityDTO,
                                         BindingResult bindingResult) {
        //TODO Сделать так, чтобы можно было передавать массив ролей и прав, иначе админов после первого
        //запроса выкинет из сессии, и последующие запросы по одной паре на добавление не пройдут (или мб
        // переделать логику и не выкидывать из сессии, а обновлять права?).
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult, "");

        Authority addedAuthority = authorityService.addDeletableAuthority(roleAuthorityDTO.role(),
                roleAuthorityDTO.authority());
        if (addedAuthority != null) sessionManager.expireAllUsersWithRole(roleAuthorityDTO.role());

        return roleAuthorityDTO;
    }

}
