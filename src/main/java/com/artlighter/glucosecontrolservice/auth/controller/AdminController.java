package com.artlighter.glucosecontrolservice.auth.controller;

import com.artlighter.glucosecontrolservice.auth.service.AuthorityService;
import com.artlighter.glucosecontrolservice.auth.dto.RoleAuthorityDTO;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.util.SessionManager;
import com.artlighter.glucosecontrolservice.auth.util.convert.DTOConvertUtils;
import com.artlighter.glucosecontrolservice.auth.util.exception.AuthoritiesException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchEnumerableConstantException;
import com.artlighter.glucosecontrolservice.user.UserService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public RoleAuthorityDTO revokeAuthority(@RequestBody RoleAuthorityDTO roleAuthorityDTO) {
        Pair<Role, Authority> pair = DTOConvertUtils.convertToRoleAndAuthority(roleAuthorityDTO);
        Role role = pair.getFirst();
        Authority authority = pair.getSecond();

        Authority revokedAuthority = authorityService.removeAuthority(role, authority);
        if (revokedAuthority != null) sessionManager.expireAllUsersWithRole(role);

        return roleAuthorityDTO;
    }

    @PostMapping("/roles/add-authority")
    @PreAuthorize("hasRole('ADMIN')")
    public RoleAuthorityDTO addAuthority(@RequestBody RoleAuthorityDTO roleAuthorityDTO) {
        //TODO Сделать так, чтобы можно было передавать массив ролей и прав, иначе админов после первого
        //запроса выкинет из сессии, и последующие запросы по одной паре на добавление не пройдут (или мб
        // переделать логику и не выкидывать из сессии, а обновлять права?).
        Pair<Role, Authority> pair = DTOConvertUtils.convertToRoleAndAuthority(roleAuthorityDTO);
        Role role = pair.getFirst();
        Authority authority = pair.getSecond();

        Authority addedAuthority = authorityService.addDeletableAuthority(role, authority);
        if (addedAuthority != null) sessionManager.expireAllUsersWithRole(role);

        return roleAuthorityDTO;
    }

}
