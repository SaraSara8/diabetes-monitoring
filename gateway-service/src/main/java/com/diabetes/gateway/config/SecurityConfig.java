package com.diabetes.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Autoriser l'accès aux ressources statiques
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // Autoriser POST sur /auth/login sans authentification
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        // Vous pouvez autoriser GET sur /auth/login aussi, si nécessaire
                        .requestMatchers("/auth/**").permitAll()
                        // Le reste nécessite une authentification
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // Active Basic Auth pour les autres endpoints
        return http.build();
    }


    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("1234")) // Mot de passe encodé via BCrypt
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
