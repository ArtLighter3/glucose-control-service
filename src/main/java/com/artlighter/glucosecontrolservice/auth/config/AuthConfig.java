package com.artlighter.glucosecontrolservice.auth.config;

import com.artlighter.glucosecontrolservice.auth.AuthorityService;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.user.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService) throws Exception {
        return http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) ->
                //                requests.anyRequest().permitAll())
                        requests.anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/api/auth/login")
                        .loginProcessingUrl("/api/auth/login-process"))
                .userDetailsService(userService)

                .build();
        //SessionAuth

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner initAuthorities(AuthorityService authorityService) {
        return args -> {
            authorityService.addUndeletableAuthorities(Role.ROLE_PATIENT,
                    Authority.GLUCOSE_SHOW_OWN, Authority.GLUCOSE_ADD_OWN);
        };
    }
}
