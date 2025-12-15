package com.artlighter.glucosecontrolservice.auth;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.util.exception.AuthorityIsNotDeletableException;
import com.artlighter.glucosecontrolservice.auth.repository.AuthorityRepository;
import com.artlighter.glucosecontrolservice.auth.util.exception.RoleAlreadyHasAuthorityException;
import com.artlighter.glucosecontrolservice.auth.util.exception.RoleDoesNotHaveSuchAuthorityException;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для сбора, добавления и отзыва прав у пользовательских ролей
 */

@Service
@Transactional
public class AuthorityService {
    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    /**
     * Дает право определенной роли в системе, которое разрешено отзывать администраторам в настройках системы
     * @param role определенная роль (из энума), которой нужно дать отзываемое право
     * @param authority определенное отзываемое право (из энума), добавляемое к роли
     * @return экземпляр добавленного права из энума в случае успешного добавления;
     * null, если не удалось дать право роли
     * @throws com.artlighter.glucosecontrolservice.auth.util.exception.RoleAlreadyHasAuthorityException
     * в случае, если право уже существует у роли
     */
    public Authority addDeletableAuthority(Role role, Authority authority) {
        return addAuthority(role, authority, true);
    }

    /**
     * Дает право определенной роли в системе, которое запрещено отзывать администраторам в настройках системы
     * @param role определенная роль (из энума), которой нужно дать неотзываемое право
     * @param authority определенное неотзываемое право (из энума), добавляемое к роли
     * @return экземпляр добавленного права из энума в случае успешного добавления;
     * null, если не удалось дать право роли
     * @throws com.artlighter.glucosecontrolservice.auth.util.exception.RoleAlreadyHasAuthorityException
     * в случае, если право уже существует у роли
     */
    public Authority addUndeletableAuthority(Role role, Authority authority) {
        return addAuthority(role, authority, false);
    }

    /**
     * Дает определенное право определенной роли в системе
     * @param role определенная роль (из энума), которой нужно дать право
     * @param authority определенное право (из энума), добавляемое к роли
     * @param isDeletable логическое значение, определяющее, можно ли администраторам
     *                    отзывать это право у этой роли в настройках системы
     * @return экземпляр добавленного права из энума в случае успешного добавления;
     * null, если право дать не удалось по иным причинам, не перечисленным в исключениях
     * @throws com.artlighter.glucosecontrolservice.auth.util.exception.RoleAlreadyHasAuthorityException
     * в случае, если право уже существует у роли
     */
    public Authority addAuthority(Role role, Authority authority, boolean isDeletable) {
        if (role == null || authority == null) return null;

        try {
            Authority addedAuthority = authorityRepository.addAuthority(role, authority, isDeletable);
            return addedAuthority;
        } catch (EntityExistsException ex) {
            throw new RoleAlreadyHasAuthorityException(role, authority);
        }
    }

    /**
     * Дает переданные права определенной роли в системе,
     * которые разрешено отзывать администраторам в настройках системы. В отличие от методов, добавляющих одно право,
     * в случае наличия права у роли исключение не выбрасывается, а происходит попытка добавлять следующие права.
     * @param role определенная роль (из энума), которой нужно дать отзываемые права
     * @param authorities отзываемые права, добавляемые к роли.
     */
    public void addDeletableAuthorities(Role role, Authority... authorities) {
        for (Authority authority : authorities) {
            try {
                addAuthority(role, authority, true);
            } catch (RoleAlreadyHasAuthorityException ignored) {}
        }
    }

    /**
     * Дает переданные права определенной роли в системе,
     * которые запрещено отзывать администраторам в настройках системы. В отличие от методов, добавляющих одно право,
     * в случае наличия права у роли исключение не выбрасывается, а происходит попытка добавлять следующие права.
     * @param role определенная роль (из энума), которой нужно дать неотзываемые права
     * @param authorities неотзываемые права, добавляемые к роли.
     */
    public void addUndeletableAuthorities(Role role, Authority... authorities) {
        for (Authority authority : authorities) {
            try {
                addAuthority(role, authority, false);
            } catch (RoleAlreadyHasAuthorityException ignored) {}
        }
    }

    /**
     * Отзывает определенное право у определенной роли в случае, если право у роли отзываемое
     * @param role определенная роль (из энума), у которой нужно отозвать право
     * @param authority право, которое нужно отозвать у роли
     * @return отозванное право в случае успешного отзыва; null, если право отозвать не удалось по иным причинам, не
     * перечисленным в исключениях
     * @throws AuthorityIsNotDeletableException в случае, если это право должно быть у роли всегда
     * и не разрешается его отзывать
     * @throws com.artlighter.glucosecontrolservice.auth.util.exception.RoleDoesNotHaveSuchAuthorityException в случае,
     * если у роли и так нет этого права
     */
    public Authority removeAuthority(Role role, Authority authority) {
        if (role == null || authority == null) return null;

        Map<Authority, Boolean> authorities = authorityRepository.getRoleAuthorities(role);
        if (authorities == null || !authorities.containsKey(authority))
            throw new RoleDoesNotHaveSuchAuthorityException(role, authority);

        boolean isDeletable = authorities.get(authority);
        if (!isDeletable)
            throw new AuthorityIsNotDeletableException(role, authority);

        return authorityRepository.removeAuthority(role, authority);
    }

    /**
     * Проверяет, есть ли переданное право у переданной роли
     * @param role определенная роль (из энума), у которой нужно проверить право
     * @param authority право, наличие которого нужно проверить
     * @return логическое значение, отображающее, имеет ли переданная роль переданное право; всегда true
     * для роли суперпользователя; всегда false, если один из переданных параметров - null
     */
    @Transactional(readOnly = true)
    public boolean hasAuthority(Role role, Authority authority) {
        if (role == null) return false;
        if (role == Role.ROLE_SUPERUSER) return true;
        if (authority == null) return false;

        Map<Authority, Boolean> authorities = authorityRepository.getRoleAuthorities(role);
        return authorities != null && authorities.containsKey(authority);
    }

    /**
     * Находит и составляет коллекцию всех прав переданной роли
     * @param role определенная роль, список прав которой нужно получить
     * @return Множество прав роли; пустое множество в случае ошибок
     */
    @Transactional(readOnly = true)
    public Set<Authority> getRoleAuthorities(Role role) {
        if (role == null) return Collections.emptySet();;

        Map<Authority, Boolean> authorities = authorityRepository.getRoleAuthorities(role);
        if (authorities == null) return Collections.emptySet();

        return authorities.keySet();
    }

    /**
     * Собирает коллекцию всех ролей системы
     * @return Множество всех ролей системы
     */
    public Set<Role> getAllRoles() {
        return Arrays.stream(Role.values()).collect(Collectors.toSet());
    }

    /**
     * Собирает коллекцию всех доступных прав системы
     * @return Множество всех доступных прав системы
     */
    public Set<Authority> getAllAuthorities() {
        return Arrays.stream(Authority.values()).collect(Collectors.toSet());
    }

}
