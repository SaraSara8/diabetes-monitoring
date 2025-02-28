package com.diabetes.risk.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Configuration Feign pour le client NotesClient afin d'ajouter un en-tête Basic Auth.
 */
public class NotesFeignConfig {

    @Value("${notes.service.user:admin}")
    private String notesServiceUser;

    @Value("${notes.service.password:1234}")
    private String notesServicePassword;

    /**
     * Crée un RequestInterceptor qui ajoute l'en-tête Authorization avec les identifiants encodés.
     *
     * @return le RequestInterceptor configuré pour Basic Auth
     */
    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return requestTemplate -> {
            String auth = notesServiceUser + ":" + notesServicePassword;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            requestTemplate.header("Authorization", "Basic " + encodedAuth);
        };
    }
}