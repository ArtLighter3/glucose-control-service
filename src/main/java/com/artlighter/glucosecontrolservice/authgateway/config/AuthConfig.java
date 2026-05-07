package com.artlighter.glucosecontrolservice.authgateway.config;

import com.artlighter.glucosecontrolservice.authgateway.service.UserDetailsFromUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsFromUserService userDetailsService)
            throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf
                        //.disable())
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/auth/register",
                                "/api/v1/auth/csrf",
                                "/nightscout/**").permitAll()
                                        .anyRequest().authenticated())
                        //        .requestMatchers("/api/auth/process-login").permitAll()
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .formLogin(form -> form
                                .loginProcessingUrl("/api/v1/auth/process-login")
                             //   .successForwardUrl("/api/v1/auth/get-current-user")
                             //   .failureForwardUrl("/api/v1/auth/get-current-user")
                                .defaultSuccessUrl("/api/v1/auth/get-current-user", true)
                                .failureHandler(new AuthenticationEntryPointFailureHandler(
                                        new HttpStatusEntryPoint(HttpStatus.BAD_REQUEST)))
                                .permitAll())
                .logout(logout -> logout
                                .logoutUrl("/api/v1/auth/logout"))
                .userDetailsService(userDetailsService)
                .sessionManagement(session ->
                        session.maximumSessions(1).sessionRegistry(sessionRegistry()))
                .build();
        //SessionAuth

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(Duration.ofHours(24));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
//    public ApplicationRunner initAuthorities(AuthorityService authorityService) {
//        return args -> {
//            authorityService.addUndeletableAuthorities(Role.ROLE_PATIENT,
//                    Authority.GLUCOSE_SHOW_OWN, Authority.GLUCOSE_ADD_OWN, Authority.GLUCOSE_UPDATE_OWN,
//                    Authority.INSULIN_PROFILE_SHOW_OWN, Authority.INSULIN_PROFILE_UPDATE_OWN,
//                    Authority.INSULIN_PROFILE_ADD_OWN,
//                    Authority.INSULIN_CALCULATE_OWN, Authority.ACTIVITY_SHOW_OWN,
//                    Authority.TEMPLATE_ADD_OWN, Authority.TEMPLATE_SHOW_OWN,
//                    Authority.TEMPLATE_DELETE_OWN, Authority.TEMPLATE_UPDATE_OWN);
//
//            authorityService.addUndeletableAuthorities(Role.ROLE_DOCTOR, Authority.GLUCOSE_SHOW_ATTACHED,
//                    Authority.ATTACHED_PATIENT_SHOW_OWN);
//        };
//    }
}
