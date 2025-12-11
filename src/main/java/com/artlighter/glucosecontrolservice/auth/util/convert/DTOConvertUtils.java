package com.artlighter.glucosecontrolservice.auth.util.convert;

import com.artlighter.glucosecontrolservice.auth.RoleAuthorityDTO;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchAuthorityException;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchRoleException;
import org.springframework.data.util.Pair;

public class DTOConvertUtils {

    public static Pair<Role, Authority> convertToRoleAndAuthority(RoleAuthorityDTO roleAuthorityDTO) {
        Role role = null;
        Authority authority = null;
        try {
            role = Role.valueOf(roleAuthorityDTO.role());
        } catch (IllegalArgumentException e) {
            throw new NoSuchRoleException(roleAuthorityDTO.role());
        }

        try {
            authority = Authority.valueOf(roleAuthorityDTO.authority());
        } catch (IllegalArgumentException e) {
            throw new NoSuchAuthorityException(roleAuthorityDTO.authority());
        }

        return Pair.of(role, authority);
    }

}
