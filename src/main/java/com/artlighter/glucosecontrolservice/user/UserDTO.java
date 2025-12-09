package com.artlighter.glucosecontrolservice.user;

import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public record UserDTO(String username, String password, Collection<Role> roles) {

    public UserDTO(User user) {
        this(user.getUsername(), user.getPassword(), user.getRoles());
    }

}
