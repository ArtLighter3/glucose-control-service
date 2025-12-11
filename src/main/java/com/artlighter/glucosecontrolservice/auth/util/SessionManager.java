package com.artlighter.glucosecontrolservice.auth.util;

import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.entity.User;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionManager {
    private SessionRegistry sessionRegistry;

    public SessionManager(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void expireUser(User user) {
        if (user == null) return;
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(user, false);
        for (SessionInformation session : sessions) {
            session.expireNow();
        }
    }

    public void expireAllUsersWithRole(Role role) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (principal instanceof User user && user.getRoles().contains(role)) {
                expireUser(user);
                break;
            }
        }
    }
}
