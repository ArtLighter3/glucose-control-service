package com.artlighter.glucosecontrolservice.auth.config;

import com.artlighter.glucosecontrolservice.auth.service.AuthorityService;
import com.artlighter.glucosecontrolservice.user.entity.Authority;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.auth.service.UserDetailsFromUserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsFromUserService userDetailsService)
            throws Exception {
        return http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) ->
                                requests.requestMatchers("/api/auth/register").permitAll()
                                        .anyRequest().authenticated())
                        //        .requestMatchers("/api/auth/process-login").permitAll()
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .formLogin(form ->
                        form.loginProcessingUrl("/api/auth/process-login"))
                .userDetailsService(userDetailsService)
                .sessionManagement(session ->
                        session.maximumSessions(1).sessionRegistry(sessionRegistry()))
                .build();
        //SessionAuth

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

//    @Bean
//    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher appEventPublisher) {
//        return new DefaultAuthenticationEventPublisher(appEventPublisher);
//    }

    @Bean
    public ApplicationRunner initAuthorities(AuthorityService authorityService) {
        return args -> {
            authorityService.addUndeletableAuthorities(Role.ROLE_PATIENT,
                    Authority.GLUCOSE_SHOW_OWN, Authority.GLUCOSE_ADD_OWN, Authority.GLUCOSE_UPDATE_OWN,
                    Authority.INSULIN_PROFILE_SHOW_OWN, Authority.INSULIN_PROFILE_UPDATE_OWN,
                    Authority.INSULIN_CALCULATE_OWN, Authority.TEMPLATE_ADD_OWN, Authority.TEMPLATE_SHOW_OWN,
                    Authority.TEMPLATE_DELETE_OWN, Authority.TEMPLATE_UPDATE_OWN);

            authorityService.addUndeletableAuthorities(Role.ROLE_DOCTOR, Authority.GLUCOSE_SHOW_ATTACHED);
        };
    }
}
