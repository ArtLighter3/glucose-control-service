package com.artlighter.glucosecontrolservice.auth.util.convert;

import com.artlighter.glucosecontrolservice.auth.RoleAuthorityDTO;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchAuthorityException;
import com.artlighter.glucosecontrolservice.auth.util.exception.NoSuchRoleException;
import org.springframework.data.util.Pair;

/**
 * Класс, помогающий конвертировать объекты DTO для передачи вовне (или полученных извне) во внутренние объекты
 */
public class DTOConvertUtils {

    /**
     * Конвертирует полученный DTO объект, содержащий строковые значения роли и права в пару
     * отдельных объектов роли и права соответственно
     * @param roleAuthorityDTO DTO объект, скрывающий связку строковых роли и права
     * @return пара, содержащая роль и право соответственно
     * @throws NoSuchRoleException в случае,
     * если в системе нет роли с таким строковым идентификатором, как в DTO-объекте
     * @throws NoSuchAuthorityException в случае,
     * если в системе нет права с таким строковым идентификатором, как в DTO-объекте
     */
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
