package com.artlighter.glucosecontrolservice.auth.config;

import com.artlighter.glucosecontrolservice.auth.AuthorityService;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public ApplicationRunner initAuthorities(AuthorityService authorityService) {
        return args -> {
            authorityService.addUndeletableAuthorities(Role.ROLE_PATIENT,
                    Authority.GLUCOSE_SHOW_OWN, Authority.GLUCOSE_ADD_OWN);
        };
    }
}
