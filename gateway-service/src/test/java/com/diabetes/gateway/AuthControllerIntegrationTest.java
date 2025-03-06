package com.diabetes.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testLoginGet_withValidCredentials() {
        // On simule l'authentification Basic avec les identifiants configurés (ex. admin/1234)
        TestRestTemplate authRestTemplate = restTemplate.withBasicAuth("admin", "1234");
        ResponseEntity<String> response = authRestTemplate.getForEntity("/auth/login", String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Authentifié avec Basic Auth via GET");
    }

    @Test
    public void testLoginPost_withValidCredentials() {
        TestRestTemplate authRestTemplate = restTemplate.withBasicAuth("admin", "1234");
        ResponseEntity<String> response = authRestTemplate.postForEntity("/auth/login", null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Authentifié avec Basic Auth via POST");
    }
}
