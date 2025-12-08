package com.artlighter.glucosecontrolservice.user.repository;

import com.artlighter.glucosecontrolservice.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserRepository {
    private Map<String, User> users = new HashMap<>();

    public UserRepository() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("user");
        users.put(user.getUsername(), user);
    }

    public Optional<User> getUserByUsername(String username) {
        User user = users.get(username);
        return Optional.of(user);
    }

    public User insertUser(User user) {
        return users.put(user.getUsername(), user);
    }
}
