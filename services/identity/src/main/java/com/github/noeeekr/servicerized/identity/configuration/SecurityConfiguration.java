package com.github.noeeekr.servicerized.identity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.github.noeeekr.servicerized.identity.controller.AuthenticationController;
import com.github.noeeekr.servicerized.identity.middlewares.AuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf((configurator) -> configurator.disable());
        httpSecurity.sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Allows only requests where client does not contain a spring authentication token for sign
        // in route
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AuthenticationController.CONTROLLER_PATH).anonymous());
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AuthenticationController.CONTROLLER_SIGNIN_PATH).anonymous());

        // Allows only requests where client contains a spring authentication token for default
        // routes
        httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        httpSecurity.addFilterBefore(authenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
