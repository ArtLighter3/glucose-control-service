package com.artlighter.glucosecontrolservice.user.entity;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class User implements UserDetails {
    private int id;
    private List<String> authorities;
    private String username;
    private String password;

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public List<String> getStringAuthorities() {
        return authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map((stringAuthority) -> new GrantedAuthority() {
            @Override
            public @Nullable String getAuthority() {
                return stringAuthority;
            }
        }).toList();
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
