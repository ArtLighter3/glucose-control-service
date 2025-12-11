package com.artlighter.glucosecontrolservice.auth.controller;

import com.artlighter.glucosecontrolservice.auth.AuthorityService;
import com.artlighter.glucosecontrolservice.auth.RoleAuthorityDTO;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.util.SessionManager;
import com.artlighter.glucosecontrolservice.auth.util.convert.DTOConvertUtils;
import com.artlighter.glucosecontrolservice.auth.util.exception.AuthoritiesException;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchEnumerableConstantException;
import com.artlighter.glucosecontrolservice.auth.util.exception.RoleAlreadyHasAuthorityException;
import com.artlighter.glucosecontrolservice.auth.util.exception.RoleDoesNotHaveSuchAuthorityException;
import com.artlighter.glucosecontrolservice.user.UserService;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private UserService userService;
    private AuthorityService authorityService;
    private SessionManager sessionManager;

    public AdminController(UserService userService,
                           AuthorityService authorityService,
                           SessionManager sessionManager) {
        this.userService = userService;
        this.authorityService = authorityService;
        this.sessionManager = sessionManager;
    }

    @PostMapping("/revoke-authority")
    //@PreAuthorize("hasRole('ADMIN')")
    public RoleAuthorityDTO revokeAuthority(@RequestBody RoleAuthorityDTO roleAuthorityDTO) {
        Pair<Role, Authority> pair = DTOConvertUtils.convertToRoleAndAuthority(roleAuthorityDTO);
        Role role = pair.getFirst();
        Authority authority = pair.getSecond();

        if (authorityService.hasAuthority(role, authority)) {
            authorityService.removeAuthority(role, authority);
            sessionManager.expireAllUsersWithRole(role);
            return roleAuthorityDTO;
        }

        throw new RoleDoesNotHaveSuchAuthorityException(role, authority);
    }

    @PostMapping("/add-authority")
    public RoleAuthorityDTO addAuthority(@RequestBody RoleAuthorityDTO roleAuthorityDTO) {
        Pair<Role, Authority> pair = DTOConvertUtils.convertToRoleAndAuthority(roleAuthorityDTO);
        Role role = pair.getFirst();
        Authority authority = pair.getSecond();

        if (!authorityService.hasAuthority(role, authority)) {
            authorityService.addDeletableAuthority(role, authority);
            sessionManager.expireAllUsersWithRole(role);
            return roleAuthorityDTO;
        }

        throw new RoleAlreadyHasAuthorityException(role, authority);
    }

    @ExceptionHandler(AuthoritiesException.class)
    public ResponseEntity roleDoesNotHaveSuchAuthority(AuthoritiesException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoSuchEnumerableConstantException.class)
    public ResponseEntity noSuchEnum(NoSuchEnumerableConstantException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
