package com.artlighter.glucosecontrolservice.auth.util;

import com.artlighter.glucosecontrolservice.user.entity.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Класс, помогающий обнулять сессии пользователей после модификации их прав
 */
@Component
public class SessionManager {
    private SessionRegistry sessionRegistry;

    public SessionManager(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * Отозвать все сессии определенного пользователя
     * @param userDetails объект с данными пользователя, чьи сессии необходимо отозвать
     */
    public void expireUser(UserDetails userDetails) {
        if (userDetails == null) return;
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
        for (SessionInformation session : sessions) {
            session.expireNow();
        }
    }

    /**
     * Отозвать все сессии всех пользователей с определенной ролью
     * @param role роль, у пользователей с которой необходимо отозвать сессии
     */
    public void expireAllUsersWithRole(Role role) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (principal instanceof UserDetails userDetails &&
                    userDetails.getAuthorities().contains(new SimpleGrantedAuthority(role.name()))) {
                expireUser(userDetails);
                break;
            }
        }
    }
}
