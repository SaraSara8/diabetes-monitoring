package com.diabetes.risk.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration Feign pour intercepter les requêtes et ajouter un header d'authentification Basic
 * en utilisant les identifiants du service patient.
 */

@Configuration
public class PatientsFeignConfig implements RequestInterceptor {

    @Value("${patient.service.user:admin}")
    private String username;

    @Value("${patient.service.password:password}")
    private String password;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        requestTemplate.header("Authorization", "Basic " + encodedAuth);
    }
}
