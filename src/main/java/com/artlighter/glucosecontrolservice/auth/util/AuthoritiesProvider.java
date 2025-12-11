//package com.artlighter.glucosecontrolservice.auth.util;
//
//import com.artlighter.glucosecontrolservice.auth.AuthorityService;
//import com.artlighter.glucosecontrolservice.auth.entity.Authority;
//import com.artlighter.glucosecontrolservice.auth.entity.Role;
//import com.artlighter.glucosecontrolservice.auth.entity.User;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Component
//public class AuthoritiesProvider {
//    private AuthorityService authorityService;
//
//    public AuthoritiesProvider(AuthorityService authorityService) {
//        this.authorityService = authorityService;
//    }
//
//    @EventListener
//    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
//        Authentication authentication = event.getAuthentication();
//        if (authentication.getPrincipal() instanceof User user) {
//            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//
//            for (Role role : user.getRoles()) {
//                grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
//                Set<Authority> authorities = authorityService.getRoleAuthorities(role);
//                grantedAuthorities.addAll(authorities.stream()
//                        .map(authority -> new SimpleGrantedAuthority(authority.name()))
//                        .collect(Collectors.toSet()));
//            }
//
//            authentication = (UsernamePasswordAuthenticationToken) authentication;
//            for (GrantedAuthority grantedAuthority : grantedAuthorities) {
//                authentication.getAuthorities().add(grantedAuthority);
//            }
//        }
//    }
//}
