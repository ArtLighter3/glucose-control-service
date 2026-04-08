package com.artlighter.glucosecontrolservice.authgateway.service;

import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.authgateway.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsFromUserService implements UserDetailsService {
    private UserService userService;
   // private AuthorityService authorityService;

    public UserDetailsFromUserService(UserService userService/*, AuthorityService authorityService*/) {
        this.userService = userService;
       // this.authorityService = authorityService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (user.getRoles().contains(Role.ROLE_SUPERUSER)) {
            grantedAuthorities.addAll(Arrays.stream(Role.values())
                    .map((role) -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toSet()));
        } else {
            for (Role role : user.getRoles()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
            }
        }

        return new ServiceUserDetails(user.getId(),
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userService.findUserByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(username);
//        }
//
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//
//        if (user.getRoles().contains(Role.ROLE_SUPERUSER)) {
//            grantedAuthorities.addAll(authorityService.getAllRoles().stream()
//                    .map(role -> new SimpleGrantedAuthority(role.name()))
//                    .collect(Collectors.toSet()));
//            grantedAuthorities.addAll(authorityService.getAllAuthorities().stream()
//                    .map(authority -> new SimpleGrantedAuthority(authority.name()))
//                    .collect(Collectors.toSet()));
//        } else {
//            for (Role role : user.getRoles()) {
//                grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
//                Set<Authority> authorities = authorityService.getRoleAuthorities(role);
//                grantedAuthorities.addAll(authorities.stream()
//                        .map(authority -> new SimpleGrantedAuthority(authority.name()))
//                        .collect(Collectors.toSet()));
//            }
//        }
//
//        return new ServiceUserDetails(user.getId(), user.getUsername(),
//                user.getPassword(),
//                grantedAuthorities);
//    }
}
