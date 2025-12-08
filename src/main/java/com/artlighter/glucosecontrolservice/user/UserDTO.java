package com.artlighter.glucosecontrolservice.user;

import com.artlighter.glucosecontrolservice.user.entity.User;

import java.util.List;

public record UserDTO(String username, String password, List<String> authorities) {

    public UserDTO(User user) {
        this(user.getUsername(), user.getPassword(), user.getStringAuthorities());
    }

}
