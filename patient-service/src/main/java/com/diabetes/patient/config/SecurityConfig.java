package com.diabetes.patient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.Filter;


/**
 * Configuration Spring Security pour un Resource Server JWT.
 * La clé secrète est récupérée via @Value("${my.custom.secret}").
 */



    @Configuration
    public class SecurityConfig {


        /**
         * On injecte la clé secrète à partir de l'application.properties
         */
        @Value("${spring.security.oauth2.resourceserver.jwt.secret-value}")




        private String secret;


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

        @Bean
        public Filter jwtAuthFilter() {
            return new JwtAuthFilter();
        }

}

