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

        Authority revokedAuthority = authorityService.removeAuthority(role, authority);
        if (revokedAuthority != null) sessionManager.expireAllUsersWithRole(role);

        return roleAuthorityDTO;
    }

    @PostMapping("/add-authority")
    public RoleAuthorityDTO addAuthority(@RequestBody RoleAuthorityDTO roleAuthorityDTO) {
        Pair<Role, Authority> pair = DTOConvertUtils.convertToRoleAndAuthority(roleAuthorityDTO);
        Role role = pair.getFirst();
        Authority authority = pair.getSecond();

        Authority addedAuthority = authorityService.addDeletableAuthority(role, authority);
        if (addedAuthority != null) sessionManager.expireAllUsersWithRole(role);

        return roleAuthorityDTO;
    }

    @ExceptionHandler(AuthoritiesException.class)
    public ResponseEntity<ExceptionDTO> authoritiesException(AuthoritiesException e) {
        return ResponseEntity.badRequest().body(DTOConvertUtils.createOutputException(HttpStatus.BAD_REQUEST, e));
    }

    @ExceptionHandler(NoSuchEnumerableConstantException.class)
    public ResponseEntity<ExceptionDTO> noSuchEnum(NoSuchEnumerableConstantException e) {
        return ResponseEntity.badRequest().body(DTOConvertUtils.createOutputException(HttpStatus.BAD_REQUEST, e));
    }
}
